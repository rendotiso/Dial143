package GUI.panels.universalComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TransitionLayer — a full-screen black fade overlay.
 * Add it as the TOP layer in MainFrame's glass pane.
 * Call fadeOut(callback) to fade to black, then fadeIn() to reveal next screen.
 *
 * Usage in MainFrame:
 *   transitionLayer.fadeOut(() -> {
 *       cardLayout.show(...);
 *       transitionLayer.fadeIn();
 *   });
 */
public class TransitionLayer extends JPanel {

    private static final int   FADE_STEPS    = 20;   // steps per fade
    private static final int   FADE_DELAY_MS = 16;   // ~60fps
    private static final float ALPHA_STEP    = 1f / FADE_STEPS;

    private float   alpha       = 0f;   // 0 = fully transparent, 1 = fully black
    private boolean active      = false; // only paint when needed
    private Timer   fadeTimer;

    public TransitionLayer() {
        setOpaque(false);
        setLayout(null);
        // Eat all input during transition so nothing is clickable
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});
        setFocusable(true);
        setVisible(false); // hidden until first transition
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Fades screen to black, then fires callback.
     * Callback should switch the card and call fadeIn().
     */
    public void fadeOut(Runnable onComplete) {
        stopTimer();
        alpha  = 0f;
        active = true;
        setVisible(true);
        requestFocusInWindow();

        fadeTimer = new Timer(FADE_DELAY_MS, null);
        fadeTimer.addActionListener(e -> {
            alpha = Math.min(1f, alpha + ALPHA_STEP);
            repaint();
            if (alpha >= 1f) {
                stopTimer();
                if (onComplete != null) onComplete.run();
            }
        });
        fadeTimer.start();
    }

    /**
     * Fades from black back to transparent, then hides the layer.
     */
    public void fadeIn() {
        fadeIn(null);
    }

    /** Fade in, then fire onComplete when fully visible. */
    public void fadeIn(Runnable onComplete) {
        stopTimer();
        alpha  = 1f;
        active = true;
        setVisible(true);

        fadeTimer = new Timer(FADE_DELAY_MS, null);
        fadeTimer.addActionListener(e -> {
            alpha = Math.max(0f, alpha - ALPHA_STEP);
            repaint();
            if (alpha <= 0f) {
                stopTimer();
                active = false;
                setVisible(false);
                if (onComplete != null) onComplete.run();
            }
        });
        fadeTimer.start();
    }

    /** Instant cut — no animation. Useful for new game. */
    public void cutToBlack() {
        stopTimer();
        alpha  = 1f;
        active = true;
        setVisible(true);
        repaint();
    }

    public void cutToClear() {
        stopTimer();
        alpha  = 0f;
        active = false;
        setVisible(false);
        repaint();
    }

    // ── Painting ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        if (!active) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0f, 0f, 0f, alpha));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void stopTimer() {
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}