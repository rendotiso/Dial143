package GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsPanel extends JPanel {

    private MainFrame      mainFrame;
    private JDialog        dialog;
    private String         previousScreen  = "shift";
    private WindowListener currentListener;
    private boolean        isHiding        = false;

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG_WHITE     = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(210, 215, 230);
    private static final Color TEXT_PRIMARY = new Color(30,  40,  80);
    private static final Color BTN_EXIT_BG  = new Color(110,  40,  40);
    private static final Color BTN_EXIT_HOV = new Color(150,  55,  55);
    private static final Color BTN_SAVE_BG  = new Color(38,   85, 155);
    private static final Color BTN_SAVE_HOV = new Color(55,  110, 190);
    private static final Color BTN_CONT_BG  = new Color(55,  110,  75);
    private static final Color BTN_CONT_HOV = new Color(75,  140, 100);

    private Font titleFont;
    private Font btnFont;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadFonts();
        setPreferredSize(new Dimension(300, 340));
        setOpaque(false);
        buildUI();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setPreviousScreen(String screen) { this.previousScreen = screen; }

    public void showAsPopup(WindowListener listener) {
        if (isHiding) return;
        this.currentListener = listener;
        if (dialog != null && dialog.isVisible()) dialog.dispose();

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

        // ── Buttons — GridBagLayout centers the stack vertically ──────────────
        JButton btnContinue = buildBtn("Continue",      BTN_CONT_BG, BTN_CONT_HOV);
        JButton btnSaves    = buildBtn("Save / Load",   BTN_SAVE_BG, BTN_SAVE_HOV);
        JButton btnExit     = buildBtn("Exit to Title", BTN_EXIT_BG, BTN_EXIT_HOV);

        btnContinue.addActionListener(e -> hidePopup());
        btnSaves.addActionListener(e -> { hidePopup(); mainFrame.showSave(previousScreen); });
        btnExit.addActionListener(e  -> { hidePopup(); mainFrame.showScreen("title"); });

        JPanel btnStack = new JPanel();
        btnStack.setOpaque(false);
        btnStack.setLayout(new BoxLayout(btnStack, BoxLayout.Y_AXIS));
        btnContinue.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaves.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStack.add(btnContinue);
        btnStack.add(Box.createVerticalStrut(12));
        btnStack.add(btnSaves);
        btnStack.add(Box.createVerticalStrut(12));
        btnStack.add(btnExit);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(btnStack, new GridBagConstraints());

        wrapper.add(centerPanel, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);
    }

    // ── Button factory ────────────────────────────────────────────────────────

    private JButton buildBtn(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text) {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? hoverColor : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(btnFont);
        btn.setPreferredSize(new Dimension(212, 40));
        btn.setMaximumSize(new Dimension(212, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Fonts ─────────────────────────────────────────────────────────────────

    private void loadFonts() {
        try {
            java.io.InputStream s = getClass().getResourceAsStream(
                "/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            Font base = (s != null) ? Font.createFont(Font.TRUETYPE_FONT, s)
                                    : new Font("Georgia", Font.PLAIN, 12);
            java.util.Map<java.awt.font.TextAttribute, Object> w = new java.util.HashMap<>();
            w.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            titleFont = base.deriveFont(w).deriveFont(18f);
            btnFont   = base.deriveFont(w).deriveFont(14f);
        } catch (Exception ex) {
            titleFont = new Font("Georgia", Font.BOLD, 18);
            btnFont   = new Font("Georgia", Font.BOLD, 14);
        }
    }
}