package GUI.panels.universalComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

public class DaySummaryLayer extends JPanel {

    // ── State ─────────────────────────────────────────────────────────────────
    private int     day          = 1;
    private int     ppGained     = 0;
    private int     lpGained     = 0;
    private int     salaryGained = 0;
    private int     callsDone    = 0;
    private int     totalPP      = 0;
    private int     totalLP      = 0;
    private int     totalSalary  = 0;

    private Runnable onEndDay;
    private Runnable onGoToShop;

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG_CARD      = Color.WHITE;
    private static final Color BG_ROW       = new Color(245, 247, 252);
    private static final Color BG_ROW_ALT   = new Color(238, 242, 250);
    private static final Color TITLE_COLOR  = new Color(30,  40,  80);
    private static final Color SECTION_COLOR= new Color(110, 120, 150);
    private static final Color LABEL_COLOR  = new Color(50,  55,  75);
    private static final Color ACCENT_BLUE  = new Color(60,  110, 220);
    private static final Color ACCENT_PINK  = new Color(210,  60, 110);
    private static final Color ACCENT_GREEN = new Color(30,  150,  80);
    private static final Color BTN_SHOP_BG  = new Color(40,   90, 200);
    private static final Color BTN_END_BG   = new Color(80,   85, 105);
    private static final Color BORDER_COLOR = new Color(210, 215, 230);

    // ── Card geometry ─────────────────────────────────────────────────────────
    private static final int CARD_W = 420;
    private static final int CARD_H = 530;
    private static final int CARD_X = (1280 - CARD_W) / 2;
    private static final int CARD_Y = (720  - CARD_H) / 2;

    // ── Click regions ─────────────────────────────────────────────────────────
    private Rectangle btnShopRect;
    private Rectangle btnEndRect;
    private int hoveredBtn = -1; // 0=shop, 1=end

    private Font titleFont;
    private Font sectionFont;
    private Font labelFont;
    private Font valueFont;

    public DaySummaryLayer() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        loadFonts();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateHover(e.getPoint()); }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleClick(e.getPoint()); }
        });
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void load(int day,
                     int ppGained, int lpGained, int salaryGained, int callsDone,
                     int totalPP,  int totalLP,  int totalSalary,
                     Runnable onEndDay, Runnable onGoToShop) {
        this.day          = day;
        this.ppGained     = ppGained;
        this.lpGained     = lpGained;
        this.salaryGained = salaryGained;
        this.callsDone    = callsDone;
        this.totalPP      = totalPP;
        this.totalLP      = totalLP;
        this.totalSalary  = totalSalary;
        this.onEndDay     = onEndDay;
        this.onGoToShop   = onGoToShop;
        repaint();
    }

    // ── Input ─────────────────────────────────────────────────────────────────

    private void updateHover(Point p) {
        int prev = hoveredBtn;
        hoveredBtn = -1;
        if (btnShopRect != null && btnShopRect.contains(p)) hoveredBtn = 0;
        else if (btnEndRect != null && btnEndRect.contains(p)) hoveredBtn = 1;
        if (hoveredBtn != prev) repaint();
        setCursor(hoveredBtn >= 0
            ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            : Cursor.getDefaultCursor());
    }

    private void handleClick(Point p) {
        if (btnShopRect != null && btnShopRect.contains(p)) { if (onGoToShop != null) onGoToShop.run(); }
        else if (btnEndRect != null && btnEndRect.contains(p)) { if (onEndDay != null) onEndDay.run(); }
    }

    // ── Painting ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCard(g2);
        drawTitle(g2);
        drawCalls(g2);
        drawSection(g2, "EARNED THIS SHIFT", CARD_Y + 130);
        drawRow(g2, "Performance Points", "+" + ppGained,     ACCENT_BLUE,  BG_ROW,     CARD_Y + 152);
        drawRow(g2, "Love Points",         "+" + lpGained,     ACCENT_PINK,  BG_ROW_ALT, CARD_Y + 196);
        drawRow(g2, "Salary Earned",        "+" + salaryGained, ACCENT_GREEN, BG_ROW,     CARD_Y + 240);
        drawSection(g2, "RUNNING TOTALS", CARD_Y + 300);
        drawRow(g2, "Total Performance", String.valueOf(totalPP),   ACCENT_BLUE,  BG_ROW,     CARD_Y + 322);
        drawRow(g2, "Total Love",         String.valueOf(totalLP),   ACCENT_PINK,  BG_ROW_ALT, CARD_Y + 366);
        drawRow(g2, "Total Salary",        "₱" + totalSalary,        ACCENT_GREEN, BG_ROW,     CARD_Y + 410);
        drawButtons(g2);

        g2.dispose();
    }

    private void drawCard(Graphics2D g2) {
        // Shadow
        g2.setColor(new Color(0, 0, 0, 35));
        g2.fillRoundRect(CARD_X + 4, CARD_Y + 4, CARD_W, CARD_H, 14, 14);
        // White card
        g2.setColor(BG_CARD);
        g2.fillRoundRect(CARD_X, CARD_Y, CARD_W, CARD_H, 14, 14);
        // Border
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(CARD_X, CARD_Y, CARD_W - 1, CARD_H - 1, 14, 14);
    }

    private void drawTitle(Graphics2D g2) {
        g2.setFont(titleFont);
        g2.setColor(TITLE_COLOR);
        String t = "Day " + day + " Complete";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 52);
    }

    private void drawCalls(Graphics2D g2) {
        g2.setFont(labelFont.deriveFont(Font.ITALIC, 12f));
        g2.setColor(SECTION_COLOR);
        String t = callsDone + " / 5  calls handled";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 80);
    }

    private void drawSection(Graphics2D g2, String text, int y) {
        g2.setFont(sectionFont);
        g2.setColor(SECTION_COLOR);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, CARD_X + (CARD_W - fm.stringWidth(text)) / 2, y);
    }

    private void drawRow(Graphics2D g2, String label, String value, Color valueColor, Color rowBg, int y) {
        int rx = CARD_X + 24, rw = CARD_W - 48, rh = 36;
        g2.setColor(rowBg);
        g2.fillRoundRect(rx, y, rw, rh, 8, 8);
        g2.setFont(labelFont);
        g2.setColor(LABEL_COLOR);
        g2.drawString(label, rx + 14, y + (rh + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);
        g2.setFont(valueFont);
        g2.setColor(valueColor);
        FontMetrics fm = g2.getFontMetrics();
        int valueX = rx + rw - 14 - fm.stringWidth(value);
        g2.drawString(value, valueX, y + (rh + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void drawButtons(Graphics2D g2) {
        int btnY = CARD_Y + CARD_H - 68;
        int btnH = 44;
        int gap  = 12;
        int btnW = (CARD_W - 48 - gap) / 2;
        int leftX  = CARD_X + 24;
        int rightX = leftX + btnW + gap;

        btnShopRect = new Rectangle(leftX,  btnY, btnW, btnH);
        btnEndRect  = new Rectangle(rightX, btnY, btnW, btnH);

        drawBtn(g2, btnShopRect, "Go to Shop", BTN_SHOP_BG, hoveredBtn == 0);
        drawBtn(g2, btnEndRect,  "End Day",    BTN_END_BG,  hoveredBtn == 1);
    }

    private void drawBtn(Graphics2D g2, Rectangle r, String text, Color bg, boolean hovered) {
        g2.setColor(hovered ? bg.brighter() : bg);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        g2.setFont(valueFont);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
            r.x + (r.width  - fm.stringWidth(text)) / 2,
            r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2);
    }

    // ── Fonts ─────────────────────────────────────────────────────────────────

    private void loadFonts() {
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            Font base = (s != null) ? Font.createFont(Font.TRUETYPE_FONT, s) : new Font("Georgia", Font.PLAIN, 12);
            java.util.Map<java.awt.font.TextAttribute, Object> w = new java.util.HashMap<>();
            w.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            labelFont   = base.deriveFont(Font.PLAIN, 13f);
            sectionFont = base.deriveFont(w).deriveFont(11f);
            valueFont   = base.deriveFont(w).deriveFont(14f);
            titleFont   = base.deriveFont(w).deriveFont(20f);
        } catch (Exception ex) {
            labelFont   = new Font("Georgia", Font.PLAIN, 13);
            sectionFont = new Font("Georgia", Font.BOLD,  11);
            valueFont   = new Font("Georgia", Font.BOLD,  14);
            titleFont   = new Font("Georgia", Font.BOLD,  20);
        }
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}