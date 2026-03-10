package GUI.panels.dialogueComponents; 

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogueBoxLayer extends javax.swing.JPanel {
    
    private Image boxImage;
    private String speaker = "";
    private String fullText = "";
    private String displayedText = "";
    private Timer typewriterTimer;
    private int charIndex = 0;
    private int typewriterDelay = 12; // ms per character

    private final Font speakerFont = new Font("Arial", Font.BOLD, 24);
    private final Font textFont    = new Font("Arial", Font.PLAIN, 18);

    private final int BOX_WIDTH  = 1100;
    private final int BOX_HEIGHT = 180;
    private final int BOX_Y      = 500;
    private final int BOX_X      = (1280 - BOX_WIDTH) / 2;

    private final int SPEAKER_X = BOX_X + 30;
    private final int SPEAKER_Y = BOX_Y + 40;

    private final Color BOX_COLOR     = new Color(255, 255, 255, 210);
    private final Color TEXT_COLOR    = Color.BLACK;
    private final Color SPEAKER_COLOR = Color.BLACK;

    public DialogueBoxLayer() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(1280, 720));

        createComponents();
        addComponents();
        createDefaultBox();
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }

    private void createComponents() {
        txtDialogue = new JTextArea();
        txtDialogue.setEditable(false);
        txtDialogue.setLineWrap(true);
        txtDialogue.setWrapStyleWord(true);
        txtDialogue.setFocusable(false);
        txtDialogue.setHighlighter(null);
        txtDialogue.setOpaque(false);
        txtDialogue.setForeground(TEXT_COLOR);
        txtDialogue.setFont(textFont);
        txtDialogue.setCursor(Cursor.getDefaultCursor());

        scrollPane = new JScrollPane(txtDialogue);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setEnabled(false);
        scrollPane.setFocusable(false);
        scrollPane.setWheelScrollingEnabled(false);
    }

    private void addComponents() {
        int textX      = BOX_X + 40;
        int textY      = BOX_Y + 60;
        int textWidth  = BOX_WIDTH - 80;
        int textHeight = BOX_HEIGHT - 70;

        scrollPane.setBounds(textX, textY, textWidth, textHeight);
        add(scrollPane);
    }

    private void createDefaultBox() {
        BufferedImage defaultBox = new BufferedImage(BOX_WIDTH, BOX_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultBox.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(BOX_COLOR);
        g2d.fillRoundRect(0, 0, BOX_WIDTH, BOX_HEIGHT, 30, 30);
        g2d.dispose();
        boxImage = defaultBox;
    }

    public void setDialogue(String text) {
        if (text == null) text = "";
        this.fullText = text;
        this.charIndex = 0;

        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        txtDialogue.setText(""); 

        typewriterTimer = new Timer(typewriterDelay, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (charIndex < fullText.length()) {
                try {
                    txtDialogue.getDocument().insertString(
                        txtDialogue.getDocument().getLength(),
                        String.valueOf(fullText.charAt(charIndex)),
                        null
                    );
                } catch (javax.swing.text.BadLocationException ex) {
                    txtDialogue.setText(fullText); // fallback
                }
                charIndex++;
            } else {
                typewriterTimer.stop();
            }
        }
    });
    
        typewriterTimer.start();
    }

    public void skipAnimation() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
            txtDialogue.setText(fullText);
            charIndex = fullText.length();
        }
    }

    public boolean isAnimating() {
        return typewriterTimer != null && typewriterTimer.isRunning();
    }

    public void setSpeaker(String name) {
        this.speaker = (name == null) ? "" : name;
        repaint();
    }

    public void setTypewriterDelay(int ms) {
        this.typewriterDelay = ms;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boxImage != null) {
            g.drawImage(boxImage, BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, this);
            if (!speaker.isEmpty()) {
                g.setColor(SPEAKER_COLOR);
                g.setFont(speakerFont);
                g.drawString(speaker, SPEAKER_X, SPEAKER_Y);
            }
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        txtDialogue = new javax.swing.JTextArea();

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPane.setBorder(new javax.swing.border.MatteBorder(null));
        scrollPane.setEnabled(false);
        scrollPane.setFocusable(false);
        scrollPane.setWheelScrollingEnabled(false);

        txtDialogue.setEditable(false);
        txtDialogue.setColumns(20);
        txtDialogue.setLineWrap(true);
        txtDialogue.setRows(5);
        txtDialogue.setWrapStyleWord(true);
        txtDialogue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtDialogue.setFocusable(false);
        txtDialogue.setHighlighter(null);
        txtDialogue.setName(""); // NOI18N
        scrollPane.setViewportView(txtDialogue);

        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 470, 1160, 187));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea txtDialogue;
    // End of variables declaration//GEN-END:variables
}
