package GUI.panels.dialogueComponents; 

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class DialogueBoxLayer extends JPanel {
    
    private Font mulishFont; 
    private Font unicaFont; 
    
    private Image boxImage;
    private String speaker = "";
    private String fullText = "";
    private Timer typewriterTimer;
    private int charIndex = 0;
    private int typewriterDelay = 12; 

    private Font speakerFont;
    private Font textFont;
    
    private final int BOX_WIDTH  = 1100;
    private final int BOX_HEIGHT = 180;
    private final int BOX_Y      = 500;
    private final int BOX_X      = (1280 - BOX_WIDTH) / 2;

    private final int SPEAKER_X = BOX_X + 30;
    private final int SPEAKER_Y = BOX_Y + 35;
    private final int TEXT_X    = BOX_X + 40;
    private final int TEXT_Y    = BOX_Y + 50;
    private final int TEXT_WIDTH  = BOX_WIDTH - 80;
    private final int TEXT_HEIGHT = BOX_HEIGHT - 70;

    private final Color BOX_COLOR     = new Color(255, 255, 255, 210);
    private final Color TEXT_COLOR    = Color.BLACK;
    private final Color SPEAKER_COLOR = Color.BLACK;

    private JScrollPane scrollPane;
    private JTextArea txtDialogue;
    private JLabel speakerLabel;

    public DialogueBoxLayer() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(1280, 720));
        
        loadCustomFonts();
        initializeFonts();
        createComponents();
        createDefaultBox();
    }

    private void initializeFonts() {
        speakerFont = (mulishFont != null ? mulishFont.deriveFont(24f) : new Font("Arial", Font.BOLD, 24));
        textFont = (mulishFont != null ? mulishFont.deriveFont(18f) : new Font("Arial", Font.PLAIN, 18));
    }

    private void createComponents() {
        speakerLabel = new JLabel();
        speakerLabel.setForeground(SPEAKER_COLOR);
        speakerLabel.setFont(speakerFont);
        speakerLabel.setBounds(SPEAKER_X, SPEAKER_Y - 25, 400, 30); // Adjust Y to position properly
        speakerLabel.setOpaque(false);
        add(speakerLabel);
        
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
        txtDialogue.setBorder(null);
        txtDialogue.setMargin(new Insets(5, 5, 5, 5));

        scrollPane = new JScrollPane(txtDialogue);
        scrollPane.setBounds(TEXT_X, TEXT_Y, TEXT_WIDTH, TEXT_HEIGHT);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setEnabled(false);
        scrollPane.setFocusable(false);
        scrollPane.setWheelScrollingEnabled(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane);
    }

    private void createDefaultBox() {
        BufferedImage defaultBox = new BufferedImage(BOX_WIDTH, BOX_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultBox.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(BOX_COLOR);
        g2d.fillRoundRect(0, 0, BOX_WIDTH, BOX_HEIGHT, 30, 30);
        
        g2d.setColor(new Color(200, 200, 200, 180));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(0, 0, BOX_WIDTH - 1, BOX_HEIGHT - 1, 30, 30);
        
        g2d.dispose();
        boxImage = defaultBox;
    }
    
    public void setSpeaker(String name) {
        this.speaker = (name == null) ? "" : name;
        speakerLabel.setText(speaker); 
    }
    
    public void setDialogue(String text) {
        if (text == null) text = "";
        this.fullText = text;
        this.charIndex = 0;

        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        txtDialogue.setText(""); 

        typewriterTimer = new Timer(typewriterDelay, e -> {
            if (charIndex < fullText.length()) {
                txtDialogue.append(String.valueOf(fullText.charAt(charIndex)));
                charIndex++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        
        typewriterTimer.start();
    }

    public void setDialogueInstant(String text) {
        if (text == null) text = "";
        this.fullText = text;
        this.charIndex = fullText.length();
        
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        
        txtDialogue.setText(fullText);
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

    public void setTypewriterDelay(int ms) {
        this.typewriterDelay = ms;
    }

    public void clear() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        txtDialogue.setText("");
        speaker = "";
        fullText = "";
        charIndex = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boxImage != null) {
            g.drawImage(boxImage, BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, this);
        }
    }
    
    private void loadCustomFonts() {
        try {
            InputStream mulish = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (mulish != null) {
                mulishFont = Font.createFont(Font.TRUETYPE_FONT, mulish);
            } else {
                mulishFont = null;
            }
            
            InputStream unica = getClass().getResourceAsStream("/GUI/resources/font/UnicaOne-Regular.ttf");
            if (unica != null) {
                unicaFont = Font.createFont(Font.TRUETYPE_FONT, unica);
            } else {
                unicaFont = null;
            }           
        } catch (FontFormatException e) {
            System.err.println("Font format error: " + e.getMessage());
            e.printStackTrace();
            mulishFont = null;
            unicaFont = null;
        } catch (Exception e) {
            System.err.println("Error loading fonts: " + e.getMessage());
            e.printStackTrace();
            mulishFont = null;
            unicaFont = null;
        }
    }
    
    @Override 
    public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override 
    public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override 
    public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}