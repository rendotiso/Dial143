package GUI.panels.shiftComponents;

import java.awt.*;

public class CallCreation {

    public static final int BOX_W = 880;
    public static final int BOX_X      = (1280 - BOX_W) / 2;
    public static final int BOX_Y = 125;
    public static final int BOX_H = 420;
    public static final int ARC   = 12;
    public static final int PAD   = 30;

    public static final int TIMER_Y = BOX_Y + BOX_H + 20;
    public static final int TIMER_H = 8;

    public static final int CHOICE_Y = TIMER_Y + TIMER_H + 18;
    public static final int CHOICE_H = 38;

    public static final Color BOX_BG        = new Color(255, 255, 255, 230);
    public static final Color BOX_BORDER    = new Color(210, 210, 210);
    public static final Color TITLE_COLOR   = new Color(60, 60, 60);
    public static final Color CALLER_COLOR  = new Color(40, 80, 160);
    public static final Color TEXT_COLOR    = new Color(30, 30, 30);
    public static final Color DIVIDER_COLOR = new Color(210, 210, 210);

    public static final Color TIMER_BG   = new Color(220, 220, 220);
    public static final Color TIMER_FILL = new Color(80, 140, 220);
    public static final Color TIMER_LOW  = new Color(200, 60, 60);

    public static final Color CHOICE_NORMAL = new Color(238, 238, 238);
    public static final Color CHOICE_HOVER  = new Color(200, 215, 240);
    public static final Color CHOICE_CHOSEN = new Color(180, 195, 225);
    public static final Color CHOICE_BORDER = new Color(160, 160, 160);

    public static final Color PP_COLOR     = new Color(40, 160, 80);
    public static final Color SALARY_COLOR = new Color(180, 120, 0);

    public static final Font TITLE_FONT    = new Font("Arial", Font.BOLD,  15);
    public static final Font CALLER_FONT   = new Font("Arial", Font.BOLD,  15);
    public static final Font DESC_FONT     = new Font("Arial", Font.PLAIN, 15);
    public static final Font CHOICE_FONT   = new Font("Arial", Font.PLAIN, 14);
    public static final Font REWARD_FONT   = new Font("Arial", Font.BOLD,  14);
    public static final Font RESPONSE_FONT = new Font("Arial", Font.PLAIN, 14);


    public static void drawBox(Graphics2D g2) {
        
        g2.setColor(new Color(0, 0, 0, 18));
        g2.fillRoundRect(BOX_X + 3, BOX_Y + 3, BOX_W, BOX_H, ARC, ARC);
        
        g2.setColor(BOX_BG);
        g2.fillRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);
        
        g2.setColor(BOX_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);
    }

    public static int drawTitle(Graphics2D g2, int callNumber) {
        int y = BOX_Y + PAD;
        g2.setFont(TITLE_FONT);
        g2.setColor(TITLE_COLOR);
        String title = "Call #" + callNumber;
        g2.drawString(title, BOX_X + PAD, y);
        return y + 4;
    }

    public static int drawTitle(Graphics2D g2, int currentCall, int totalCalls) {
        int y = BOX_Y + PAD;
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(TITLE_COLOR);
        String title = "CALL " + currentCall + " OF " + totalCalls;
        g2.drawString(title, BOX_X + PAD, y);
        return y + 4;
    }

    public static int drawDivider(Graphics2D g2, int y) {
        g2.setColor(DIVIDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(BOX_X + PAD, y + 10, BOX_X + BOX_W - PAD, y + 10);
        return y + 30;
    }

    public static int drawDescription(Graphics2D g2, String callerName, String displayedDesc, int y) {
        // Caller name
        g2.setFont(CALLER_FONT);
        g2.setColor(CALLER_COLOR);
        g2.drawString(callerName + ":", BOX_X + PAD, y);
        y += 22;

        // Dialogue text
        g2.setFont(DESC_FONT);
        g2.setColor(TEXT_COLOR);
        String text = displayedDesc.replace(callerName + ":\n", "");
        return drawWrappedText(g2, text, BOX_X + PAD, y, BOX_W - PAD * 2, 6);
    }
    
    public static int drawResponse(Graphics2D g2, String callerName,
                                    String displayedResponse, int y) {
        y += 16;

        for (String line : displayedResponse.split("\n")) {
            if (line.isBlank()) { y += 6; continue; }

            if (line.startsWith("You:")) {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(new Color(30, 130, 60));
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 4);

            } else if (line.startsWith(callerName + ":")) {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(CALLER_COLOR);
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 4);

            } else if (line.contains("+Performance Points") || line.contains("+Salary")) {
                // Split reward line on 3 spaces and draw each part in its own color
                String[] parts = line.split("   ");
                int rx = BOX_X + PAD;
                g2.setFont(REWARD_FONT);
                for (String part : parts) {
                    part = part.trim();
                    if (part.isEmpty()) continue;
                    if (part.startsWith("+Performance Points") || part.startsWith("+Perfomance Points")) {
                        g2.setColor(PP_COLOR);
                    } else if (part.startsWith("+Salary")) {
                        g2.setColor(SALARY_COLOR);
                    } else {
                        g2.setColor(TEXT_COLOR);
                    }
                    g2.drawString(part, rx, y + g2.getFontMetrics().getAscent());
                    rx += g2.getFontMetrics().stringWidth(part) + 24;
                }
                y += g2.getFontMetrics().getHeight() + 4;
                g2.setFont(RESPONSE_FONT);

            } else {
                g2.setFont(RESPONSE_FONT);
                g2.setColor(TEXT_COLOR);
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 4);
            }
        }
        return y;
    }

    public static void drawTimerBar(Graphics2D g2, int timerLeft, int timerMax) {
        int barX = BOX_X;
        int barW = BOX_W;

        // Track
        g2.setColor(TIMER_BG);
        g2.fillRoundRect(barX, TIMER_Y, barW, TIMER_H, TIMER_H, TIMER_H);

        // Fill
        float ratio = timerMax > 0 ? (float) timerLeft / timerMax : 0f;
        Color fill  = ratio < 0.3f ? TIMER_LOW : TIMER_FILL;
        g2.setColor(fill);
        int fillW = (int) (barW * ratio);
        if (fillW > 0) g2.fillRoundRect(barX, TIMER_Y, fillW, TIMER_H, TIMER_H, TIMER_H);
    }

    public static void drawChoices(Graphics2D g2, String[] labels, int chosenIndex,
                                    int hoveredChoice, Rectangle[] choiceRects) {
        if (labels == null || labels.length == 0) return;
        int count  = Math.min(labels.length, 3);
        int gap    = 12;
        int btnW   = (BOX_W - gap * (count - 1)) / count;
        int startX = BOX_X;

        for (int i = 0; i < count; i++) {
            int bx = startX + i * (btnW + gap);
            choiceRects[i] = new Rectangle(bx, CHOICE_Y, btnW, CHOICE_H);

            boolean isChosen  = chosenIndex == i;
            boolean isHovered = hoveredChoice == i && chosenIndex == -1;

            // Plain button look
            g2.setColor(isChosen ? CHOICE_CHOSEN : isHovered ? CHOICE_HOVER : CHOICE_NORMAL);
            g2.fillRect(bx, CHOICE_Y, btnW, CHOICE_H);
            g2.setColor(CHOICE_BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRect(bx, CHOICE_Y, btnW, CHOICE_H);

            // Label
            g2.setFont(CHOICE_FONT);
            g2.setColor(isChosen ? new Color(100, 110, 140) : TEXT_COLOR);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(labels[i],
                bx + (btnW - fm.stringWidth(labels[i])) / 2,
                CHOICE_Y + (CHOICE_H + fm.getAscent() - fm.getDescent()) / 2);
        }
    }

    public static int drawWrappedText(Graphics2D g2, String text, int x, int y,
                                       int maxWidth, int lineSpacing) {
        if (text == null || text.isEmpty()) return y;
        FontMetrics fm = g2.getFontMetrics();
        int lineH = fm.getHeight() + lineSpacing;
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
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
}