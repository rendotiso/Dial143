package GUI.panels;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import GUI.panels.inventoryComponents.ItemLayer;
import Entities.Item;

public class InventoryPanel extends JPanel {

    private static final int COLS         = 3;
    private static final int DEFAULT_ROWS = 2;

    private final List<Item> items = new ArrayList<>();

    private JDialog   dialog;
    private JFrame    owner;
    private JPanel    gridPanel;
    private ItemLayer detailPanel;

    private Runnable                     onInventoryOpening;
    private Runnable                     onInventoryClosed;
    private ItemLayer.ItemEffectListener onItemEffect;

    public InventoryPanel(JFrame owner) {
        this.owner       = owner;
        this.detailPanel = new ItemLayer();
        setOpaque(false);
        buildUI();
    }

    public void onInventoryOpening(Runnable callback)           { this.onInventoryOpening = callback; }
    public void onInventoryClosed(Runnable callback)            { this.onInventoryClosed  = callback; }
    public void setOnItemEffect(ItemLayer.ItemEffectListener l) { this.onItemEffect       = l; }

    // ── Item management ───────────────────────────────────────────────────────

    public void setItems(List<Item> newItems) {
        this.items.clear();
        for (Item item : newItems) {
            if (item.getQuantity() > 0) this.items.add(item);
        }
        refreshGrid();
    }
    
    public void addItem(Item item) {
        for (Item existing : items) {
            if (existing.getName().equals(item.getName())) {
                existing.addQuantity(1);
                refreshGrid();
                return;
            }
        }
        Item copy = new Item(
            item.getName(),
            item.getDescription(),
            item.getIconPath(),
            1,
            item.getPrice(),
            item.getEffectType(),
            item.getEffectValue()
        );
        items.add(copy);
        refreshGrid();
    }

    public List<Item> getItems()   { return items; }
    public boolean    isEmpty()    { return items.isEmpty(); }
    public int getTotalItemCount() { return items.stream().mapToInt(Item::getQuantity).sum(); }

    public void clear() {
        items.clear();
        refreshGrid();
    }

    // ── Popup ─────────────────────────────────────────────────────────────────

    public void showAsPopup() {
        if (onInventoryOpening != null) onInventoryOpening.run();

        dialog = new JDialog(owner, "Inventory", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setContentPane(this);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setAlwaysOnTop(true);
        dialog.setFocusable(true);

        dialog.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) hidePopup();
            }
        });

        dialog.setVisible(true);

        if (onInventoryClosed != null) onInventoryClosed.run();
    }

    public void hidePopup() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dispose();
            dialog = null;
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new BorderLayout(0, 16)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        wrapper.setOpaque(true);
        wrapper.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        JLabel title = new JLabel("INVENTORY", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(loadFont(22f, Font.BOLD));
        wrapper.add(title, BorderLayout.NORTH);

        // Start with the default 3×2 layout; refreshGrid will expand as needed.
        gridPanel = new JPanel(new GridLayout(DEFAULT_ROWS, COLS, 14, 14));
        gridPanel.setOpaque(false);
        refreshGrid();
        wrapper.add(gridPanel, BorderLayout.CENTER);

        JButton btnExit = makeButton("EXIT", new Color(60, 65, 80), new Color(100, 105, 120));
        btnExit.setPreferredSize(new Dimension(120, 36));
        btnExit.addActionListener(e -> hidePopup());

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        south.add(btnExit, BorderLayout.CENTER);
        wrapper.add(south, BorderLayout.SOUTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private void refreshGrid() {
        if (gridPanel == null) return;

        int itemCount  = items.size();
        // Always show at least DEFAULT_ROWS rows so the grid is 3×2 when empty.
        int rows       = Math.max(DEFAULT_ROWS, (int) Math.ceil((double) itemCount / COLS));
        int totalSlots = rows * COLS;

        gridPanel.setLayout(new GridLayout(rows, COLS, 14, 14));
        gridPanel.removeAll();
        for (int i = 0; i < totalSlots; i++) {
            gridPanel.add(i < itemCount ? makeItemSlot(items.get(i)) : makeEmptySlot());
        }
        gridPanel.revalidate();
        gridPanel.repaint();

        // Re-pack the dialog if it is currently open so it resizes when rows grow.
        if (dialog != null && dialog.isVisible()) {
            dialog.pack();
            dialog.setLocationRelativeTo(owner);
        }
    }

    private JPanel makeItemSlot(Item item) {
        JPanel slot = new JPanel(new BorderLayout(0, 2)) {
            private boolean hovered = false;
            {
                setOpaque(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                    public void mouseClicked(MouseEvent e) { openDetail(item); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                g.setColor(hovered ? new Color(220, 230, 245) : new Color(240, 243, 250));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        slot.setBorder(BorderFactory.createEmptyBorder(8, 8, 6, 8));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                if (getIcon() == null) {
                    g.setColor(new Color(200, 210, 225));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(new Color(120, 130, 150));
                    g.setFont(new Font("Arial", Font.PLAIN, 9));
                    FontMetrics fm = g.getFontMetrics();
                    String t = "ICON";
                    g.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                                    getHeight() / 2 + fm.getAscent() / 2 - 2);
                } else {
                    super.paintComponent(g);
                }
            }
        };
        iconLabel.setPreferredSize(new Dimension(256, 256));
        iconLabel.setIcon(item.getIcon(80, 80));
        iconLabel.setOpaque(false);

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.DARK_GRAY);
        nameLabel.setFont(loadFont(14f, Font.BOLD));

        JLabel qtyLabel = new JLabel("x" + item.getQuantity(), SwingConstants.CENTER);
        qtyLabel.setForeground(item.hasStock()
            ? new Color(30, 140, 60) : new Color(180, 40, 40));
        qtyLabel.setFont(loadFont(14f, Font.BOLD));

        JPanel south = new JPanel(new GridLayout(2, 1, 0, 0));
        south.setOpaque(false);
        south.add(nameLabel);
        south.add(qtyLabel);
        south.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0));

        slot.add(iconLabel, BorderLayout.CENTER);
        slot.add(south,     BorderLayout.SOUTH);
        return slot;
    }

    private JPanel makeEmptySlot() {
        JPanel slot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(230, 233, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        slot.setOpaque(true);
        slot.setPreferredSize(new Dimension(130, 120));
        return slot;
    }

        private void openDetail(Item item) {
            detailPanel.setItem(item);
            detailPanel.onItemUsed(this::refreshGrid);

            detailPanel.setOnEffect((effectType, effectValue) -> {
                if (onItemEffect != null) {
                    onItemEffect.onEffect(effectType, effectValue);
                }
            });

            if (dialog != null && dialog.isVisible()) {
                detailPanel.showAsPopup(dialog);
            } else {
                detailPanel.showAsPopup(owner);
            }

            refreshGrid();
        }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JButton makeButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text) {
            private Color cur = bg;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { cur = hover; repaint(); }
                    public void mouseExited (MouseEvent e) { cur = bg;    repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cur);
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
        btn.setFont(loadFont(15f, Font.BOLD));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private Font loadFont(float size, int style) {
        try {
            java.io.InputStream s = getClass().getResourceAsStream(
                "/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (s != null) return Font.createFont(Font.TRUETYPE_FONT, s).deriveFont(style, size);
        } catch (Exception ignored) {}
        return new Font("Arial", style, (int) size);
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(540, 500); }
    @Override public Dimension getMaximumSize()   { return new Dimension(540, 500); }
    @Override public Dimension getPreferredSize() { return new Dimension(540, 500); }
}