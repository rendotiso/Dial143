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
        setOpaque(false);
        buildUI();
    }

    public void setItem(Item item)                        { this.item = item; refresh(); }
    public void onItemUsed(Runnable callback)             { this.onItemUsed = callback; }
    public void setOnEffect(ItemEffectListener listener)  { this.onEffect   = listener; }

    public void showAsPopup(JFrame owner) {
        dialog = new JDialog(owner, "Use Item?", Dialog.ModalityType.APPLICATION_MODAL);
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

        // ── White card wrapper ────────────────────────────────────────────────
        JPanel wrapper = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(210, 215, 230));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 28));

        // ── "Use Item?" ───────────────────────────────────────────────────────
        JLabel prompt = new JLabel("Use Item?", SwingConstants.CENTER);
        prompt.setForeground(new Color(30, 40, 80));
        prompt.setFont(loadFont(16f, Font.BOLD));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(prompt);
        wrapper.add(Box.createVerticalStrut(14));

        // ── Divider ───────────────────────────────────────────────────────────
        wrapper.add(makeDivider());
        wrapper.add(Box.createVerticalStrut(14));

        // ── Icon ──────────────────────────────────────────────────────────────
        lblIcon = new JLabel("", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                if (getIcon() == null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(240, 243, 250));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(180, 190, 210));
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    String t = "NO IMG";
                    g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                                     getHeight() / 2 + fm.getAscent() / 2);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        lblIcon.setPreferredSize(new Dimension(90, 90));
        lblIcon.setMinimumSize(new Dimension(90, 90));
        lblIcon.setMaximumSize(new Dimension(90, 90));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(lblIcon);
        wrapper.add(Box.createVerticalStrut(12));

        // ── Item name ─────────────────────────────────────────────────────────
        lblName = new JLabel("", SwingConstants.CENTER);
        lblName.setForeground(new Color(30, 40, 80));
        lblName.setFont(loadFont(15f, Font.BOLD));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(lblName);
        wrapper.add(Box.createVerticalStrut(8));

        // ── Divider ───────────────────────────────────────────────────────────
        wrapper.add(makeDivider());
        wrapper.add(Box.createVerticalStrut(10));

        // ── Description ───────────────────────────────────────────────────────
        lblDescription = new JLabel("", SwingConstants.CENTER);
        lblDescription.setForeground(new Color(60, 70, 100));
        lblDescription.setFont(loadFont(12f, Font.PLAIN));
        lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(lblDescription);
        wrapper.add(Box.createVerticalStrut(6));

        // ── Effect ────────────────────────────────────────────────────────────
        lblEffect = new JLabel("", SwingConstants.CENTER);
        lblEffect.setForeground(new Color(45, 110, 70));
        lblEffect.setFont(loadFont(12f, Font.BOLD));
        lblEffect.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(lblEffect);
        wrapper.add(Box.createVerticalStrut(5));

        // ── Quantity ──────────────────────────────────────────────────────────
        lblQuantity = new JLabel("", SwingConstants.CENTER);
        lblQuantity.setForeground(new Color(130, 140, 160));
        lblQuantity.setFont(loadFont(11f, Font.PLAIN));
        lblQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(lblQuantity);
        wrapper.add(Box.createVerticalStrut(14));

        // ── Divider ───────────────────────────────────────────────────────────
        wrapper.add(makeDivider());
        wrapper.add(Box.createVerticalStrut(14));

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnUse  = makeButton("Use",  new Color(55, 120, 80),  new Color(75, 155, 100));
        btnBack = makeButton("Back", new Color(70, 35, 35),   new Color(100, 55, 55));

        btnUse.addActionListener(e  -> handleUse());
        btnBack.addActionListener(e -> hidePopup());

        btnRow.add(btnUse);
        btnRow.add(btnBack);
        wrapper.add(btnRow);

        add(wrapper, BorderLayout.CENTER);
    }

    /** Thin horizontal rule matching the card's inner width */
    private JPanel makeDivider() {
        JPanel line = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(220, 224, 235));
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(1, 1));
        return line;
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
        hidePopup();
    }

    private void refresh() {
        if (item == null) return;

        lblName.setText(item.getName());

        // HTML-centered description, constrained width so it wraps neatly
        String desc = item.getDescription().replace("\n", "<br>");
        lblDescription.setText(
            "<html><div style='text-align:center;width:220px;'>" + desc + "</div></html>");

        lblEffect.setText(effectLabel(item.getEffectType(), item.getEffectValue()));

        lblQuantity.setText("Quantity: " + item.getQuantity());
        lblQuantity.setForeground(item.getQuantity() > 0
            ? new Color(130, 140, 160) : new Color(200, 60, 60));

        btnUse.setEnabled(item.hasStock());

        ImageIcon icon = item.getIcon(90, 90);
        lblIcon.setIcon(icon);
        lblIcon.setText("");

        revalidate();
        repaint();
    }

    private String effectLabel(Item.EffectType type, int value) {
        return switch (type) {
            case PP_MULTIPLIER       -> "+" + value + "% PP for one day";
            case HINT_PER_CALL       -> "Reveals best option for one call";
            case TIMER_BOOST         -> "+" + value + "s added to call timer";
            case LP_FLAT             -> "+" + value + " LP instantly";
            case LP_MULTIPLIER_DAILY -> "+" + value + "% LP per day (passive)";
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
                g2.setColor(isEnabled() ? cur : new Color(200, 205, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(110, 38));
        btn.setMinimumSize(new Dimension(80, 38));
        btn.setFont(loadFont(13f, Font.BOLD));
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