package Entities;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {

    public enum Track {
        INTRO          ("/GUI/resources/audio/INTRO.wav"),
        GAMEPLAY       ("/GUI/resources/audio/GAMEPLAY.wav"),
        GOOD_ENDING    ("/GUI/resources/audio/GOOD_ENDING.wav"),
        BAD_ENDING     ("/GUI/resources/audio/BAD_ENDING.wav"),
        NEUTRAL_ENDING ("/GUI/resources/audio/NEUTRAL_ENDING.wav");

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
            if (m.getName().startsWith("default")) {
                return m;
            }
        }
        
        return null;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void play(Track track) {
        if (track == currentTrack && currentClip != null && currentClip.isRunning()) return;
        stopInternal();

        Clip clip = loadClip(track);
        if (clip == null) return;

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
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
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
        Clip  clipToFade = currentClip;
        float startVol   = muted ? 0f : volume;
        int   steps      = 40;
        int   stepMs     = Math.max(1, durationMs / steps);
        currentClip  = null;
        currentTrack = null;
        Thread t = new Thread(() -> {
            for (int i = steps; i >= 0; i--) {
                applyVolume(clipToFade, startVol * (i / (float) steps));
                try { Thread.sleep(stepMs); } catch (InterruptedException e) { break; }
            }
            clipToFade.stop();
            clipToFade.close();
        }, "audio-fadeout");
        t.setDaemon(true);
        t.start();
    }

    public void crossfadeTo(Track next, int durationMs) {
        fadeOut(durationMs);
        javax.swing.Timer delay = new javax.swing.Timer(durationMs / 4, e -> play(next));
        delay.setRepeats(false);
        delay.start();
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

    // ── Internal ──────────────────────────────────────────────────────────────

    private void stopInternal() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    private Clip loadClip(Track track) {
        try {
            InputStream raw = AudioPlayer.class.getResourceAsStream(track.path);
            if (raw == null) {
                raw = AudioPlayer.class.getClassLoader()
                        .getResourceAsStream(track.path.startsWith("/")
                            ? track.path.substring(1) : track.path);
            }
            if (raw == null) {
                return null;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            AudioFormat fmt = ais.getFormat();

            if (!fmt.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)
                    && !fmt.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                ais = AudioSystem.getAudioInputStream(toPCM(fmt), ais);
            }
            
            Clip clip;
            if (preferredMixer != null) {
                Mixer mixer = AudioSystem.getMixer(preferredMixer);
                DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
                if (mixer.isLineSupported(info)) {
                    clip = (Clip) mixer.getLine(info);
                } else {
                    clip = AudioSystem.getClip();
                }
            } else {
                clip = AudioSystem.getClip();
            }

            clip.open(ais);
            return clip;

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

    private AudioFormat toPCM(AudioFormat source) {
        float rate     = source.getSampleRate() > 0 ? source.getSampleRate() : 44100f;
        int   channels = source.getChannels()   > 0 ? source.getChannels()   : 2;
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                rate, 16, channels, channels * 2, rate, false);
    }
}