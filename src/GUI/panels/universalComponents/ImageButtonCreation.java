package GUI.panels.universalComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ImageButtonCreation extends JButton {

    // Fallback style (used when no image is set)
    private static final Color CHOICE_NORMAL = new Color(238, 238, 238);
    private static final Color CHOICE_HOVER  = new Color(200, 215, 240);
    private static final Color CHOICE_CHOSEN = new Color(180, 195, 225);
    private static final Color CHOICE_BORDER = new Color(160, 160, 160);
    private static final Color TEXT_COLOR    = new Color(30,  30,  30);
    private static final Font  CHOICE_FONT   = new Font("Arial", Font.PLAIN, 14);

    // Tint overlays applied on top of the image after 9-slice paint
    private static final Color TINT_HOVER   = new Color(220, 235, 255, 60);
    private static final Color TINT_PRESSED = new Color(0,   0,   0,   70);

    private BufferedImage imgNormal;
    private boolean hov = false, isPressed = false;

    public ImageButtonCreation(String label) {
        super(label);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered (MouseEvent e) { hov       = true;  repaint(); }
            @Override public void mouseExited  (MouseEvent e) { hov       = false; repaint(); }
            @Override public void mousePressed (MouseEvent e) { isPressed = true;  repaint(); }
            @Override public void mouseReleased(MouseEvent e) { isPressed = false; repaint(); }
        });
    }

    // ── Image loading ─────────────────────────────────────────────────────────

    /**
     * Load an image by filename from /GUI/resources/icons/ and apply it.
     * Callers never need their own loadImage() method.
     */
    public void setImage(String filename) {
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/icons/" + filename);
            if (s != null) setImage(ImageIO.read(s));
        } catch (Exception ex) { /* silently ignore — fallback style used */ }
    }

    /** Apply an already-loaded BufferedImage directly. */
    public void setImage(BufferedImage img) {
        this.imgNormal = img;
        repaint();
    }

    // ── Paint ─────────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        if (imgNormal != null) {
            // 1. 9-slice paint
            paintNineSliceH(g2, imgNormal, w, h, (int)(imgNormal.getWidth() * 0.20));

            // 2. Tint overlay clipped to button bounds
            if (isPressed) {
                g2.setColor(TINT_PRESSED);
                g2.fillRect(0, 0, w, h);
            } else if (hov) {
                g2.setColor(TINT_HOVER);
                g2.fillRect(0, 0, w, h);
            }

        } else {
            // Fallback solid-colour style
            Color bg = isPressed ? CHOICE_CHOSEN : hov ? CHOICE_HOVER : CHOICE_NORMAL;
            g2.setColor(bg);
            g2.fillRect(0, 0, w, h);
            g2.setColor(CHOICE_BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRect(0, 0, w - 1, h - 1);
        }

        // 3. Label text always on top
        g2.setFont(CHOICE_FONT);
        g2.setColor(TEXT_COLOR);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(getText(),
            (w - fm.stringWidth(getText())) / 2,
            (h + fm.getAscent() - fm.getDescent()) / 2);

        g2.dispose();
    }

    // ── 9-slice helpers ───────────────────────────────────────────────────────

    private void paintNineSliceH(Graphics2D g2, BufferedImage src,
                                  int dstW, int dstH, int sliceX) {
        Rectangle b = getOpaqueBounds(src);
        if (b == null) return;

        int cw = b.width, ch = b.height;
        sliceX = Math.min(sliceX, cw / 2);

        int srcL  = b.x,               srcLR = b.x + sliceX;
        int srcRL = b.x + cw - sliceX, srcR  = b.x + cw;
        int srcT  = b.y,               srcBot = b.y + ch;

        double scaleH = (double) dstH / ch;
        int capW = Math.min((int) Math.round(sliceX * scaleH), dstW / 2);
        int midW = dstW - 2 * capW;

        g2.drawImage(src, 0,           0, capW,        dstH, srcL,  srcT, srcLR, srcBot, null);
        if (midW > 0 && (srcRL - srcLR) > 0)
            g2.drawImage(src, capW,    0, capW + midW, dstH, srcLR, srcT, srcRL, srcBot, null);
        g2.drawImage(src, capW + midW, 0, dstW,        dstH, srcRL, srcT, srcR,  srcBot, null);
    }

    private Rectangle getOpaqueBounds(BufferedImage img) {
        int minX = img.getWidth(), minY = img.getHeight(), maxX = -1, maxY = -1;
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++)
                if (((img.getRGB(x, y) >> 24) & 0xff) > 10) {
                    if (x < minX) minX = x; if (x > maxX) maxX = x;
                    if (y < minY) minY = y; if (y > maxY) maxY = y;
                }
        return (maxX < 0) ? null : new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}