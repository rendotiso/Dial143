package GUI.panels.titleScreenComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreenComponents extends JPanel {

    private JLabel  titleLabel;
    private JButton btnPlay;
    private JButton btnSave;
    private JButton btnExit;

    private Runnable onPlay;
    private Runnable onSave;
    private Runnable onExit;

    // ── Match CallCreation palette exactly ────────────────────────────────────
    private static final Color CHOICE_NORMAL = new Color(238, 238, 238);
    private static final Color CHOICE_HOVER  = new Color(200, 215, 240);
    private static final Color CHOICE_CHOSEN = new Color(180, 195, 225);
    private static final Color CHOICE_BORDER = new Color(160, 160, 160);
    private static final Color TEXT_COLOR    = new Color(30,  30,  30);
    private static final Font  CHOICE_FONT   = new Font("Arial", Font.PLAIN, 14);

    public TitleScreenComponents() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(1280, 720));
        initComponents();
    }

    private void initComponents() {
        titleLabel = new JLabel("DIAL 143");
        titleLabel.setFont(new Font("Jomolhari", Font.ITALIC, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(58, 78, 294, 216);
        add(titleLabel);

        btnPlay = buildChoiceButton("New Game");
        btnPlay.setBounds(88, 444, 236, 38);
        btnPlay.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        add(btnPlay);

        btnSave = buildChoiceButton("Load Save");
        btnSave.setBounds(88, 492, 236, 38);
        btnSave.addActionListener(e -> { if (onSave != null) onSave.run(); });
        add(btnSave);

        btnExit = buildChoiceButton("Exit");
        btnExit.setBounds(88, 540, 236, 38);
        btnExit.addActionListener(e -> { if (onExit != null) onExit.run(); });
        add(btnExit);
    }

    private JButton buildChoiceButton(String label) {
        JButton btn = new JButton(label) {
            private boolean hov     = false;
            private boolean pressed = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hov     = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hov     = false; repaint(); }
                    @Override public void mousePressed(MouseEvent e) { pressed = true;  repaint(); }
                    @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Color bg = pressed ? CHOICE_CHOSEN : hov ? CHOICE_HOVER : CHOICE_NORMAL;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(CHOICE_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                g2.setFont(CHOICE_FONT);
                g2.setColor(TEXT_COLOR);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public void setTitleImage(String filename) {
        try {
            java.io.InputStream s = getClass().getResourceAsStream("/GUI/resources/icons/" + filename);
            if (s == null) return;
            java.awt.image.BufferedImage raw = javax.imageio.ImageIO.read(s);
            int maxW = 500, maxH = 300;
            double scale = Math.min((double) maxW / raw.getWidth(), (double) maxH / raw.getHeight());
            int newW = Math.max(1, (int)(raw.getWidth()  * scale));
            int newH = Math.max(1, (int)(raw.getHeight() * scale));
            Image scaled = raw.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            titleLabel.setIcon(new ImageIcon(scaled));
            titleLabel.setText("");
            int lx = (88 + 236 / 2) - newW / 2; 
            titleLabel.setBounds(lx, 78, newW, newH);
        } catch (Exception ex) {
        }
    }

    public void setPlayAction(Runnable action) { this.onPlay = action; }
    public void setSaveAction(Runnable action) { this.onSave = action; }
    public void setExitAction(Runnable action) { this.onExit = action; }
}