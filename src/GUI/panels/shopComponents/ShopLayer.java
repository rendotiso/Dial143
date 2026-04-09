package GUI.panels.shopComponents;

import Entities.Item;
import GUI.panels.universalComponents.ImageButtonCreation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopLayer extends JPanel {

    private final List<Item> shopItems = new ArrayList<>();
    private int     currentIndex = 0;
    private int     playerFunds  = 0;
    private boolean isLoaded     = false;
    private Font    mulishFont;

    @FunctionalInterface public interface PurchaseListener { void onBought(Item item, int cost); }
    private PurchaseListener onPurchase;
    private Runnable         onExit;

    public void setOnPurchase(PurchaseListener listener) { this.onPurchase = listener; }
    public void setOnExit(Runnable callback)             { this.onExit     = callback; }
    public List<Item> getShopItems()                     { return shopItems; }

    private static final int CARD_W = 480;
    private static final int CARD_H = 560;
    private static final int CARD_X = (1280 - CARD_W) / 2;
    private static final int CARD_Y = (720  - CARD_H) / 2;

    private static final int ICON_SIZE = 128;
    private static final int ICON_TOP  = CARD_Y + 160;
    private static final int ICON_LEFT = CARD_X + (CARD_W - ICON_SIZE) / 2;

    private Rectangle btnBuyRect;
    private Rectangle btnExitRect;
    private int hoveredBtn = -1;

    private ImageButtonCreation btnBackImage;
    private ImageButtonCreation btnNextImage;

    private String toastText  = "";
    private int    toastAlpha = 0;
    private Timer  toastTimer;

    private static final Color BG_CARD       = Color.WHITE;
    private static final Color BORDER_COLOR  = new Color(210, 215, 230);
    private static final Color TITLE_COLOR   = new Color(30,  40,  80);
    private static final Color TEXT_PRIMARY  = new Color(40,  45,  65);  
    private static final Color TEXT_MUTED    = new Color(110, 120, 150);
    private static final Color ACCENT_BLUE   = new Color(60,  110, 220);
    private static final Color BG_NAV        = new Color(245, 247, 252);
    private static final Color BG_BUY        = new Color(55,  120, 80);
    private static final Color BG_BUY_HOVER  = new Color(75,  160, 105);
    private static final Color BG_EXIT       = new Color(70,  35,  35);
    private static final Color BG_EXIT_HOVER = new Color(100, 55,  55);
    private static final Color SHADOW_COLOR  = new Color(0, 0, 0, 35);

    public ShopLayer() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));

        loadCustomFonts();
        setupImageButtons();
        setupEventHandlers();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateHover(e.getPoint()); }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleClick(e.getPoint()); }
        });
    }

    // ── Font loading ──────────────────────────────────────────────────────────

    private void loadCustomFonts() {
        try {
            InputStream stream = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (stream == null) { mulishFont = new Font("SansSerif", Font.BOLD, 14); return; }
            Font base = Font.createFont(Font.TRUETYPE_FONT, stream);
            java.util.Map<java.awt.font.TextAttribute, Object> attrs = new java.util.HashMap<>();
            attrs.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            mulishFont = base.deriveFont(attrs);
        } catch (Exception e) {
            mulishFont = new Font("SansSerif", Font.BOLD, 14);
        }
    }

    private Font loadFont(float size, int style) {
        if (mulishFont != null) return mulishFont.deriveFont(style, size);
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (s != null) return Font.createFont(Font.TRUETYPE_FONT, s).deriveFont(style, size);
        } catch (Exception ignored) {}
        return new Font("Arial", style, (int) size);
    }

    // ── Nav buttons ───────────────────────────────────────────────────────────

    private void setupImageButtons() {
        int btnSize = 40;
        int btnY    = ICON_TOP + (ICON_SIZE - btnSize) / 2;

        btnBackImage = new ImageButtonCreation("");
        btnBackImage.setBounds(ICON_LEFT - btnSize - 80, btnY, btnSize, btnSize);
        add(btnBackImage);

        btnNextImage = new ImageButtonCreation("");
        btnNextImage.setBounds(ICON_LEFT + ICON_SIZE + 80, btnY, btnSize, btnSize);
        add(btnNextImage);
    }

    private void updateNavButtonPositions() {
        if (btnBackImage == null || btnNextImage == null) return;
        int btnSize = 40;
        int btnY    = ICON_TOP + (ICON_SIZE - btnSize) / 2;
        btnBackImage.setBounds(ICON_LEFT - btnSize - 80, btnY, btnSize, btnSize);
        btnNextImage.setBounds(ICON_LEFT + ICON_SIZE + 80, btnY, btnSize, btnSize);
    }

    private void setupEventHandlers() {
        if (btnBackImage != null) btnBackImage.addActionListener(e -> navigate(-1));
        if (btnNextImage != null) btnNextImage.addActionListener(e -> navigate(1));
    }

    public void setNavButtonImages(String backPath, String nextPath) {
        if (backPath != null && btnBackImage != null) btnBackImage.setImage(backPath);
        if (nextPath != null && btnNextImage != null) btnNextImage.setImage(nextPath);
    }
    public void setBackButtonImage(String path) { if (btnBackImage != null) btnBackImage.setImage(path); }
    public void setNextButtonImage(String path) { if (btnNextImage != null) btnNextImage.setImage(path); }

    // ── Public API ────────────────────────────────────────────────────────────

    public void load() {
        if (!isLoaded) { initItems(); isLoaded = true; }
        currentIndex = 0;
        repaint();
    }

    public void setPlayerFunds(int funds) { this.playerFunds = funds; repaint(); }
    public int  getPlayerFunds()          { return playerFunds; }

    // ── Items ─────────────────────────────────────────────────────────────────

    private void initItems() {
        shopItems.clear();
        shopItems.add(new Item("Coffee",
            "A strong brew to sharpen focus.\n+10% PP earned for one full day.",
            "/GUI/resources/icons/coffee.png",
            0, 80, Item.ItemType.CONSUMABLE, Item.EffectType.PP_MULTIPLIER, 10));
        shopItems.add(new Item("Sticky Note",
            "A handy reminder on your desk.\nHighlights the best answer for one call.",
            "/GUI/resources/icons/stickynotes.png",
            0, 120, Item.ItemType.CONSUMABLE, Item.EffectType.HINT_PER_CALL, 1));
        shopItems.add(new Item("Clock",
            "A trusty desk clock to buy you time.\n+10 seconds added to one call timer.",
            "/GUI/resources/icons/clock.png",
            0, 150, Item.ItemType.CONSUMABLE, Item.EffectType.TIMER_BOOST, 10));
        shopItems.add(new Item("Chocolate",
            "A sweet treat for someone special.\nGrants +5 Love Points instantly.",
            "/GUI/resources/icons/chocolit.png",
            0, 200, Item.ItemType.CONSUMABLE, Item.EffectType.LP_FLAT, 5));
        shopItems.add(new Item("Desk Plant",
            "A little green companion for their desk.\nPassively grants +5% LP each day.",
            "/GUI/resources/icons/plant.png",
            0, 350, Item.ItemType.SPECIAL, Item.EffectType.LP_MULTIPLIER_DAILY, 5));
        shopItems.add(new Item("Book",
            "A thoughtful read picked just for them.\nGrants +10 Love Points instantly.",
            "/GUI/resources/icons/book.png",
            0, 300, Item.ItemType.CONSUMABLE, Item.EffectType.LP_FLAT, 10));
    }

    // ── Interaction ───────────────────────────────────────────────────────────

    private void updateHover(Point p) {
        int prev = hoveredBtn;
        hoveredBtn = -1;
        if (btnBuyRect  != null && btnBuyRect.contains(p))  hoveredBtn = 2;
        else if (btnExitRect != null && btnExitRect.contains(p)) hoveredBtn = 3;
        if (hoveredBtn != prev) repaint();
    }

    private void handleClick(Point p) {
        if (!isLoaded) return;
        if (btnBuyRect  != null && btnBuyRect.contains(p))  { handleBuy();  return; }
        if (btnExitRect != null && btnExitRect.contains(p)) { handleExit(); return; }
    }

    private void navigate(int dir) {
        if (shopItems.isEmpty()) return;
        currentIndex = (currentIndex + dir + shopItems.size()) % shopItems.size();
        repaint();
    }
    
    private void handleBuy() {
        if (shopItems.isEmpty()) return;
        Item item = shopItems.get(currentIndex);
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

    private void handleExit() { if (onExit != null) onExit.run(); }

    private void showToast(String text) {
        this.toastText  = text;
        this.toastAlpha = 255;
        if (toastTimer != null) toastTimer.stop();
        toastTimer = new Timer(40, e -> {
            toastAlpha -= 8;
            if (toastAlpha <= 0) { toastAlpha = 0; ((Timer) e.getSource()).stop(); }
            repaint();
        });
        toastTimer.setInitialDelay(600);
        toastTimer.start();
        repaint();
    }

    // ── Paint ─────────────────────────────────────────────────────────────────

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
        updateNavButtonPositions();  
        drawDescription(g2, item);   
        drawDivider(g2, CARD_Y + 420);
        drawPriceRow(g2, item);       
        drawDivider(g2, CARD_Y + 490);
        drawExitButton(g2);         
        drawToast(g2);

        g2.dispose();
    }

    // ── Draw helpers ──────────────────────────────────────────────────────────

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
        g2.setFont(loadFont(25f, Font.BOLD));
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
        g2.setFont(loadFont(20f, Font.BOLD));
        g2.setColor(TITLE_COLOR);
        String t = item.getName();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(t, CARD_X + (CARD_W - fm.stringWidth(t)) / 2, CARD_Y + 120);
    }

    private void drawIcon(Graphics2D g2, Item item) {
        ImageIcon icon = item.getIcon(ICON_SIZE, ICON_SIZE);
        if (icon != null && icon.getImage() != null) {
            g2.drawImage(icon.getImage(), ICON_LEFT, ICON_TOP, ICON_SIZE, ICON_SIZE, null);
        } else {
            g2.setColor(BG_NAV);
            g2.fillRoundRect(ICON_LEFT, ICON_TOP, ICON_SIZE, ICON_SIZE, 12, 12);
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(ICON_LEFT + 1, ICON_TOP + 1, ICON_SIZE - 2, ICON_SIZE - 2, 12, 12);
            g2.setFont(loadFont(11f, Font.PLAIN));
            g2.setColor(TEXT_MUTED);
            FontMetrics fm = g2.getFontMetrics();
            String t = "NO IMAGE";
            g2.drawString(t, ICON_LEFT + (ICON_SIZE - fm.stringWidth(t)) / 2,
                             ICON_TOP  + ICON_SIZE / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void drawDescription(Graphics2D g2, Item item) {
        String[] lines = item.getDescription().split("\n");
        int startY = ICON_TOP + ICON_SIZE + 50;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            if (i == 0) {
                g2.setFont(loadFont(16f, Font.PLAIN));
                g2.setColor(TEXT_PRIMARY);
            } else {
                g2.setFont(loadFont(16f, Font.BOLD));
                g2.setColor(ACCENT_BLUE);
            }

            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(line, CARD_X + (CARD_W - fm.stringWidth(line)) / 2, startY);
            startY += fm.getHeight() + 8;
        }
    }

    private void drawPriceRow(Graphics2D g2, Item item) {
        int rowY = CARD_Y + 420 + 35;  
        int btnW = 90, btnH = 34;
        int btnX = CARD_X + CARD_W - 32 - btnW;
        int btnY = rowY - btnH / 2;
        g2.setFont(loadFont(18f, Font.PLAIN));
        g2.setColor(TEXT_MUTED);
        FontMetrics fmLabel = g2.getFontMetrics();
        int textBaseline = rowY + fmLabel.getAscent() / 2 - 1;
        g2.drawString("Price:", CARD_X + 32, textBaseline);
        g2.setFont(loadFont(18f, Font.BOLD));
        g2.setColor(ACCENT_BLUE);
        FontMetrics fmPrice = g2.getFontMetrics();
        g2.drawString("\u20b1" + item.getPrice(),
                CARD_X + 32 + fmLabel.stringWidth("Price:") + 10,
                rowY + fmPrice.getAscent() / 2 - 1);

        btnBuyRect = new Rectangle(btnX, btnY, btnW, btnH);
        drawBuyButton(g2, btnBuyRect, "BUY", hoveredBtn == 2, playerFunds >= item.getPrice());
    }

    private void drawBuyButton(Graphics2D g2, Rectangle r, String text, boolean hovered, boolean enabled) {
        Color bg = enabled ? (hovered ? BG_BUY_HOVER : BG_BUY) : new Color(160, 160, 160);
        g2.setColor(bg);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        g2.setFont(loadFont(14f, Font.BOLD));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
                r.x + (r.width  - fm.stringWidth(text)) / 2,
                r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void drawExitButton(Graphics2D g2) {
        int btnW    = 130, btnH = 38;
        int bx      = CARD_X + (CARD_W - btnW) / 2;
        int zoneTop = CARD_Y + 490;
        int zoneBot = CARD_Y + CARD_H - 8;
        int by      = zoneTop + (zoneBot - zoneTop - btnH) / 2;

        btnExitRect = new Rectangle(bx, by, btnW, btnH);
        g2.setColor(hoveredBtn == 3 ? BG_EXIT_HOVER : BG_EXIT);
        g2.fillRoundRect(bx, by, btnW, btnH, 8, 8);
        g2.setFont(loadFont(13f, Font.BOLD));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String t = "Leave Shop";
        g2.drawString(t,
                bx + (btnW - fm.stringWidth(t)) / 2,
                by + (btnH + fm.getAscent() - fm.getDescent()) / 2);
    }

private void drawToast(Graphics2D g2) {
    if (toastAlpha <= 0 || toastText.isEmpty()) return;
    g2.setFont(loadFont(13f, Font.BOLD));
    FontMetrics fm = g2.getFontMetrics();
    int tw = fm.stringWidth(toastText) + 32, th = 32;
    int tx = CARD_X + (CARD_W - tw) / 2;
    int ty = CARD_Y + 480 - th - 10;
    g2.setColor(new Color(220, 245, 225, Math.min(230, toastAlpha)));
    g2.fillRoundRect(tx, ty, tw, th, 10, 10);
    g2.setColor(new Color(80, 170, 100, toastAlpha));
    g2.setStroke(new BasicStroke(1.5f));
    g2.drawRoundRect(tx, ty, tw, th, 10, 10);
    g2.setColor(new Color(30, 100, 50, toastAlpha));
    g2.drawString(toastText,
            tx + (tw - fm.stringWidth(toastText)) / 2,
            ty + (th + fm.getAscent() - fm.getDescent()) / 2);
}
        
        
    // ── Size overrides ────────────────────────────────────────────────────────

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}