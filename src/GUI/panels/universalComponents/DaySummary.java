/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.panels.universalComponents;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

/**
 * DaySummaryPanel — modal popup shown at end of each day.
 * Displays Day #, PP earned, LP earned, Salary earned.
 * Has a Continue button that fires a callback.
 *
 * Usage:
 *   daySummary.show(currentDay, pp, lp, salary, () -> showScreen("shop"));
 */
public class DaySummary extends JPanel {

    private JDialog  dialog;
    private JFrame   owner;

    // Labels updated each time show() is called
    private JLabel lblDay;
    private JLabel lblPP;
    private JLabel lblLP;
    private JLabel lblSalary;
    private JLabel lblCallsLabel;
    private JLabel lblCalls;
    private JButton btnContinue;

    private Font pixelFont;

    public DaySummary(JFrame owner) {
        this.owner = owner;
        loadFont();
        setPreferredSize(new Dimension(480, 420));
        setOpaque(false);
        buildUI();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * @param day      current day number
     * @param pp       total PP at end of day
     * @param lp       total LP at end of day
     * @param salary   total salary at end of day
     * @param calls    number of calls completed today
     * @param onContinue  callback fired when player clicks Continue
     */
    public void show(int day, int pp, int lp, int salary, int calls, Runnable onContinue) {
        lblDay.setText("Day " + day + " Complete");
        lblPP.setText(String.valueOf(pp));
        lblLP.setText(String.valueOf(lp));
        lblSalary.setText("₱" + salary);
        lblCalls.setText(String.valueOf(calls));

        // Wire continue button fresh each time
        for (ActionListener al : btnContinue.getActionListeners()) {
            btnContinue.removeActionListener(al);
        }
        btnContinue.addActionListener(e -> {
            hidePopup();
            if (onContinue != null) onContinue.run();
        });

        showPopup();
    }

    // ── Dialog management ─────────────────────────────────────────────────────

    private void showPopup() {
        dialog = new JDialog(owner, "Day Summary", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setContentPane(this);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true); // blocks until hidePopup()
    }

    private void hidePopup() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dispose();
            dialog = null;
        }
    }

    // ── UI Construction ───────────────────────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background
                g2.setColor(new Color(20, 24, 34));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Gold border
                g2.setColor(new Color(180, 140, 60));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 24, 36));

        // ── Day title ──
        lblDay = new JLabel("Day 1 Complete", SwingConstants.CENTER);
        lblDay.setForeground(new Color(220, 185, 90));
        lblDay.setFont(font(24f, Font.BOLD));
        card.add(lblDay, BorderLayout.NORTH);

        // ── Divider ──
        JPanel dividerPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(180, 140, 60, 80));
                g.drawLine(0, 8, getWidth(), 8);
            }
        };
        dividerPanel.setOpaque(false);
        dividerPanel.setPreferredSize(new Dimension(0, 20));

        // ── Stats grid ──
        JPanel stats = new JPanel(new GridLayout(4, 2, 0, 16));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Calls completed
        stats.add(makeStatLabel("Calls Completed:", new Color(160, 175, 210)));
        lblCalls = makeValueLabel("0", new Color(210, 220, 245));
        stats.add(lblCalls);

        // Performance Points
        stats.add(makeStatLabel("Performance Points:", new Color(160, 175, 210)));
        lblPP = makeValueLabel("0", new Color(100, 210, 140));
        stats.add(lblPP);

        // Love Points
        stats.add(makeStatLabel("Love Points:", new Color(160, 175, 210)));
        lblLP = makeValueLabel("0", new Color(220, 130, 180));
        stats.add(lblLP);

        // Salary
        stats.add(makeStatLabel("Salary Earned:", new Color(160, 175, 210)));
        lblSalary = makeValueLabel("₱0", new Color(220, 185, 90));
        stats.add(lblSalary);

        JPanel centre = new JPanel(new BorderLayout());
        centre.setOpaque(false);
        centre.add(dividerPanel, BorderLayout.NORTH);
        centre.add(stats,        BorderLayout.CENTER);
        card.add(centre, BorderLayout.CENTER);

        // ── Continue button ──
        btnContinue = new JButton("CONTINUE") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? new Color(75, 155, 105) : new Color(55, 120, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(255, 255, 255, 210));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnContinue.setFont(font(15f, Font.BOLD));
        btnContinue.setPreferredSize(new Dimension(160, 44));
        btnContinue.setBorderPainted(false);
        btnContinue.setContentAreaFilled(false);
        btnContinue.setFocusPainted(false);
        btnContinue.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.setOpaque(false);
        south.add(btnContinue);
        card.add(south, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JLabel makeStatLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setForeground(color);
        lbl.setFont(font(14f, Font.PLAIN));
        return lbl;
    }

    private JLabel makeValueLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setForeground(color);
        lbl.setFont(font(16f, Font.BOLD));
        return lbl;
    }

    private Font font(float size, int style) {
        if (pixelFont != null) return pixelFont.deriveFont(style, size);
        return new Font("Arial", style, (int) size);
    }

    private void loadFont() {
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            pixelFont = (s != null) ? Font.createFont(Font.TRUETYPE_FONT, s) : null;
        } catch (Exception e) {
            pixelFont = null;
        }
    }
}