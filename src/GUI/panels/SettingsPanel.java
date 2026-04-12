package GUI.panels;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import GUI.panels.universalComponents.ImageButtonCreation;
import Entities.AudioPlayer;

public class SettingsPanel extends JPanel {

    private MainFrame      mainFrame;
    private JDialog        dialog;
    private String         previousScreen  = "shift";
    private WindowListener currentListener;
    private boolean        isHiding        = false;

    private static final Color BG_WHITE     = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(210, 215, 230);
    private static final Color TEXT_PRIMARY = new Color(30,  40,  80);
    private static final Color MUTED_COLOR  = new Color(200, 60,  60);

    private Font titleFont;
    private Font btnFont;
    private Font labelFont;

    private ImageButtonCreation btnContinue;
    private ImageButtonCreation btnSaves;
    private ImageButtonCreation btnExit;

    // ── Audio controls ────────────────────────────────────────────────────────
    private JSlider  volumeSlider;
    private JButton  muteButton;
    private JLabel   volumeLabel;
    private boolean  updatingSlider = false; 

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadFonts();
        setPreferredSize(new Dimension(300, 440)); 
        setOpaque(false);
        buildUI();
    }

    public void setPreviousScreen(String screen) { this.previousScreen = screen; }

    public void setButtonImage(String filename) {
        btnContinue.setImage(filename);
        btnSaves.setImage(filename);
        btnExit.setImage(filename);
    }

    public void setButtonImages(String continueFile, String savesFile, String exitFile) {
        if (continueFile != null) btnContinue.setImage(continueFile);
        if (savesFile    != null) btnSaves.setImage(savesFile);
        if (exitFile     != null) btnExit.setImage(exitFile);
    }

    public void showAsPopup(WindowListener listener) {
        if (isHiding) return;
        this.currentListener = listener;
        if (dialog != null && dialog.isVisible()) dialog.dispose();

        syncAudioControls();

        dialog = new JDialog(mainFrame, "Settings", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setContentPane(this);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setAlwaysOnTop(true);

        if (listener != null) dialog.addWindowListener(listener);

        dialog.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { isHiding = false; }
        });
        dialog.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) hidePopup();
            }
        });
        dialog.setFocusable(true);
        dialog.setVisible(true);
    }

    public void showAsPopup() { showAsPopup(null); }

    public void hidePopup() {
        if (dialog != null && dialog.isVisible()) {
            if (currentListener != null) dialog.removeWindowListener(currentListener);
            dialog.dispose();
            dialog = null;
        }
    }

    // ── Sync slider/button to AudioPlayer state ───────────────────────────────

    private void syncAudioControls() {
        AudioPlayer audio = AudioPlayer.getInstance();
        updatingSlider = true;
        volumeSlider.setValue((int)(audio.getVolume() * 100));
        updatingSlider = false;
        updateMuteButton(audio.isMuted());
        updateVolumeLabel(audio.getVolume(), audio.isMuted());
    }

    private void updateMuteButton(boolean muted) {
        muteButton.setText(muted ? "Unmute" : "Mute");
        muteButton.setForeground(muted ? MUTED_COLOR : TEXT_PRIMARY);
    }

    private void updateVolumeLabel(float volume, boolean muted) {
        volumeLabel.setText(muted ? "Volume: muted" : "Volume: " + (int)(volume * 100) + "%");
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(35, 0, 20, 0));
        wrapper.add(title, BorderLayout.NORTH);

        // ── Center: buttons + audio ───────────────────────────────────────────
        JPanel centerStack = new JPanel();
        centerStack.setOpaque(false);
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));

        // Navigation buttons
        btnContinue = new ImageButtonCreation("Continue");
        btnSaves    = new ImageButtonCreation("Save / Load");
        btnExit     = new ImageButtonCreation("Exit to Title");
        setButtonImage("btn.png");

        Dimension btnSize = new Dimension(212, 40);
        for (ImageButtonCreation b : new ImageButtonCreation[]{ btnContinue, btnSaves, btnExit }) {
            b.setFont(btnFont);
            b.setPreferredSize(btnSize);
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        btnContinue.addActionListener(e -> hidePopup());
        btnSaves.addActionListener(e -> { hidePopup(); mainFrame.showSave(previousScreen); });
        btnExit.addActionListener(e  -> { hidePopup(); mainFrame.showScreen("title"); });

        centerStack.add(btnContinue);
        centerStack.add(Box.createVerticalStrut(12));
        centerStack.add(btnSaves);
        centerStack.add(Box.createVerticalStrut(12));
        centerStack.add(btnExit);

        // ── Divider ───────────────────────────────────────────────────────────
        centerStack.add(Box.createVerticalStrut(20));
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(212, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerStack.add(sep);
        centerStack.add(Box.createVerticalStrut(14));

        // ── Volume label ──────────────────────────────────────────────────────
        volumeLabel = new JLabel("Volume: " + (int)(AudioPlayer.getInstance().getVolume() * 100) + "%");
        volumeLabel.setFont(labelFont);
        volumeLabel.setForeground(TEXT_PRIMARY);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerStack.add(volumeLabel);
        centerStack.add(Box.createVerticalStrut(6));

        // ── Volume slider ─────────────────────────────────────────────────────
        volumeSlider = new JSlider(0, 100, (int)(AudioPlayer.getInstance().getVolume() * 100));
        volumeSlider.setOpaque(false);
        volumeSlider.setMaximumSize(new Dimension(212, 30));
        volumeSlider.setPreferredSize(new Dimension(212, 30));
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeSlider.setForeground(TEXT_PRIMARY);

        volumeSlider.addChangeListener(e -> {
            if (updatingSlider) return;
            float v = volumeSlider.getValue() / 100f;
            AudioPlayer.getInstance().setVolume(v);
            if (AudioPlayer.getInstance().isMuted() && volumeSlider.getValue() > 0) {
                AudioPlayer.getInstance().setMuted(false);
                updateMuteButton(false);
            }
            updateVolumeLabel(v, AudioPlayer.getInstance().isMuted());
        });

        centerStack.add(volumeSlider);
        centerStack.add(Box.createVerticalStrut(10));

        // ── Mute button ───────────────────────────────────────────────────────
        muteButton = new JButton(AudioPlayer.getInstance().isMuted() ? "Unmute" : "Mute");
        muteButton.setFont(labelFont);
        muteButton.setForeground(TEXT_PRIMARY);
        muteButton.setBackground(BG_WHITE);
        muteButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        muteButton.setFocusPainted(false);
        muteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        muteButton.setPreferredSize(new Dimension(100, 30));
        muteButton.setMaximumSize(new Dimension(100, 30));
        muteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        muteButton.addActionListener(e -> {
            AudioPlayer audio = AudioPlayer.getInstance();
            boolean nowMuted = !audio.isMuted();
            audio.setMuted(nowMuted);
            updateMuteButton(nowMuted);
            updateVolumeLabel(audio.getVolume(), nowMuted);
        });

        centerStack.add(muteButton);

        // ── Wrap in GridBagLayout to center horizontally ───────────────────────
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(centerStack, new GridBagConstraints());
        wrapper.add(centerPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    // ── Fonts ─────────────────────────────────────────────────────────────────

    private void loadFonts() {
        try {
            InputStream s = getClass().getResourceAsStream(
                "/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            Font base = (s != null) ? Font.createFont(Font.TRUETYPE_FONT, s)
                                    : new Font("Georgia", Font.PLAIN, 12);
            java.util.Map<java.awt.font.TextAttribute, Object> w = new java.util.HashMap<>();
            w.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            titleFont = base.deriveFont(w).deriveFont(18f);
            btnFont   = base.deriveFont(w).deriveFont(14f);
            labelFont = base.deriveFont(12f);
        } catch (Exception ex) {
            titleFont = new Font("Georgia", Font.BOLD, 18);
            btnFont   = new Font("Georgia", Font.BOLD, 14);
            labelFont = new Font("Georgia", Font.PLAIN, 12);
        }
    }
}