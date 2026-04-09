package GUI.panels.universalComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TransitionLayer extends JPanel {
    
    // Slower, smoother transition settings
    private static final int   FADE_STEPS    = 20;  // More steps for smoother fade
    private static final int   FADE_DELAY_MS = 16;  // ~60 FPS
    private static final float ALPHA_STEP    = 1f / FADE_STEPS;
    
    private float   alpha       = 0f;
    private boolean active      = false;
    private Timer   fadeTimer;
    private Runnable pendingCallback = null;
    private boolean isFadingOut = false;
    private boolean isFadingIn = false;

    public TransitionLayer() {
        setOpaque(false);
        setLayout(null);
        
        // Block all mouse/key events during transition
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});
        
        setFocusable(true);
        setVisible(false);
    }
    
    /**
     * Fade to black, then execute callback
     */
    public void fadeOut(Runnable onComplete) {
        // Don't interrupt a fade-out that's already in progress
        if (isFadingOut) return;
        
        // If fading in, cancel it and start from current alpha
        if (isFadingIn && fadeTimer != null) {
            fadeTimer.stop();
            isFadingIn = false;
        }
        
        stopTimer();
        isFadingOut = true;
        isFadingIn = false;
        alpha = Math.max(0f, Math.min(1f, alpha));
        active = true;
        setVisible(true);
        requestFocusInWindow();
        
        // Ensure we're fully visible before starting fade out
        if (alpha < 0.05f) {
            alpha = 0f;
            repaint();
        }
        
        pendingCallback = onComplete;
        
        fadeTimer = new Timer(FADE_DELAY_MS, null);
        fadeTimer.addActionListener(e -> {
            alpha = Math.min(1f, alpha + ALPHA_STEP);
            repaint();
            
            if (alpha >= 1f) {
                stopTimer();
                isFadingOut = false;
                if (pendingCallback != null) {
                    Runnable callback = pendingCallback;
                    pendingCallback = null;
                    callback.run();
                }
            }
        });
        fadeTimer.start();
    }

    /**
     * Fade in from black
     */
    public void fadeIn() {
        fadeIn(null);
    }
    
    public void fadeIn(Runnable onComplete) {
        // Don't interrupt a fade-in that's already in progress
        if (isFadingIn) return;
        
        // If fading out, cancel it and start from current alpha
        if (isFadingOut && fadeTimer != null) {
            fadeTimer.stop();
            isFadingOut = false;
        }
        
        stopTimer();
        isFadingIn = true;
        isFadingOut = false;
        alpha = Math.min(1f, Math.max(0f, alpha));
        active = true;
        setVisible(true);
        
        pendingCallback = onComplete;
        
        fadeTimer = new Timer(FADE_DELAY_MS, null);
        fadeTimer.addActionListener(e -> {
            alpha = Math.max(0f, alpha - ALPHA_STEP);
            repaint();
            
            if (alpha <= 0f) {
                stopTimer();
                isFadingIn = false;
                active = false;
                setVisible(false);
                if (pendingCallback != null) {
                    Runnable callback = pendingCallback;
                    pendingCallback = null;
                    callback.run();
                }
            }
        });
        fadeTimer.start();
    }
    
    /**
     * Instantly show black screen
     */
    public void cutToBlack() {
        stopTimer();
        isFadingOut = false;
        isFadingIn = false;
        alpha = 1f;
        active = true;
        setVisible(true);
        repaint();
    }
    
    /**
     * Instantly clear black screen
     */
    public void cutToClear() {
        stopTimer();
        isFadingOut = false;
        isFadingIn = false;
        alpha = 0f;
        active = false;
        setVisible(false);
        repaint();
    }
    
    /**
     * Check if currently transitioning
     */
    public boolean isTransitioning() {
        return isFadingOut || isFadingIn;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!active && alpha <= 0) return;
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Use composite for smoother alpha blending
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.dispose();
    }
    
    private void stopTimer() {
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
            fadeTimer = null;
        }
    }
    
    @Override 
    public Dimension getMinimumSize() { 
        return new Dimension(1280, 720); 
    }
    
    @Override 
    public Dimension getMaximumSize() { 
        return new Dimension(1280, 720); 
    }
    
    @Override 
    public Dimension getPreferredSize() { 
        return new Dimension(1280, 720); 
    }
}