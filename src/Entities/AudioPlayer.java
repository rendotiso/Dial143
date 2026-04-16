package Entities;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {

    public enum Track {
        INTRO          ("/GUI/resources/audio/INTRO.wav"),
        GAMEPLAY       ("/GUI/resources/audio/GAMEPLAY.wav"),
        GOOD_ENDING    ("/GUI/resources/audio/GOOD ENDING.wav"),
        BAD_ENDING     ("/GUI/resources/audio/BAD ENDING.wav"),
        NEUTRAL_ENDING ("/GUI/resources/audio/NEUTRAL ENDING.wav");

        public final String path;
        Track(String path) { this.path = path; }
    }

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static AudioPlayer instance;
    public static AudioPlayer getInstance() {
        if (instance == null) instance = new AudioPlayer();
        return instance;
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private Clip    currentClip  = null;
    private Track   currentTrack = null;
    private float   volume       = 0.8f;
    private boolean muted        = false;

    private volatile boolean fadingOut = false;

    private final Map<Track, Boolean> loopMap = new HashMap<>();
    private Mixer.Info preferredMixer = null;

    private AudioPlayer() {
        loopMap.put(Track.INTRO,          true);
        loopMap.put(Track.GAMEPLAY,       true);
        loopMap.put(Track.GOOD_ENDING,    false);
        loopMap.put(Track.BAD_ENDING,     false);
        loopMap.put(Track.NEUTRAL_ENDING, false);
        preferredMixer = detectMixer();
    }

    private Mixer.Info detectMixer() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info m : mixers) {
            System.out.println("  " + m.getName() + " | " + m.getDescription());
        }
        for (Mixer.Info m : mixers) {
            if (m.getName().contains("plughw:1,0") && m.getDescription().contains("HDA Analog")) {
                return m;
            }
        }
        for (Mixer.Info m : mixers) {
            String name = m.getName();
            String desc = m.getDescription();
            if (name.contains("plughw")
                    && !name.contains("Loopback")
                    && !desc.contains("HDMI")
                    && !desc.contains("DMIC")) {
                return m;
            }
        }
        for (Mixer.Info m : mixers) {
            if (m.getName().startsWith("default")) return m;
        }
        return null;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void play(Track track) {
        if (track == currentTrack && currentClip != null && currentClip.isRunning()) return;

        stopInternal();
        Clip clip = loadClip(track);
        if (clip == null) {
            System.err.println("AudioPlayer: failed to load clip for " + track);
            return;
        }

        currentClip  = clip;
        currentTrack = track;
        applyVolume(clip, muted ? 0f : volume);

        if (Boolean.TRUE.equals(loopMap.get(track))) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    public void play(Track track, boolean loop) {
        loopMap.put(track, loop);
        play(track);
    }

    public void stop() {
        stopInternal();
        currentTrack = null;
    }

    public void pause() {
        if (currentClip != null && currentClip.isRunning()) currentClip.stop();
    }

    public void resume() {
        if (currentClip != null && !currentClip.isRunning()) {
            if (Boolean.TRUE.equals(loopMap.get(currentTrack))) {
                currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                currentClip.start();
            }
        }
    }

    public void fadeOut(int durationMs) {
        if (currentClip == null || !currentClip.isRunning()) return;

        final Clip  clipToFade = currentClip;
        final float startVol   = muted ? 0f : volume;
        final int   steps      = 40;
        final int   stepMs     = Math.max(1, durationMs / steps);

        currentClip  = null;
        currentTrack = null;
        fadingOut    = true;

        Thread t = new Thread(() -> {
            for (int i = steps; i >= 0; i--) {
                applyVolume(clipToFade, startVol * (i / (float) steps));
                try { Thread.sleep(stepMs); } catch (InterruptedException e) { break; }
            }
            clipToFade.stop();
            clipToFade.close();
            fadingOut = false;
        }, "audio-fadeout");
        t.setDaemon(true);
        t.start();
    }
    
    public void crossfadeTo(Track next, int durationMs) {
        fadeOut(durationMs);

        final int waitMs = durationMs + 50; 
        Thread waiter = new Thread(() -> {
            long deadline = System.currentTimeMillis() + waitMs;
            while (fadingOut && System.currentTimeMillis() < deadline) {
                try { Thread.sleep(20); } catch (InterruptedException e) { break; }
            }
            javax.swing.SwingUtilities.invokeLater(() -> play(next));
        }, "audio-crossfade-wait");
        waiter.setDaemon(true);
        waiter.start();
    }

    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
        if (currentClip != null && !muted) applyVolume(currentClip, volume);
    }

    public void setMuted(boolean mute) {
        muted = mute;
        if (currentClip != null) applyVolume(currentClip, muted ? 0f : volume);
    }

    public float   getVolume()            { return volume; }
    public boolean isMuted()              { return muted; }
    public Track   getCurrentTrack()      { return currentTrack; }
    public boolean isPlaying()            { return currentClip != null && currentClip.isRunning(); }
    public boolean isPlaying(Track track) { return track == currentTrack && isPlaying(); }

    private void stopInternal() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    private Clip loadClip(Track track) {
        try {
            AudioFormat[] formatsToTry = {
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false),
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true),
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false),
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false),
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 24, 2, 6, 44100, false),
            };

            AudioInputStream pcmStream = null;
            AudioFormat      workingFormat = null;

            for (AudioFormat fmt : formatsToTry) {
                AudioInputStream raw = openStream(track);
                if (raw == null) return null;
                try {
                    pcmStream     = AudioSystem.getAudioInputStream(fmt, raw);
                    workingFormat = fmt;
                    break;
                } catch (IllegalArgumentException e) {
                    raw.close(); 
                }
            }
            if (pcmStream == null) {
                AudioInputStream raw = openStream(track);
                if (raw == null) return null;
                AudioFormat src = raw.getFormat();
                AudioFormat target = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    src.getSampleRate(),
                    16,
                    src.getChannels(),
                    src.getChannels() * 2,
                    src.getSampleRate(),
                    false
                );
                pcmStream = AudioSystem.getAudioInputStream(target, raw);
            }
            
            if (preferredMixer != null) {
                try {
                    Mixer mixer = AudioSystem.getMixer(preferredMixer);
                    DataLine.Info info = new DataLine.Info(Clip.class, pcmStream.getFormat());
                    if (mixer.isLineSupported(info)) {
                        Clip clip = (Clip) mixer.getLine(info);
                        clip.open(pcmStream);
                        return clip;
                    }
                } catch (Exception e) {
                    System.err.println("Preferred mixer failed: " + e.getMessage());
                }
            }
            
            Clip clip = AudioSystem.getClip();
            clip.open(pcmStream);
            return clip;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private AudioInputStream openStream(Track track) {
        InputStream raw = AudioPlayer.class.getResourceAsStream(track.path);
        if (raw == null) {
            String stripped = track.path.startsWith("/") ? track.path.substring(1) : track.path;
            raw = AudioPlayer.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (raw == null) {
            System.err.println("AudioPlayer: resource not found: " + track.path);
            return null;
        }
        try {
            return AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void applyVolume(Clip clip, float linearVolume) {
        if (clip == null) return;
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = linearVolume <= 0f
                ? gain.getMinimum()
                : 20f * (float) Math.log10(Math.max(linearVolume, 0.0001f));
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
            gain.setValue(dB);
            return;
        }
        if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            vol.setValue(Math.max(vol.getMinimum(), Math.min(vol.getMaximum(), linearVolume)));
        }
    }
}