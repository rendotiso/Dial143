package GUI.panels.inventoryComponents;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import Entities.Item;

public class ItemLayer extends JPanel {

    private Item    item;
    private JDialog dialog;

    private JLabel  lblName;
    private JLabel  lblIcon;
    private JLabel  lblDescription;
    private JLabel  lblQuantity;
    private JLabel  lblEffect;
    private JButton btnUse;
    private JButton btnBack;

    private Runnable           onItemUsed;
    private ItemEffectListener onEffect;

    @FunctionalInterface
    public interface ItemEffectListener {
        void onEffect(Item.EffectType effectType, int effectValue);
    }

    public ItemLayer() {
        setPreferredSize(new Dimension(540, 500));
        setOpaque(false);
        buildUI();
    }

    public void setItem(Item item)                        { this.item = item; refresh(); }
    public void onItemUsed(Runnable callback)             { this.onItemUsed = callback; }
    public void setOnEffect(ItemEffectListener listener)  { this.onEffect   = listener; }

    public void showAsPopup(JFrame owner) {
        dialog = new JDialog(owner, "Item Detail", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setContentPane(this);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    public void hidePopup() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dispose();
            dialog = null;
        }
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 26, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(82, 130, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(28, 32, 24, 32));
        wrapper.setLayout(new BorderLayout(0, 12));

        // ── Title ──
        lblName = new JLabel("ITEM NAME", SwingConstants.CENTER);
        lblName.setForeground(new Color(210, 225, 255));
        lblName.setFont(loadFont(22f, Font.BOLD));
        wrapper.add(lblName, BorderLayout.NORTH);

        // ── Centre ──
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        lblIcon = new JLabel("", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                if (getIcon() == null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(40, 50, 70));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(new Color(82, 130, 200, 120));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                    g2.setColor(new Color(100, 120, 160));
                    g2.setFont(new Font("Arial", Font.PLAIN, 11));
                    FontMetrics fm = g2.getFontMetrics();
                    String t = "ITEM ICON";
                    g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                                     getHeight() / 2 + fm.getAscent() / 2);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIcon.setPreferredSize(new Dimension(110, 110));
        lblIcon.setMinimumSize(new Dimension(110, 110));
        lblIcon.setMaximumSize(new Dimension(110, 110));

        lblDescription = new JLabel("", SwingConstants.CENTER);
        lblDescription.setForeground(new Color(170, 185, 210));
        lblDescription.setFont(loadFont(13f, Font.PLAIN));
        lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblEffect = new JLabel("", SwingConstants.CENTER);
        lblEffect.setForeground(new Color(255, 210, 100));
        lblEffect.setFont(loadFont(12f, Font.BOLD));
        lblEffect.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblQuantity = new JLabel("Quantity: 0", SwingConstants.CENTER);
        lblQuantity.setForeground(new Color(120, 200, 140));
        lblQuantity.setFont(loadFont(13f, Font.BOLD));
        lblQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);

        centre.add(lblIcon);
        centre.add(Box.createVerticalStrut(14));
        centre.add(lblDescription);
        centre.add(Box.createVerticalStrut(8));
        centre.add(lblEffect);
        centre.add(Box.createVerticalStrut(10));
        centre.add(lblQuantity);

        wrapper.add(centre, BorderLayout.CENTER);

        // ── Buttons ──
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 24, 0));
        btnRow.setOpaque(false);

        btnUse  = makeButton("USE",  new Color(60, 140, 100), new Color(80, 180, 130));
        btnBack = makeButton("BACK", new Color(55, 65, 90),   new Color(75, 90, 120));

        btnUse.addActionListener(e -> handleUse());
        btnBack.addActionListener(e -> hidePopup());

        btnRow.add(btnUse);
        btnRow.add(btnBack);
        wrapper.add(btnRow, BorderLayout.SOUTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private void handleUse() {
        if (item == null) return;
        if (!item.hasStock()) {
            JOptionPane.showMessageDialog(this,
                "No more " + item.getName() + " left!",
                "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        item.use();
        if (onEffect != null) onEffect.onEffect(item.getEffectType(), item.getEffectValue());
        refresh();
        if (onItemUsed != null) onItemUsed.run();
    }

    private void refresh() {
        if (item == null) return;
        lblName.setText(item.getName().toUpperCase());
        lblDescription.setText("<html><div style='text-align:center;width:320px'>"
            + item.getDescription().replace("\n", "<br>") + "</div></html>");
        lblEffect.setText(effectLabel(item.getEffectType(), item.getEffectValue()));
        lblQuantity.setText("Quantity: " + item.getQuantity());
        lblQuantity.setForeground(item.getQuantity() > 0
            ? new Color(120, 200, 140) : new Color(200, 80, 80));
        btnUse.setEnabled(item.hasStock());
        lblIcon.setIcon(item.getIcon(100, 100));
        revalidate();
        repaint();
    }

    private String effectLabel(Item.EffectType type, int value) {
        return switch (type) {
            case PP_MULTIPLIER       -> "Effect: +" + value + "% PP for one day";
            case HINT_PER_CALL       -> "Effect: Reveals best option for one call";
            case TIMER_BOOST         -> "Effect: +" + value + "s to call timer";
            case LP_FLAT             -> "Effect: +" + value + " LP instantly";
            case LP_MULTIPLIER_DAILY -> "Effect: +" + value + "% LP per day (passive)";
            case NONE                -> "";
        };
    }

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
                g2.setColor(isEnabled() ? cur : new Color(50, 55, 65));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(0, 44));
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
}