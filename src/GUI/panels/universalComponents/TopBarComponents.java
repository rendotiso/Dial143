package GUI.panels.universalComponents;

import GUI.panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import GUI.panels.inventoryComponents.*;

public class TopBarComponents extends JPanel {
    private Font pixelFont;
    private SettingsPanel  settings;
    private InventoryPanel inventory;
    private MainFrame      mainFrame;
    private String         parentScreen = "shift";

    private Runnable onSettingsOpening;
    private Runnable onSettingsClosed;

    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnInventory;
    private javax.swing.JLabel  lpLabel;
    private javax.swing.JLabel  lpValue;
    private javax.swing.JLabel  ppLabel;
    private javax.swing.JLabel  ppValue;
    private javax.swing.JLabel  salaryLabel;
    private javax.swing.JLabel  salaryValue;

    public TopBarComponents(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadCustomFont();

        setBounds(0, 0, 1280, 720);
        setLayout(null);
        setOpaque(false);

    ppLabel = new JLabel("Performance:");
    ppLabel.setBounds(20, 10, 180, 30);
    ppLabel.setForeground(Color.WHITE);
    ppLabel.setIcon(loadIcon("performance meter.png", 28, 28));
    ppLabel.setFont(pixelFont != null ? pixelFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
    add(ppLabel);

    ppValue = new JLabel("0");
    ppValue.setBounds(160, 10, 60, 30);
    ppValue.setForeground(Color.WHITE);
    ppValue.setFont(pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Arial", Font.BOLD, 18));
    add(ppValue);

    lpLabel = new JLabel("Love Meter:");
    lpLabel.setBounds(20, 38, 130, 30);
    lpLabel.setForeground(Color.WHITE);
    lpLabel.setIcon(loadIcon("love meter.png", 28, 28));
    lpLabel.setFont(pixelFont != null ? pixelFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
    add(lpLabel);

    lpValue = new JLabel("0");
    lpValue.setBounds(160, 38, 60, 30);
    lpValue.setForeground(Color.WHITE);
    lpValue.setFont(pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Arial", Font.BOLD, 18));
    add(lpValue);

    salaryLabel = new JLabel("Salary:");
    salaryLabel.setBounds(20, 66, 130, 30);
    salaryLabel.setForeground(Color.WHITE);
    salaryLabel.setIcon(loadIcon("salaryIcon.png", 28, 28));
    salaryLabel.setFont(pixelFont != null ? pixelFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
    add(salaryLabel);

    salaryValue = new JLabel("0");
    salaryValue.setBounds(160, 66, 60, 30);
    salaryValue.setForeground(Color.WHITE);
    salaryValue.setFont(pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Arial", Font.BOLD, 18));
    add(salaryValue);

    btnInventory  = new JButton();
    btnInventory.setBounds(1120, 15, 100, 35);
    btnInventory.setIcon(loadIcon("inventory icon.png", 80, 35));
    styleButton(btnInventory);
    btnInventory.addActionListener(e -> handleInventoryClick());
    add(btnInventory);

    btnSettings = new JButton();
    btnSettings.setBounds(1180, 15, 100, 35);
    btnSettings.setIcon(loadIcon("settings icon.png", 80, 35));
    styleButton(btnSettings);
    add(btnSettings);

        setComponentZOrder(ppLabel,      0);
        setComponentZOrder(ppValue,      1);
        setComponentZOrder(lpLabel,      2);
        setComponentZOrder(lpValue,      3);
        setComponentZOrder(btnInventory, 4);
        setComponentZOrder(btnSettings,  5);

    }

    // ── Dark gradient behind stats so labels are readable on any background ───

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Horizontal fade: opaque black on left → transparent at 280px
        GradientPaint gradient = new GradientPaint(
            0,   0, new Color(0, 0, 0, 180),
            280, 0, new Color(0, 0, 0, 0)
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, 280, 105);
        g2.dispose();
        super.paintComponent(g);
    }
    
    
    // SETTING ICON IMAGE
    private ImageIcon loadIcon(String filename, int maxW, int maxH) {
        try {
            java.io.InputStream s = getClass().getResourceAsStream("/GUI/resources/icons/" + filename);
            if (s == null) return null;
            java.awt.image.BufferedImage raw = javax.imageio.ImageIO.read(s);
            double scale = Math.min((double) maxW / raw.getWidth(), (double) maxH / raw.getHeight());
            int newW = Math.max(1, (int)(raw.getWidth()  * scale));
            int newH = Math.max(1, (int)(raw.getHeight() * scale));
            return new ImageIcon(raw.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
        } catch (Exception ex) {
            return null;
        }
    }
    // ── Settings / inventory wiring ───────────────────────────────────────────

    public void setSettingsPanel(SettingsPanel settings) {
        this.settings = settings;
        setupSettingsButton();
    }

    public void setInventoryPanel(InventoryPanel inventory) {
        this.inventory = inventory;
    }

    private void setupSettingsButton() {
        for (ActionListener al : btnSettings.getActionListeners()) {
            btnSettings.removeActionListener(al);
        }
        btnSettings.addActionListener(e -> {
            if (settings == null) {
                System.err.println("SettingsPanel not set in TopBarComponents");
                return;
            }
            btnSettings.setEnabled(false);
            if (onSettingsOpening != null) onSettingsOpening.run();
            settings.setPreviousScreen(parentScreen);
            settings.showAsPopup();
            handleSettingsClosed();
            if (onSettingsClosed != null) onSettingsClosed.run();
        });
    }

    private void handleSettingsClosed() {
        SwingUtilities.invokeLater(() -> {
            btnSettings.setEnabled(true);
            requestFocusInWindow();
        });
    }

    private void handleInventoryClick() {
        if (inventory == null) {
            System.err.println("InventoryPanel not set in TopBarComponents");
            return;
        }
        btnInventory.setEnabled(false);
        inventory.showAsPopup();
        SwingUtilities.invokeLater(() -> {
            btnInventory.setEnabled(true);
            requestFocusInWindow();
        });
    }

    private void styleButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ── Callbacks / config ────────────────────────────────────────────────────

    public void setParentScreen(String screenName)   { this.parentScreen = screenName; }
    public void onSettingsOpening(Runnable callback) { this.onSettingsOpening = callback; }
    public void onSettingsClosed(Runnable callback)  { this.onSettingsClosed = callback; }

    // ── Font ──────────────────────────────────────────────────────────────────

    private void loadCustomFont() {
        try {
            InputStream stream = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (stream == null) { pixelFont = null; return; }
            Font base = Font.createFont(Font.TRUETYPE_FONT, stream);
            java.util.Map<java.awt.font.TextAttribute, Object> attrs = new java.util.HashMap<>();
            attrs.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            pixelFont = base.deriveFont(attrs);
        } catch (Exception e) {
            pixelFont = null;
        }
    }

    // ── Size overrides ────────────────────────────────────────────────────────

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }

    // ── Stats API ─────────────────────────────────────────────────────────────

    public int getCurrentPpValue()     { try { return Integer.parseInt(ppValue.getText());     } catch (NumberFormatException e) { return 0; } }
    public int getCurrentLpValue()     { try { return Integer.parseInt(lpValue.getText());     } catch (NumberFormatException e) { return 0; } }
    public int getCurrentSalaryValue() { try { return Integer.parseInt(salaryValue.getText()); } catch (NumberFormatException e) { return 0; } }

    public void setPpValue(int points)     { ppValue.setText(String.valueOf(points)); }
    public void setLpValue(int points)     { lpValue.setText(String.valueOf(points)); }
    public void setSalaryValue(int points) { salaryValue.setText(String.valueOf(points)); }

    public void setStats(int pp, int lp)     { setPpValue(pp); setLpValue(lp); }
    public void addPpPoints(int points)      { setPpValue(getCurrentPpValue() + points); }
    public void addLpPoints(int points)      { setLpValue(getCurrentLpValue() + points); }
    public void addSalaryPoints(int points)  { setSalaryValue(getCurrentSalaryValue() + points); }
    public void resetStats()                 { setPpValue(0); setLpValue(0); setSalaryValue(0); }

    // ── Button accessors ──────────────────────────────────────────────────────

    public JButton getBtnSettings()  { return btnSettings; }
    public JButton getBtnInventory() { return btnInventory; }

    public void setSettingsButtonVisible(boolean v)  { btnSettings.setVisible(v); }
    public void setSettingsButtonEnabled(boolean v)  { btnSettings.setEnabled(v); }
    public void setInventoryButtonVisible(boolean v) { btnInventory.setVisible(v); }
    public void setInventoryButtonEnabled(boolean v) { btnInventory.setEnabled(v); }

    public Rectangle getSettingsButtonBounds()  { return btnSettings.getBounds(); }
    public Rectangle getInventoryButtonBounds() { return btnInventory.getBounds(); }
}