package GUI.panels.saveComponents;

import GUI.panels.MainFrame;
import Storyline.RouteManager;
import Storyline.AmayaRoute.AmayaRoute;
import Entities.Character;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class SaveDisplayLayer extends JPanel {

    private final MainFrame mainFrame;
    private Runnable        onBack;

    private static final int SLOT_COUNT = 6;

    // ── Light theme palette ───────────────────────────────────────────────────
    private static final Color BG_CARD       = Color.WHITE;
    private static final Color BG_SLOT       = new Color(245, 247, 252);
    private static final Color BG_SLOT_HOV   = new Color(238, 242, 250);
    private static final Color BORDER_COLOR  = new Color(210, 215, 230);
    private static final Color TITLE_COLOR   = new Color(30,  40,  80);
    private static final Color TEXT_PRIMARY  = new Color(50,  55,  75);
    private static final Color TEXT_MUTED    = new Color(110, 120, 150);
    private static final Color ACCENT_BLUE   = new Color(60,  110, 220);
    private static final Color LOAD_BG       = new Color(55, 120, 80);  
    private static final Color DELETE_BG     = new Color(70, 35, 35); 
    private static final Color BACK_BG       = new Color(80, 85, 105); 
    private static final Color SHADOW_COLOR  = new Color(0, 0, 0, 35);

    // ── Persistence ───────────────────────────────────────────────────────────
    private static final String PREF_NODE = "dial143";
    private static final String PREF_KEY  = "save_slot_";

    // ── Slot rows ─────────────────────────────────────────────────────────────
    private JPanel   slotsContainer;
    private JPanel[] slotRows   = new JPanel[SLOT_COUNT];
    private JLabel[] metaLabels = new JLabel[SLOT_COUNT];
    private JButton[] loadBtns  = new JButton[SLOT_COUNT];
    private JButton[] delBtns   = new JButton[SLOT_COUNT];

    private Font titleFont;
    private Font slotFont;
    private Font metaFont;
    private Font btnFont;
    private Font subFont;

    public SaveDisplayLayer(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadFonts();
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(1280, 720));
        buildUI();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setOnBack(Runnable callback) { this.onBack = callback; }

    public void refresh() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            String meta   = readSlotLabel(i + 1);
            boolean filled = meta != null;
            metaLabels[i].setText(filled ? meta : "— Empty —");
            metaLabels[i].setForeground(filled ? ACCENT_BLUE : TEXT_MUTED);
            loadBtns[i].setEnabled(filled);
            delBtns[i].setEnabled(filled);
        }
        revalidate();
        repaint();
    }

    private void buildUI() {
        setBackground(BG_CARD);
        
        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout()) {
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(40, 60, 0, 60));

        JLabel titleLbl = new JLabel("SAVE  /  LOAD") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(TITLE_COLOR);
                g2.drawString(getText(), 0, fm.getAscent());
                g2.dispose();
            }
        };
        titleLbl.setFont(titleFont);
        header.add(titleLbl, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ── Slots ─────────────────────────────────────────────────────────────
        slotsContainer = new JPanel();
        slotsContainer.setOpaque(false);
        slotsContainer.setLayout(new BoxLayout(slotsContainer, BoxLayout.Y_AXIS));
        slotsContainer.setBorder(BorderFactory.createEmptyBorder(30, 60, 20, 60));

        for (int i = 0; i < SLOT_COUNT; i++) {
            slotsContainer.add(buildSlotRow(i));
            if (i < SLOT_COUNT - 1)
                slotsContainer.add(Box.createVerticalStrut(14));
        }

        JScrollPane scroll = new JScrollPane(slotsContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout()) {
        };
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(0, 90));
        footer.setBorder(BorderFactory.createEmptyBorder(30, 60, 20, 60));

        JButton backBtn = buildBtn("Back", BACK_BG, Color.WHITE, 120, 40);
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });
        footer.add(backBtn, BorderLayout.WEST);

        add(footer, BorderLayout.SOUTH);
    }

    private JPanel buildSlotRow(int index) {
        int slot = index + 1;

        JPanel row = new JPanel(new BorderLayout(12, 0)) {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hov = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(SHADOW_COLOR);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
                
                g2.setColor(hov ? BG_SLOT_HOV : BG_SLOT);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);
                
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(0, 70));
        row.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 18)); 

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel slotLbl = new JLabel("Slot " + slot);
        slotLbl.setFont(slotFont);
        slotLbl.setForeground(TEXT_PRIMARY);

        metaLabels[index] = new JLabel("— Empty —");
        metaLabels[index].setFont(metaFont);
        metaLabels[index].setForeground(TEXT_MUTED);

        info.add(slotLbl);
        info.add(Box.createVerticalStrut(5));
        info.add(metaLabels[index]);
        row.add(info, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setOpaque(false);

        JButton saveBtn = buildBtn("Save",   ACCENT_BLUE, Color.WHITE, 68, 32);
        JButton loadBtn = buildBtn("Load",   LOAD_BG, Color.WHITE, 68, 32);
        JButton delBtn  = buildBtn("Delete", DELETE_BG,  Color.WHITE, 68, 32);
        
        loadBtn.setEnabled(false);
        delBtn.setEnabled(false);

        saveBtn.addActionListener(e -> confirmSave(slot));
        loadBtn.addActionListener(e -> confirmLoad(slot));
        delBtn.addActionListener (e -> confirmDelete(slot));

        loadBtns[index] = loadBtn;
        delBtns[index]  = delBtn;

        btnPanel.add(saveBtn);
        btnPanel.add(loadBtn);
        btnPanel.add(delBtn);
        row.add(btnPanel, BorderLayout.EAST);

        slotRows[index] = row;
        return row;
    }


private void confirmSave(int slot) {
    JOptionPane pane = new JOptionPane(
        "Overwrite Slot " + slot + " with current progress?",
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.YES_NO_OPTION
    );
    JDialog dialog = pane.createDialog(this, "Confirm Save");
    makeButtonsNonFocusable(pane);
    dialog.setVisible(true);
    
    Object value = pane.getValue();
    if (value != null && value instanceof Integer) {
        int r = (Integer) value;
        if (r == JOptionPane.YES_OPTION) { 
            writeSlot(slot); 
            refresh(); 
        }
    }
}

private void confirmLoad(int slot) {
    JOptionPane pane = new JOptionPane(
        "Load Slot " + slot + "? Unsaved progress will be lost.",
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.YES_NO_OPTION
    );
    JDialog dialog = pane.createDialog(this, "Confirm Load");
    makeButtonsNonFocusable(pane);
    dialog.setVisible(true);
    
    Object value = pane.getValue();
    if (value != null && value instanceof Integer) {
        int r = (Integer) value;
        if (r == JOptionPane.YES_OPTION) readSlot(slot);
    }
}

private void confirmDelete(int slot) {
    JOptionPane pane = new JOptionPane(
        "Delete Slot " + slot + "? This cannot be undone.",
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.YES_NO_OPTION
    );
    JDialog dialog = pane.createDialog(this, "Confirm Delete");
    makeButtonsNonFocusable(pane);
    dialog.setVisible(true);
    
    Object value = pane.getValue();
    if (value != null && value instanceof Integer) {
        int r = (Integer) value;
        if (r == JOptionPane.YES_OPTION) { 
            deleteSlot(slot); 
            refresh(); 
        }
    }
}
        
    private void makeButtonsNonFocusable(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof AbstractButton) {
                comp.setFocusable(false);
            }
             if (comp instanceof Container) {
                makeButtonsNonFocusable((Container) comp);
             }
        }
    }
    

    // ── Persistence ───────────────────────────────────────────────────────────

    private Preferences prefs() { return Preferences.userRoot().node(PREF_NODE); }

    private void writeSlot(int slot) {
        Preferences p = prefs();
        String name = mainFrame.getPlayerName();
        RouteManager rm = mainFrame.getRouteManager();

        // Save display label
        p.put(PREF_KEY + slot, String.format("Day %d  •  %s  •  PP %d  LP %d  ₱%d",
            mainFrame.getCurrentDay(),
            (name == null || name.isEmpty()) ? "???" : name,
            mainFrame.getPP(), mainFrame.getLP(), mainFrame.getSalary()
        ));

        // Save basic stats
        p.putInt(PREF_KEY + slot + "_day",     mainFrame.getCurrentDay());
        p.putInt(PREF_KEY + slot + "_pp",      mainFrame.getPP());
        p.putInt(PREF_KEY + slot + "_salary",  mainFrame.getSalary());
        p.put(   PREF_KEY + slot + "_name",    name != null ? name : "");
        p.put(   PREF_KEY + slot + "_gender",  mainFrame.getPlayerGender()  != null ? mainFrame.getPlayerGender()  : "");
        p.put(   PREF_KEY + slot + "_pronoun", mainFrame.getPlayerPronoun() != null ? mainFrame.getPlayerPronoun() : "");
        p.putInt(PREF_KEY + slot + "_scene",   mainFrame.getDialogueScene());
        p.put(   PREF_KEY + slot + "_segment", mainFrame.getCurrentSegment().name());

        // Save per-character LP using constants from Entities.Character
        p.putInt(PREF_KEY + slot + "_lp_amaya",   rm.getLPForCharacter(Character.AMAYA));
        p.putInt(PREF_KEY + slot + "_lp_rosario", rm.getLPForCharacter(Character.ROSARIO));
        p.putInt(PREF_KEY + slot + "_lp_cloma",   rm.getLPForCharacter(Character.CLOMA));
        p.putInt(PREF_KEY + slot + "_lp_celeres", rm.getLPForCharacter(Character.CELERES));

        // Save active route info
        if (rm.hasActiveRoute()) {
            p.put(PREF_KEY + slot + "_active_char", rm.getActiveCharacter());
        } else {
            p.remove(PREF_KEY + slot + "_active_char");
        }

        try { p.flush(); } catch (Exception ignored) {}
    }

 private void readSlot(int slot) {
    Preferences p = prefs();
    if (p.get(PREF_KEY + slot, null) == null) return;

    mainFrame.resetStats();
    mainFrame.setPP(    p.getInt(PREF_KEY + slot + "_pp",     0));
    mainFrame.setSalary(p.getInt(PREF_KEY + slot + "_salary", 0));
    mainFrame.setCurrentDay(p.getInt(PREF_KEY + slot + "_day", 1)); 
    mainFrame.setPlayerIdentity(
        p.get(PREF_KEY + slot + "_name",    ""),
        p.get(PREF_KEY + slot + "_gender",  ""),
        p.get(PREF_KEY + slot + "_pronoun", "")
    );
    mainFrame.setDialogueScene(p.getInt(PREF_KEY + slot + "_scene", 0));

    try {
        mainFrame.setCurrentSegment(MainFrame.Segment.valueOf(
            p.get(PREF_KEY + slot + "_segment", "MORNING")));
    } catch (Exception ignored) {}
    
    RouteManager rm = mainFrame.getRouteManager();
    mainFrame.setLPForCharacter(Character.AMAYA,   p.getInt(PREF_KEY + slot + "_lp_amaya",   0));
    mainFrame.setLPForCharacter(Character.ROSARIO, p.getInt(PREF_KEY + slot + "_lp_rosario", 0));
    mainFrame.setLPForCharacter(Character.CLOMA,   p.getInt(PREF_KEY + slot + "_lp_cloma",   0));
    mainFrame.setLPForCharacter(Character.CELERES, p.getInt(PREF_KEY + slot + "_lp_celeres", 0));

    String activeChar = p.get(PREF_KEY + slot + "_active_char", null);
    if (activeChar != null && !activeChar.isEmpty()) {
        RouteManager route = routeForCharacter(activeChar);
        rm.selectRoute(route, activeChar);
    }

    mainFrame.showScreen("dialogue");
}
 
    private void deleteSlot(int slot) {
        Preferences p = prefs();
        String[] keys = { "", "_day", "_pp", "_lp", "_salary", "_name", "_gender", 
                          "_pronoun", "_scene", "_segment", "_lp_amaya", "_lp_rosario", 
                          "_lp_cloma", "_lp_celeres", "_active_char" };
        for (String k : keys) {
            p.remove(PREF_KEY + slot + k);
        }
        try { p.flush(); } catch (Exception ignored) {}
    }

    private String readSlotLabel(int slot) {
        return prefs().get(PREF_KEY + slot, null);
    }

    private RouteManager routeForCharacter(String name) {
        if (name == null) return new AmayaRoute();
        return switch (name) {
            case Character.AMAYA -> new AmayaRoute();
            // case Character.ROSARIO -> new RosarioRoute();
            // case Character.CLOMA -> new ClomaRoute();
            // case Character.CELERES -> new CeleresRoute();
            default -> new AmayaRoute();
        };
    }

    // ── Button factory ────────────────────────────────────────────────────────
    private JButton buildBtn(String text, Color bgColor, Color fgColor, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hov = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(SHADOW_COLOR);
                g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);

                Color c = !isEnabled() ? BG_SLOT
                        : hov          ? bgColor.brighter()
                        :                bgColor;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                g2.setColor(!isEnabled() ? TEXT_MUTED : hov ? Color.WHITE : fgColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();

                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        btn.setFont(btnFont);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setMaximumSize(new Dimension(w, h));
        btn.setMinimumSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 4, 0, 4));

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
            titleFont = base.deriveFont(w).deriveFont(22f);
            slotFont  = base.deriveFont(w).deriveFont(15f);
            metaFont  = base.deriveFont(Font.PLAIN, 13f);
            btnFont   = base.deriveFont(w).deriveFont(12f);
            subFont   = base.deriveFont(Font.PLAIN, 12f);
        } catch (Exception ex) {
            titleFont = new Font("Georgia", Font.BOLD,  22);
            slotFont  = new Font("Georgia", Font.BOLD,  15);
            metaFont  = new Font("Georgia", Font.PLAIN, 13);
            btnFont   = new Font("Georgia", Font.BOLD,  12);
            subFont   = new Font("Georgia", Font.PLAIN, 12);
        }
    }

    // ── Size overrides ────────────────────────────────────────────────────────

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}