package GUI.panels.shopComponents;

import Entities.Item;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ShopLayer extends JPanel {

    private final List<Item> shopItems = new ArrayList<>();
    private int     currentIndex = 0;
    private int     playerFunds  = 0;
    private boolean isLoaded     = false;

    @FunctionalInterface public interface PurchaseListener { void onBought(Item item, int cost); }
    private PurchaseListener onPurchase;
    private Runnable         onExit;

    public void setOnPurchase(PurchaseListener listener) { this.onPurchase = listener; }
    public void setOnExit(Runnable callback)             { this.onExit     = callback; }
    public List<Item> getShopItems()                     { return shopItems; }

    private static final int CARD_W = 480;
    private static final int CARD_H = 520;
    private static final int CARD_X = (1280 - CARD_W) / 2;
    private static final int CARD_Y = (720  - CARD_H) / 2 + 20;

    private Rectangle btnBackRect;
    private Rectangle btnNextRect;
    private Rectangle btnBuyRect;
    private Rectangle btnExitRect;
    private int hoveredBtn = -1;

    private String toastText  = "";
    private int    toastAlpha = 0;
    private Timer  toastTimer;

    private static final Color BG_CARD       = Color.WHITE;
    private static final Color BORDER_COLOR  = new Color(210, 215, 230);
    private static final Color TITLE_COLOR   = new Color(30,  40,  80);
    private static final Color TEXT_PRIMARY  = new Color(50,  55,  75);
    private static final Color TEXT_MUTED    = new Color(110, 120, 150);
    private static final Color ACCENT_BLUE   = new Color(60,  110, 220);
    private static final Color BG_NAV        = new Color(245, 247, 252);
    private static final Color BG_NAV_HOVER  = new Color(238, 242, 250);
    private static final Color BG_BUY        = new Color(55,  120, 80);
    private static final Color BG_BUY_HOVER  = new Color(75,  160, 105);
    private static final Color BG_EXIT       = new Color(70,  35,  35);
    private static final Color BG_EXIT_HOVER = new Color(100, 55,  55);
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

    public void load() {
        if (!isLoaded) {
            initItems();
            isLoaded = true;
        }
        currentIndex = 0;
        repaint();
    }

    public void setPlayerFunds(int funds) { this.playerFunds = funds; repaint(); }
    public int  getPlayerFunds()          { return playerFunds; }

    private void initItems() {
        shopItems.clear();

        shopItems.add(new Item(
            "Coffee",
            "A strong brew to sharpen focus.\n+10% PP earned for one full day.",
            "/GUI/resources/items/coffee.png",
            0, 80, Item.ItemType.CONSUMABLE,
            Item.EffectType.PP_MULTIPLIER, 10
        ));
        shopItems.add(new Item(
            "Sticky Note",
            "A handy reminder on your desk.\nHighlights the best answer for one call.",
            "/GUI/resources/items/stickynote.png",
            0, 120, Item.ItemType.CONSUMABLE,
            Item.EffectType.HINT_PER_CALL, 1
        ));
        shopItems.add(new Item(
            "Clock",
            "A trusty desk clock to buy you time.\n+10 seconds added to one call timer.",
            "/GUI/resources/items/clock.png",
            0, 150, Item.ItemType.CONSUMABLE,
            Item.EffectType.TIMER_BOOST, 10
        ));
        shopItems.add(new Item(
            "Chocolate",
            "A sweet treat for someone special.\nGrants +5 Love Points instantly.",
            "/GUI/resources/items/chocolate.png",
            0, 200, Item.ItemType.CONSUMABLE,
            Item.EffectType.LP_FLAT, 5
        ));
        shopItems.add(new Item(
            "Desk Plant",
            "A little green companion for their desk.\nPassively grants +5% LP each day.",
            "/GUI/resources/items/deskplant.png",
            0, 350, Item.ItemType.SPECIAL,
            Item.EffectType.LP_MULTIPLIER_DAILY, 5
        ));
        shopItems.add(new Item(
            "Book",
            "A thoughtful read picked just for them.\nGrants +10 Love Points instantly.",
            "/GUI/resources/items/book.png",
            0, 300, Item.ItemType.CONSUMABLE,
            Item.EffectType.LP_FLAT, 10
        ));
    }

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
        Item item = shopItems.get(currentIndex);
        if (playerFunds < item.getPrice()) { showToast("Not enough funds!"); return; }
        playerFunds -= item.getPrice();
        item.addQuantity(1);
        if (onPurchase != null) onPurchase.onBought(item, item.getPrice());
        showToast("Bought " + item.getName() + "!");
        repaint();
    }

    private void handleExit() {
        if (onExit != null) onExit.run();
    }

    private void showToast(String text) {
        this.toastText  = text;
        this.toastAlpha = 255;
        if (toastTimer != null) toastTimer.stop();
        toastTimer = new Timer(40, e -> {
            toastAlpha -= 8;
            if (toastAlpha <= 0) { toastAlpha = 0; ((Timer) e.getSource()).stop(); }
            repaint();
        });
        toastTimer.setInitialDelay(1200);
        toastTimer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isLoaded || shopItems.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Item item = shopItems.get(currentIndex);

        drawCard(g2);
        drawTitle(g2);
        drawDivider(g2, CARD_Y + 62);
        drawItemName(g2, item);
        drawIcon(g2, item);
        drawNavButtons(g2);
        drawPageIndicator(g2);
        drawDescription(g2, item);
        drawEffectTag(g2, item);
        drawDivider(g2, CARD_Y + 400);
        drawPriceRow(g2, item);
        drawFunds(g2);
        drawToast(g2);
        drawExitButton(g2);

        g2.dispose();
    }

    private void drawCard(Graphics2D g2) {
        g2.setColor(SHADOW_COLOR);
        g2.fillRoundRect(CARD_X + 4, CARD_Y + 4, CARD_W, CARD_H, 14, 14);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(CARD_X, CARD_Y, CARD_W, CARD_H, 14, 14);
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

    private void drawItemName(Graphics2D g2, Item item) {
        g2.setFont(loadFont(18f, Font.BOLD));
        g2.setColor(TITLE_COLOR);
        String t = item.getName();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 96);
    }

    private void drawIcon(Graphics2D g2, Item item) {
        int iconSize = 120;
        int ix = CARD_X + (CARD_W - iconSize) / 2;
        int iy = CARD_Y + 108;

        ImageIcon icon = item.getIcon(iconSize, iconSize);
        if (icon != null) {
            g2.drawImage(icon.getImage(), ix, iy, iconSize, iconSize, null);
        } else {
            g2.setColor(BG_NAV);
            g2.fillRoundRect(ix, iy, iconSize, iconSize, 12, 12);
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(ix + 1, iy + 1, iconSize - 2, iconSize - 2, 12, 12);
            g2.setFont(loadFont(11f, Font.PLAIN));
            g2.setColor(TEXT_MUTED);
            FontMetrics fm = g2.getFontMetrics();
            String t = "NO IMAGE";
            g2.drawString(t, ix + (iconSize - fm.stringWidth(t)) / 2,
                             iy + iconSize / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void drawNavButtons(Graphics2D g2) {
        int iy   = CARD_Y + 108;
        int btnW = 90, btnH = 32;
        int btnY = iy + (120 - btnH) / 2;

        btnBackRect = new Rectangle(CARD_X + 24,                btnY, btnW, btnH);
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
        g2.setColor(TEXT_MUTED);
        String t = (currentIndex + 1) + " / " + shopItems.size();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 244);
    }

    private void drawDescription(Graphics2D g2, Item item) {
        g2.setFont(loadFont(13f, Font.PLAIN));
        g2.setColor(TEXT_PRIMARY);
        String[] lines = item.getDescription().split("\n");
        FontMetrics fm  = g2.getFontMetrics();
        int startY = CARD_Y + 268;
        for (String line : lines) {
            g2.drawString(line, CARD_X + (CARD_W - fm.stringWidth(line)) / 2, startY);
            startY += fm.getHeight() + 2;
        }
    }

    private void drawEffectTag(Graphics2D g2, Item item) {
        if (item.getEffectType() == Item.EffectType.NONE) return;
        String tag = effectTag(item.getEffectType(), item.getEffectValue());
        g2.setFont(loadFont(11f, Font.BOLD));
        g2.setColor(new Color(180, 140, 30));
        FontMetrics fm = g2.getFontMetrics();
        int tx = CARD_X + (CARD_W - fm.stringWidth(tag)) / 2;
        int ty = CARD_Y + 340;
        // Pill background
        int pw = fm.stringWidth(tag) + 20, ph = 22;
        g2.setColor(new Color(255, 245, 200));
        g2.fillRoundRect(tx - 10, ty - 16, pw, ph, 10, 10);
        g2.setColor(new Color(220, 180, 60));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(tx - 10, ty - 16, pw, ph, 10, 10);
        g2.setColor(new Color(140, 100, 10));
        g2.drawString(tag, tx, ty);
    }

    private String effectTag(Item.EffectType type, int value) {
        return switch (type) {
            case PP_MULTIPLIER       -> "+" + value + "% PP  •  1 day";
            case HINT_PER_CALL       -> "Reveals best option  •  1 call";
            case TIMER_BOOST         -> "+" + value + "s timer  •  1 call";
            case LP_FLAT             -> "+" + value + " LP  •  instant";
            case LP_MULTIPLIER_DAILY -> "+" + value + "% LP per day  •  passive";
            case NONE                -> "";
        };
    }

    private void drawPriceRow(Graphics2D g2, Item item) {
        int rowY = CARD_Y + 422;

        g2.setFont(loadFont(13f, Font.PLAIN));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString("Price:", CARD_X + 24, rowY);

        g2.setFont(loadFont(15f, Font.BOLD));
        g2.setColor(ACCENT_BLUE);
        g2.drawString("₱" + item.getPrice(), CARD_X + 80, rowY);

        int btnW = 110, btnH = 32;
        btnBuyRect = new Rectangle(CARD_X + CARD_W - 24 - btnW, rowY - 22, btnW, btnH);
        boolean hov = hoveredBtn == 2;
        boolean canAfford = playerFunds >= item.getPrice();
        g2.setColor(canAfford ? (hov ? BG_BUY_HOVER : BG_BUY) : new Color(160, 160, 160));
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
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + CARD_H - 82);
    }

    private void drawToast(Graphics2D g2) {
        if (toastAlpha <= 0 || toastText.isEmpty()) return;
        g2.setFont(loadFont(14f, Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(toastText) + 40, th = 36;
        int tx = CARD_X + (CARD_W - tw) / 2;
        int ty = CARD_Y - 50;
        g2.setColor(new Color(30, 36, 50, toastAlpha));
        g2.fillRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(),
                              ACCENT_BLUE.getBlue(), toastAlpha));
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
        int by = CARD_Y + CARD_H - 50;
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

    private Font loadFont(float size, int style) {
        try {
            java.io.InputStream s = getClass().getResourceAsStream(
                "/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (s != null) return Font.createFont(Font.TRUETYPE_FONT, s).deriveFont(style, size);
        } catch (Exception ignored) {}
        return new Font("Arial", style, (int) size);
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}