package GUI.panels.shopComponents;

import Entities.Items.Items;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ShopLayer extends JPanel {

    // ── Shop state ────────────────────────────────────────────────────────────
    private final List<Items> shopItems = new ArrayList<>();
    private int currentIndex = 0;
    private int playerFunds  = 1000;
    private boolean isLoaded = false;

    // ── Callbacks ─────────────────────────────────────────────────────────────
    @FunctionalInterface public interface PurchaseListener { void onBought(Items item, int cost); }
    private PurchaseListener onPurchase;
    private Runnable         onExit;

    public void setOnPurchase(PurchaseListener listener) { this.onPurchase = listener; }
    public void setOnExit(Runnable callback)             { this.onExit     = callback; }

    // ── Geometry (matches your screenshot layout) ─────────────────────────────
    private static final int CARD_W = 480;
    private static final int CARD_H = 520;
    private static final int CARD_X = (1280 - CARD_W) / 2;
    private static final int CARD_Y = (720  - CARD_H) / 2 + 20;

    // Click regions
    private Rectangle btnBackRect;
    private Rectangle btnNextRect;
    private Rectangle btnBuyRect;
    private Rectangle btnExitRect;
    private int hoveredBtn = -1; // 0=back, 1=next, 2=buy, 3=exit

    // Toast state
    private String toastText    = "";
    private int    toastAlpha   = 0;
    private Timer  toastTimer;

    // ── Light theme colors (matching DaySummaryLayer) ─────────────────────────
    private static final Color BG_CARD       = Color.WHITE;
    private static final Color BORDER_COLOR  = new Color(210, 215, 230);
    private static final Color TITLE_COLOR   = new Color(30,  40,  80);
    private static final Color TEXT_PRIMARY  = new Color(50,  55,  75);
    private static final Color TEXT_MUTED    = new Color(110, 120, 150);
    private static final Color ACCENT_BLUE   = new Color(60,  110, 220);
    private static final Color BG_NAV        = new Color(245, 247, 252);
    private static final Color BG_NAV_HOVER  = new Color(238, 242, 250);
    private static final Color BG_BUY        = new Color(55, 120, 80);
    private static final Color BG_BUY_HOVER  = new Color(75, 160, 105);
    private static final Color BG_EXIT       = new Color(70, 35, 35);
    private static final Color BG_EXIT_HOVER = new Color(100, 55, 55);
    private static final Color SHADOW_COLOR  = new Color(0, 0, 0, 35);

    public ShopLayer() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateHover(e.getPoint()); }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleClick(e.getPoint()); }
        });
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void load() {
        if (!isLoaded) {
            initItems();
            isLoaded = true;
        }
        currentIndex = 0;
        repaint();
    }

    public void setPlayerFunds(int funds) {
        this.playerFunds = funds;
        repaint();
    }

    public int getPlayerFunds() {
        return playerFunds;
    }

    // ── Item definitions ──────────────────────────────────────────────────────

    private void initItems() {
        shopItems.clear();
        shopItems.add(new Items("Coffee",       "A strong cup of coffee.\nRestores focus and grants +5 PP.",  null, 0, 100,  Items.ItemType.CONSUMABLE));
        shopItems.add(new Items("Lunchbox",     "A hearty homemade meal.\nBoosts energy for +10 PP.",         null, 0, 250,  Items.ItemType.CONSUMABLE));
        shopItems.add(new Items("Energy Drink", "Instant energy boost.\n+15 PP but -5 LP.",                   null, 0, 150,  Items.ItemType.CONSUMABLE));
        shopItems.add(new Items("Love Letter",  "A heartfelt letter.\nGrants +20 LP.",                        null, 0, 300,  Items.ItemType.CONSUMABLE));
        shopItems.add(new Items("Study Notes",  "Detailed class notes.\n+10 PP and +5 LP.",                   null, 0, 400,  Items.ItemType.CONSUMABLE));
        shopItems.add(new Items("Gift Box",     "A beautifully wrapped gift.\nMassive +30 LP boost.",         null, 0, 500,  Items.ItemType.CONSUMABLE));
    }

    // ── Input handling ────────────────────────────────────────────────────────

    private void updateHover(Point p) {
        int prev = hoveredBtn;
        hoveredBtn = -1;
        if (btnBackRect != null && btnBackRect.contains(p)) hoveredBtn = 0;
        else if (btnNextRect != null && btnNextRect.contains(p)) hoveredBtn = 1;
        else if (btnBuyRect  != null && btnBuyRect.contains(p))  hoveredBtn = 2;
        else if (btnExitRect != null && btnExitRect.contains(p)) hoveredBtn = 3;
        if (hoveredBtn != prev) repaint();
        setCursor(hoveredBtn >= 0
            ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            : Cursor.getDefaultCursor());
    }

    private void handleClick(Point p) {
        if (!isLoaded) return;
        if (btnBackRect != null && btnBackRect.contains(p)) { navigate(-1); return; }
        if (btnNextRect != null && btnNextRect.contains(p)) { navigate(1);  return; }
        if (btnBuyRect  != null && btnBuyRect.contains(p))  { handleBuy();  return; }
        if (btnExitRect != null && btnExitRect.contains(p)) { handleExit(); return; }
    }

    private void navigate(int dir) {
        currentIndex = (currentIndex + dir + shopItems.size()) % shopItems.size();
        repaint();
    }

    private void handleBuy() {
        if (shopItems.isEmpty()) return;
        Items item = shopItems.get(currentIndex);
        if (playerFunds < item.getPrice()) {
            showToast("Not enough funds!");
            return;
        }
        playerFunds -= item.getPrice();
        item.addQuantity(1);
        if (onPurchase != null) onPurchase.onBought(item, item.getPrice());
        showToast("Bought " + item.getName() + "!");
        repaint();
    }

    private void handleExit() {
        if (onExit != null) onExit.run();
    }

    // ── Toast ─────────────────────────────────────────────────────────────────

    private void showToast(String text) {
        this.toastText  = text;
        this.toastAlpha = 255;
        if (toastTimer != null) toastTimer.stop();
        toastTimer = new Timer(40, e -> {
            toastAlpha -= 8;
            if (toastAlpha <= 0) {
                toastAlpha = 0;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        toastTimer.setInitialDelay(1200);
        toastTimer.start();
        repaint();
    }

    // ── Painting ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isLoaded || shopItems.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Items item = shopItems.get(currentIndex);

        drawCard(g2);
        drawTitle(g2);
        drawDivider(g2, CARD_Y + 62);
        drawItemName(g2, item);
        drawIcon(g2, item);
        drawNavButtons(g2);
        drawPageIndicator(g2);
        drawDescription(g2, item);
        drawDivider(g2, CARD_Y + 390);
        drawPriceRow(g2, item);
        drawFunds(g2);
        drawToast(g2);
        drawExitButton(g2);

        g2.dispose();
    }

    // ── Draw helpers ──────────────────────────────────────────────────────────

    private void drawCard(Graphics2D g2) {
        // Shadow
        g2.setColor(SHADOW_COLOR);
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
        g2.setFont(loadFont(26f, Font.BOLD));
        g2.setColor(TITLE_COLOR);
        String t = "SHOP";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 44);
    }

    private void drawDivider(Graphics2D g2, int y) {
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(CARD_X + 24, y, CARD_X + CARD_W - 24, y);
    }

    private void drawItemName(Graphics2D g2, Items item) {
        g2.setFont(loadFont(18f, Font.BOLD));
        g2.setColor(TITLE_COLOR);
        String t = item.getName();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 96);
    }

    private void drawIcon(Graphics2D g2, Items item) {
        int iconSize = 130;
        int ix = CARD_X + (CARD_W - iconSize) / 2;
        int iy = CARD_Y + 110;

        ImageIcon icon = item.getIcon(iconSize, iconSize);
        if (icon != null) {
            g2.drawImage(icon.getImage(), ix, iy, iconSize, iconSize, null);
        } else {
            // Placeholder box
            g2.setColor(BG_NAV);
            g2.fillRoundRect(ix, iy, iconSize, iconSize, 12, 12);
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(ix + 1, iy + 1, iconSize - 2, iconSize - 2, 12, 12);
            g2.setFont(loadFont(12f, Font.PLAIN));
            g2.setColor(TEXT_PRIMARY);
            FontMetrics fm = g2.getFontMetrics();
            String t = "ITEM ICON";
            g2.drawString(t, ix + (iconSize - fm.stringWidth(t)) / 2,
                             iy + iconSize / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void drawNavButtons(Graphics2D g2) {
        int iconSize = 130;
        int iy       = CARD_Y + 110;
        int btnW = 90, btnH = 32;
        int btnY = iy + (iconSize - btnH) / 2;

        btnBackRect = new Rectangle(CARD_X + 24,               btnY, btnW, btnH);
        btnNextRect = new Rectangle(CARD_X + CARD_W - 24 - btnW, btnY, btnW, btnH);

        drawNavBtn(g2, btnBackRect, "◀  Back", hoveredBtn == 0);
        drawNavBtn(g2, btnNextRect, "Next  ▶", hoveredBtn == 1);
    }

    private void drawNavBtn(Graphics2D g2, Rectangle r, String text, boolean hovered) {
        g2.setColor(hovered ? BG_NAV_HOVER : BG_NAV);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(r.x, r.y, r.width - 1, r.height - 1, 8, 8);
        g2.setFont(loadFont(12f, Font.PLAIN));
        g2.setColor(hovered ? ACCENT_BLUE : TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
            r.x + (r.width  - fm.stringWidth(text)) / 2,
            r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void drawPageIndicator(Graphics2D g2) {
        g2.setFont(loadFont(12f, Font.PLAIN));
        g2.setColor(TEXT_PRIMARY);
        String t = (currentIndex + 1) + " / " + shopItems.size();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 256);
    }

    private void drawDescription(Graphics2D g2, Items item) {
        g2.setFont(loadFont(13f, Font.PLAIN));
        g2.setColor(TEXT_PRIMARY);

        String[] lines = item.getDescription().split("\n");
        FontMetrics fm = g2.getFontMetrics();
        int startY = CARD_Y + 278;
        int lineH  = fm.getHeight() + 4;
        for (String line : lines) {
            int lx = CARD_X + (CARD_W - fm.stringWidth(line)) / 2;
            g2.drawString(line, lx, startY);
            startY += lineH;
        }
    }

    private void drawPriceRow(Graphics2D g2, Items item) {
        int rowY = CARD_Y + 412;

        // "Price:" label
        g2.setFont(loadFont(13f, Font.PLAIN));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString("Price:", CARD_X + 24, rowY);

        // Price value
        g2.setFont(loadFont(15f, Font.BOLD));
        g2.setColor(ACCENT_BLUE);
        g2.drawString("₱" + item.getPrice(), CARD_X + 80, rowY);

        // Buy button
        int btnW = 110, btnH = 32;
        btnBuyRect = new Rectangle(CARD_X + CARD_W - 24 - btnW, rowY - 20, btnW, btnH);
        boolean hov = hoveredBtn == 2;
        g2.setColor(hov ? BG_BUY_HOVER : BG_BUY);
        g2.fillRoundRect(btnBuyRect.x, btnBuyRect.y, btnBuyRect.width, btnBuyRect.height, 8, 8);
        g2.setFont(loadFont(14f, Font.BOLD));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String buyText = "BUY";
        g2.drawString(buyText,
            btnBuyRect.x + (btnBuyRect.width  - fm.stringWidth(buyText)) / 2,
            btnBuyRect.y + (btnBuyRect.height + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void drawFunds(Graphics2D g2) {
        g2.setFont(loadFont(13f, Font.BOLD));
        g2.setColor(ACCENT_BLUE);
        String t = "Your funds: ₱" + playerFunds;
        FontMetrics fm = g2.getFontMetrics();
        // Moved funds display higher up to avoid overlapping with exit button
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + CARD_H - 82);
    }

    private void drawToast(Graphics2D g2) {
        if (toastAlpha <= 0 || toastText.isEmpty()) return;
        g2.setFont(loadFont(14f, Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(toastText) + 40;
        int th = 36;
        int tx = CARD_X + (CARD_W - tw) / 2;
        int ty = CARD_Y - 50;

        g2.setColor(new Color(30, 36, 50, toastAlpha));
        g2.fillRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(), ACCENT_BLUE.getBlue(), toastAlpha));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(255, 255, 255, toastAlpha));
        g2.drawString(toastText,
            tx + (tw - fm.stringWidth(toastText)) / 2,
            ty + (th + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void drawExitButton(Graphics2D g2) {
        int btnW = 110, btnH = 32;
        int bx = CARD_X + (CARD_W - btnW) / 2;
        int by = CARD_Y + CARD_H - 52;
        btnExitRect = new Rectangle(bx, by, btnW, btnH);
        boolean hov = hoveredBtn == 3;
        g2.setColor(hov ? BG_EXIT_HOVER : BG_EXIT);
        g2.fillRoundRect(bx, by, btnW, btnH, 8, 8);
        g2.setFont(loadFont(13f, Font.BOLD));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String t = "Leave Shop";
        g2.drawString(t, bx + (btnW - fm.stringWidth(t)) / 2,
                         by + (btnH + fm.getAscent() - fm.getDescent()) / 2);
    }

    // ── Font ──────────────────────────────────────────────────────────────────

    private Font loadFont(float size, int style) {
        try {
            java.io.InputStream s = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (s != null) return Font.createFont(Font.TRUETYPE_FONT, s).deriveFont(style, size);
        } catch (Exception ignored) {}
        return new Font("Arial", style, (int) size);
    }

    // ── Size overrides ────────────────────────────────────────────────────────

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}