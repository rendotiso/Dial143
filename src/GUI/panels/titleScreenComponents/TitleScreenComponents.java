package GUI.panels.titleScreenComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import GUI.panels.universalComponents.ImageButtonCreation;

public class TitleScreenComponents extends JPanel {

    private JLabel              titleLabel;
    private ImageButtonCreation btnPlay;
    private ImageButtonCreation btnSave;
    private ImageButtonCreation btnExit;

    private Runnable onPlay;
    private Runnable onSave;
    private Runnable onExit;

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
        titleLabel.setBounds(125, 78, 294, 216);
        add(titleLabel);

        btnPlay = new ImageButtonCreation("New Game");
        btnPlay.setBounds(108, 444, 236, 38);
        btnPlay.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        add(btnPlay);

        btnSave = new ImageButtonCreation("Load Save");
        btnSave.setBounds(108, 492, 236, 38);
        btnSave.addActionListener(e -> { if (onSave != null) onSave.run(); });
        add(btnSave);

        btnExit = new ImageButtonCreation("Exit");
        btnExit.setBounds(108, 540, 236, 38);
        btnExit.addActionListener(e -> { if (onExit != null) onExit.run(); });
        add(btnExit);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setTitleImage(String filename) {
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/icons/" + filename);
            if (s == null) return;
            BufferedImage raw = ImageIO.read(s);
            int maxW = 500, maxH = 300;
            double scale = Math.min((double) maxW / raw.getWidth(), (double) maxH / raw.getHeight());
            int newW = Math.max(1, (int)(raw.getWidth()  * scale));
            int newH = Math.max(1, (int)(raw.getHeight() * scale));
            Image scaled = raw.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            titleLabel.setIcon(new ImageIcon(scaled));
            titleLabel.setText("");
            int lx = (88 + 236 / 2) - newW / 2;
            titleLabel.setBounds(lx, 78, newW, newH);
        } catch (Exception ex) { /* silently ignore */ }
    }

    /** Apply the same image to all three buttons. */
    public void setButtonImage(String filename) {
        btnPlay.setImage(filename);
        btnSave.setImage(filename);
        btnExit.setImage(filename);
    }

    /** Apply individual images per button; pass null to keep the default style. */
    public void setButtonImages(String playFile, String saveFile, String exitFile) {
        if (playFile != null) btnPlay.setImage(playFile);
        if (saveFile != null) btnSave.setImage(saveFile);
        if (exitFile != null) btnExit.setImage(exitFile);
    }

    public void setPlayAction(Runnable action) { this.onPlay = action; }
    public void setSaveAction(Runnable action) { this.onSave = action; }
    public void setExitAction(Runnable action) { this.onExit = action; }
}