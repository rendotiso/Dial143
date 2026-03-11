package GUI.panels.shiftComponents;

import java.awt.*;

public class CallCreation {

    // ── Layout — shifted down 60px to clear TopBarComponents ─────────────────
    public static final int BOX_X = 90;
    public static final int BOX_Y = 70;   // was 30 — now clears the top bar
    public static final int BOX_W = 1100;
    public static final int BOX_H = 600;  // reduced to keep bottom edge at 670
    public static final int ARC   = 30;
    public static final int PAD   = 40;

    // ── Colors — matched to DialogueBoxLayer ──────────────────────────────────
    public static final Color BOX_BG        = new Color(255, 255, 255, 210); // same as dialogue box
    public static final Color BOX_BORDER    = new Color(200, 200, 200, 180);
    public static final Color TITLE_COLOR   = Color.BLACK;
    public static final Color CALLER_COLOR  = new Color(40, 80, 160);        // dark blue, readable on white
    public static final Color TEXT_COLOR    = Color.BLACK;
    public static final Color DIVIDER_COLOR = new Color(180, 180, 180);

    public static final Color TIMER_BG   = new Color(220, 220, 220);
    public static final Color TIMER_FILL = new Color(80, 140, 220);
    public static final Color TIMER_LOW  = new Color(200, 60, 60);

    public static final Color CHOICE_NORMAL = new Color(230, 235, 245);
    public static final Color CHOICE_HOVER  = new Color(180, 200, 240);
    public static final Color CHOICE_CHOSEN = new Color(200, 210, 230);
    public static final Color CHOICE_BORDER = new Color(140, 160, 210);

    public static final Color PP_COLOR     = new Color(40, 160, 80);
    public static final Color SALARY_COLOR = new Color(180, 120, 0);

    // ── Fonts — matched to DialogueBoxLayer ───────────────────────────────────
    public static final Font TITLE_FONT    = new Font("Arial", Font.BOLD,   20);
    public static final Font CALLER_FONT   = new Font("Arial", Font.BOLD,   18);
    public static final Font DESC_FONT     = new Font("Arial", Font.PLAIN,  18);
    public static final Font CHOICE_FONT   = new Font("Arial", Font.PLAIN,  16);
    public static final Font REWARD_FONT   = new Font("Arial", Font.BOLD,   16);
    public static final Font RESPONSE_FONT = new Font("Arial", Font.ITALIC, 18);

    // ── Draw methods ──────────────────────────────────────────────────────────

    public static void drawBox(Graphics2D g2) {
        // shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(BOX_X + 4, BOX_Y + 4, BOX_W, BOX_H, ARC, ARC);
        // body
        g2.setColor(BOX_BG);
        g2.fillRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);
        // border
        g2.setColor(BOX_BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, ARC, ARC);
    }

    public static int drawTitle(Graphics2D g2, int callNumber) {
        int y = BOX_Y + PAD + 16;
        g2.setFont(TITLE_FONT);
        g2.setColor(TITLE_COLOR);
        String title = "Call #" + callNumber;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, BOX_X + (BOX_W - fm.stringWidth(title)) / 2, y);
        return y + 10;
    }

    public static int drawDivider(Graphics2D g2, int y) {
        g2.setColor(DIVIDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(BOX_X + PAD, y + 8, BOX_X + BOX_W - PAD, y + 8);
        return y + 35;
    }

    public static int drawDescription(Graphics2D g2, String callerName, String displayedDesc, int y) {
        g2.setFont(CALLER_FONT);
        g2.setColor(CALLER_COLOR);
        g2.drawString(callerName + ":", BOX_X + PAD, y);
        y += 20;
        g2.setFont(DESC_FONT);
        g2.setColor(TEXT_COLOR);
        String text = displayedDesc.replace(callerName + ":\n", "");
        return drawWrappedText(g2, text, BOX_X + PAD, y, BOX_W - PAD * 2, 20) + 10;
    }

    public static int drawResponse(Graphics2D g2, String callerName,
                                    String displayedResponse, int y) {
        y += 20;
        g2.setColor(new Color(80, 120, 200, 25));
        g2.fillRoundRect(BOX_X + PAD - 8, y - 6, BOX_W - PAD * 2 + 16, 160, 12, 12);

        g2.setFont(RESPONSE_FONT);
        for (String line : displayedResponse.split("\n")) {
            if (line.contains("+PP:")) {
                String[] parts = line.split("   ");
                int rx = BOX_X + PAD;
                for (String part : parts) {
                    if (part.startsWith("+PP:"))        { g2.setColor(PP_COLOR);     g2.setFont(REWARD_FONT); }
                    else if (part.startsWith("+Salary:")){ g2.setColor(SALARY_COLOR); g2.setFont(REWARD_FONT); }
                    g2.drawString(part, rx, y + 16);
                    rx += g2.getFontMetrics().stringWidth(part) + 20;
                }
                g2.setFont(RESPONSE_FONT);
                y += 22;
            } else if (line.startsWith("You:")) {
                g2.setColor(new Color(30, 130, 60));
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 20);
            } else {
                g2.setColor(CALLER_COLOR);
                y = drawWrappedText(g2, line, BOX_X + PAD, y, BOX_W - PAD * 2, 20);
            }
        }
        return y + 10;
    }

    public static void drawTimerBar(Graphics2D g2, int timerLeft, int timerMax) {
        int barX = BOX_X + PAD;
        int barY = BOX_Y + BOX_H - 110;
        int barW = BOX_W - PAD * 2;
        int barH = 10;

        g2.setColor(TIMER_BG);
        g2.fillRoundRect(barX, barY, barW, barH, barH, barH);

        float ratio = timerMax > 0 ? (float) timerLeft / timerMax : 0f;
        g2.setColor(ratio < 0.3f ? TIMER_LOW : TIMER_FILL);
        int fillW = (int) (barW * ratio);
        if (fillW > 0) g2.fillRoundRect(barX, barY, fillW, barH, barH, barH);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(ratio < 0.3f ? TIMER_LOW : Color.DARK_GRAY);
        g2.drawString(timerLeft + "s", barX + barW - 28, barY - 4);
    }

    public static void drawChoices(Graphics2D g2, String[] labels, int chosenIndex,
                                    int hoveredChoice, Rectangle[] choiceRects) {
        if (labels == null || labels.length == 0) return;
        int count  = Math.min(labels.length, 3);
        int btnH   = 44, gap = 16;
        int btnW   = (BOX_W - PAD * 2 - gap * (count - 1)) / count;
        int btnY   = BOX_Y + BOX_H - 70;
        int startX = BOX_X + PAD;

        for (int i = 0; i < count; i++) {
            int bx = startX + i * (btnW + gap);
            choiceRects[i] = new Rectangle(bx, btnY, btnW, btnH);

            boolean isChosen  = chosenIndex == i;
            boolean isHovered = hoveredChoice == i && chosenIndex == -1;

            g2.setColor(isChosen ? CHOICE_CHOSEN : isHovered ? CHOICE_HOVER : CHOICE_NORMAL);
            g2.fillRoundRect(bx, btnY, btnW, btnH, 12, 12);
            g2.setColor(isChosen ? new Color(100, 130, 200) : CHOICE_BORDER);
            g2.setStroke(new BasicStroke(isChosen ? 2f : 1f));
            g2.drawRoundRect(bx, btnY, btnW, btnH, 12, 12);
            if (isChosen) {
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(bx, btnY, btnW, btnH, 12, 12);
            }
            g2.setFont(CHOICE_FONT);
            g2.setColor(isChosen ? new Color(100, 110, 130) : TEXT_COLOR);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(labels[i],
                bx + (btnW - fm.stringWidth(labels[i])) / 2,
                btnY + (btnH + fm.getAscent() - fm.getDescent()) / 2);
        }
    }

    public static int drawWrappedText(Graphics2D g2, String text, int x, int y,
                                       int maxWidth, int lineHeight) {
        if (text == null || text.isEmpty()) return y;
        FontMetrics fm = g2.getFontMetrics();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String test = line.isEmpty() ? word : line + " " + word;
            if (fm.stringWidth(test) > maxWidth && !line.isEmpty()) {
                g2.drawString(line.toString(), x, y + fm.getAscent());
                y += lineHeight + fm.getHeight() - fm.getAscent();
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (!line.isEmpty()) {
            g2.drawString(line.toString(), x, y + fm.getAscent());
            y += lineHeight + fm.getHeight() - fm.getAscent();
        }
        return y;
    }
}