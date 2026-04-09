package GUI.panels.shiftComponents;

import java.awt.*;

public class CallCreation {

    public static final int BOX_W = 900;
    public static final int BOX_X = (1280 - BOX_W) / 2;
    public static final int BOX_Y = 90;
    public static final int BOX_H = 450;
    public static final int ARC = 20;
    public static final int PAD = 45;

    public static final int TIMER_Y = BOX_Y + BOX_H + 25;
    public static final int TIMER_H = 12;

    public static final int CHOICE_Y = TIMER_Y + TIMER_H + 35;
    public static final int CHOICE_H = 55;

    public static final Color BOX_BG = new Color(255, 255, 255, 250);
    public static final Color BOX_BORDER = new Color(180, 185, 205);
    public static final Color TITLE_COLOR = new Color(35, 45, 75);
    public static final Color CALLER_COLOR = new Color(30, 85, 165);
    public static final Color TEXT_COLOR = new Color(40, 45, 60);
    public static final Color DIVIDER_COLOR = new Color(200, 205, 225);

    public static final Color TIMER_BG = new Color(225, 230, 240);
    public static final Color TIMER_FILL = new Color(65, 135, 215);
    public static final Color TIMER_LOW = new Color(215, 65, 65);

    public static final Color CHOICE_NORMAL = new Color(245, 248, 253);
    public static final Color CHOICE_HOVER = new Color(210, 222, 245);
    public static final Color CHOICE_CHOSEN = new Color(190, 208, 235);
    public static final Color CHOICE_BORDER = new Color(175, 182, 200);

    public static final Color PP_COLOR = new Color(45, 165, 85);
    public static final Color SALARY_COLOR = new Color(195, 125, 0);

    public static final Color SKIP_NORMAL = new Color(175, 65, 65);
    public static final Color SKIP_HOVER = new Color(205, 85, 85);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font CALLER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font DESC_FONT = new Font("Segoe UI", Font.PLAIN,  18);
    public static final Font CHOICE_FONT = new Font("Segoe UI", Font.PLAIN,   18);
    public static final Font REWARD_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    public static final Font RESPONSE_FONT = new Font("Segoe UI", Font.PLAIN,  18);

    public static void drawBox(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(BOX_X + 5, BOX_Y + 5, BOX_W, BOX_H, ARC, ARC);

        g2.setColor(BOX_BG);
        g2.fillRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);

        g2.setColor(BOX_BORDER);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);
    }

    public static int drawTitle(Graphics2D g2, int currentCall, int totalCalls) {
        int y = BOX_Y + PAD;
        g2.setFont(TITLE_FONT);
        g2.setColor(TITLE_COLOR);
        String title = "CALL " + currentCall + " OF " + totalCalls;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, BOX_X + (BOX_W - fm.stringWidth(title)) / 2, y);
        return y;
    }

    public static int drawDivider(Graphics2D g2, int y) {
        g2.setColor(DIVIDER_COLOR);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(BOX_X + PAD, y + 20, BOX_X + BOX_W - PAD, y + 20);
        return y + 60;
    }

    public static int drawDescription(Graphics2D g2, String callerName, String displayedDesc, int y) {
        g2.setFont(CALLER_FONT);
        g2.setColor(CALLER_COLOR);
        FontMetrics fm = g2.getFontMetrics();
        int callerNameWidth = fm.stringWidth(callerName);
        int callerX = BOX_X + (BOX_W - callerNameWidth) / 2;
        g2.drawString(callerName, callerX, y);
        y += 40;

        g2.setFont(DESC_FONT);
        g2.setColor(TEXT_COLOR);
        String text = displayedDesc.replace(callerName + ":\n", "");
        return drawWrappedText(g2, text, BOX_X + PAD, y, BOX_W - PAD * 2, 2);
    }
    
    public static Rectangle drawSkipButton(Graphics2D g2, boolean hovered) {
        int btnW = 130, btnH = 36;
        int bx = BOX_X + BOX_W - btnW - 20;
        int by = BOX_Y + BOX_H - btnH - 16;

        g2.setColor(hovered ? SKIP_HOVER : SKIP_NORMAL);
        g2.fillRoundRect(bx, by, btnW, btnH, 10, 10);

        g2.setFont(loadFont(14f, Font.BOLD));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String t = "SKIP ALL";
        g2.drawString(t,
            bx + (btnW - fm.stringWidth(t)) / 2,
            by + (btnH + fm.getAscent() - fm.getDescent()) / 2);

        return new Rectangle(bx, by, btnW, btnH);
    }

    public static int drawResponse(Graphics2D g2, String callerName,
                                    String displayedResponse, int y) {
        y += 25;

        for (String line : displayedResponse.split("\n")) {
            if (line.isBlank()) {
                y += 12;
                continue;
            }

            if (line.startsWith("You:")) {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(new Color(35, 135, 65));
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 10);

            } else if (line.startsWith(callerName + ":")) {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(CALLER_COLOR);
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 10);

            } else if (line.contains("+Performance Points") || line.contains("+Salary")) {
                String[] parts = line.split("   ");
                int rx = BOX_X + PAD;
                g2.setFont(REWARD_FONT);
                for (String part : parts) {
                    part = part.trim();
                    if (part.isEmpty()) continue;
                    if (part.contains("+Performance Points")) {
                        g2.setColor(PP_COLOR);
                    } else if (part.contains("+Salary")) {
                        g2.setColor(SALARY_COLOR);
                    } else {
                        g2.setColor(TEXT_COLOR);
                    }
                    g2.drawString(part, rx, y + g2.getFontMetrics().getAscent());
                    rx += g2.getFontMetrics().stringWidth(part) + 30;
                }
                y += g2.getFontMetrics().getHeight() + 12;
                g2.setFont(RESPONSE_FONT);

            } else {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(TEXT_COLOR);
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 10);
            }
        }
        return y;
    }

    public static void drawTimerBar(Graphics2D g2, int timerLeft, int timerMax) {
        int barX = BOX_X;
        int barW = BOX_W;

        g2.setColor(TIMER_BG);
        g2.fillRoundRect(barX, TIMER_Y, barW, TIMER_H, TIMER_H, TIMER_H);

        float ratio = timerMax > 0 ? (float) timerLeft / timerMax : 0f;
        Color fill = ratio < 0.3f ? TIMER_LOW : TIMER_FILL;
        g2.setColor(fill);
        int fillW = (int) (barW * ratio);
        if (fillW > 0) g2.fillRoundRect(barX, TIMER_Y, fillW, TIMER_H, TIMER_H, TIMER_H);
    }

    public static void drawChoices(Graphics2D g2, String[] labels, int chosenIndex,
                                    int hoveredChoice, Rectangle[] choiceRects) {
        if (labels == null || labels.length == 0) return;
        int count = Math.min(labels.length, 3);
        int gap = 20;
        int btnW = (BOX_W - gap * (count - 1)) / count;
        int startX = BOX_X;

        for (int i = 0; i < count; i++) {
            int bx = startX + i * (btnW + gap);
            choiceRects[i] = new Rectangle(bx, CHOICE_Y, btnW, CHOICE_H);

            boolean isChosen = chosenIndex == i;
            boolean isHovered = hoveredChoice == i && chosenIndex == -1;

            g2.setColor(isChosen ? CHOICE_CHOSEN : isHovered ? CHOICE_HOVER : CHOICE_NORMAL);
            g2.fillRoundRect(bx, CHOICE_Y, btnW, CHOICE_H, 12, 12);
            g2.setColor(CHOICE_BORDER);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(bx, CHOICE_Y, btnW, CHOICE_H, 12, 12);

            g2.setFont(CHOICE_FONT);
            g2.setColor(isChosen ? new Color(55, 65, 95) : TEXT_COLOR);
            FontMetrics fm = g2.getFontMetrics();
            String text = labels[i];
            int textW = fm.stringWidth(text);
            if (textW > btnW - 30) {
                while (textW > btnW - 30 && text.length() > 3) {
                    text = text.substring(0, text.length() - 4) + "...";
                    textW = fm.stringWidth(text);
                }
            }
            g2.drawString(text,
                bx + (btnW - textW) / 2,
                CHOICE_Y + (CHOICE_H + fm.getAscent() - fm.getDescent()) / 2);
        }
    }

    public static int drawWrappedText(Graphics2D g2, String text, int x, int y,
                                       int maxWidth, int lineSpacing) {
        if (text == null || text.isEmpty()) return y;
        FontMetrics fm = g2.getFontMetrics();
        int lineH = fm.getHeight() + lineSpacing;
        StringBuilder line = new StringBuilder();
        String[] words = text.split(" ");

        for (String word : words) {
            String test = line.isEmpty() ? word : line + " " + word;
            if (fm.stringWidth(test) > maxWidth && !line.isEmpty()) {
                g2.drawString(line.toString(), x, y + fm.getAscent());
                y += lineH;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (!line.isEmpty()) {
            g2.drawString(line.toString(), x, y + fm.getAscent());
            y += lineH;
        }
        return y;
    }

    // Helper method to load fonts safely with fallback
    private static Font loadFont(float size, int style) {
        try {
            return new Font("Segoe UI", style, (int) size);
        } catch (Exception e) {
            return new Font("Arial", style, (int) size);
        }
    }
}