package GUI.panels.dialogueComponents; 

import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class DialogueBoxLayer extends JPanel {
    
    private MainFrame mainFrame;
    private Font mulishFont; 
    private String speaker = "";
    private String fullText = "";
    private Timer typewriterTimer;
    private int charIndex = 0;
    private int typewriterDelay = 12;
    
    // Speed up related
    private Timer speedTimer;
    private Runnable onAdvanceRequest;
    private boolean isSpeedActive = false;

    private Font speakerFont;
    private Font textFont;

    private static final Color OUTER_BOX_TRANSPARENT_WHITE = new Color(255, 255, 255, 178);
    private static final Color INNER_BOX_TRANSPARENT = new Color(255, 255, 255, 0);
    private static final Color TEXT_COLOR = new Color(0, 0, 0, 255);
    private static final Color SPEAKER_TEXT_COLOR = new Color(0, 0, 0, 255);

    public DialogueBoxLayer(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(1280, 720));
        
        loadCustomFonts();
        initializeFonts();
        initComponents();
        styleSpeedButton();
        setupTransparency();
        disableTextHighlighting();
    }
    
    // ── Speed Up Methods ──────────────────────────────────────────────────────
    
    private void styleSpeedButton() {
        if (speedBtn != null) {
            speedBtn.setFont(new Font("Arial", Font.BOLD, 20));
            speedBtn.setForeground(new Color(80, 80, 100));
            speedBtn.setBackground(new Color(0, 0, 0, 0));
            speedBtn.setOpaque(false);
            speedBtn.setContentAreaFilled(false);
            speedBtn.setBorderPainted(false);
            speedBtn.setFocusPainted(false);
            speedBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
    
    public void setOnAdvanceRequest(Runnable callback) {
        this.onAdvanceRequest = callback;
    }
    
    public void onDialogueShown() {
        if (speedBtn != null) {
            speedBtn.setVisible(true);
        }
    }
    
    public void onChoicesShown() {
        stopSpeedUp();
        if (speedBtn != null) {
            speedBtn.setVisible(false);
        }
    }
    
    public void onSceneEnd() {
        stopSpeedUp();
    }
    
    private void toggleSpeedUp() {
        if (isSpeedActive) {
            stopSpeedUp();
        } else {
            if (isAnimating()) {
                skipAnimation();
                return;
            }
            startSpeedUp();
        }
    }
    
    private void startSpeedUp() {
        stopSpeedUp();
        isSpeedActive = true;
        if (speedBtn != null) {
            speedBtn.setText("■");
            speedBtn.setForeground(new Color(180, 50, 50)); // Red when active
        }
        
        speedTimer = new Timer(80, e -> {
            if (!isVisible() || !isDisplayable()) {
                stopSpeedUp();
                return;
            }
            if (isAnimating()) {
                skipAnimation();
            } else if (onAdvanceRequest != null) {
                SwingUtilities.invokeLater(() -> {
                    if (onAdvanceRequest != null) {
                        onAdvanceRequest.run();
                    }
                });
            }
        });
        speedTimer.start();
    }
    
    private void stopSpeedUp() {
        if (speedTimer != null) {
            speedTimer.stop();
            speedTimer = null;
        }
        isSpeedActive = false;
        if (speedBtn != null) {
            speedBtn.setText("▶▶");
            speedBtn.setForeground(new Color(80, 80, 100)); 
        }
    }

    private void initializeFonts() {
        speakerFont = (mulishFont != null ? mulishFont.deriveFont(24f) : new Font("Arial", Font.BOLD, 24));
        textFont = (mulishFont != null ? mulishFont.deriveFont(18f) : new Font("Arial", Font.PLAIN, 18));
    }
    
    private void setupTransparency() {
        setBackground(new Color(0, 0, 0, 0));
        
        if (txtBackground != null) {
            txtBackground.setBackground(OUTER_BOX_TRANSPARENT_WHITE);
            txtBackground.setOpaque(true);
            txtBackground.setEditable(false);
            txtBackground.setFocusable(false);
            txtBackground.setForeground(TEXT_COLOR);
            txtBackground.setFont(textFont);
        }
        
        if (txtDialogue != null) {
            txtDialogue.setBackground(INNER_BOX_TRANSPARENT);
            txtDialogue.setOpaque(false);
            txtDialogue.setForeground(TEXT_COLOR);
            txtDialogue.setFont(textFont);
        }
        
        if (txtScrollPane != null) {
            txtScrollPane.setOpaque(false);
            txtScrollPane.getViewport().setOpaque(false);
            txtScrollPane.setBorder(null);
        }
        
        if (jScrollPane1 != null) {
            jScrollPane1.setOpaque(false);
            jScrollPane1.getViewport().setOpaque(false);
            jScrollPane1.setBorder(null);
        }
        
        if (speakerLbl != null) {
            speakerLbl.setForeground(SPEAKER_TEXT_COLOR);
            speakerLbl.setBackground(new Color(0, 0, 0, 0));
            speakerLbl.setOpaque(false);
        }
    }
    
    private void disableTextHighlighting() {
        if (txtDialogue != null) {
            txtDialogue.setHighlighter(null);
            txtDialogue.setSelectionColor(INNER_BOX_TRANSPARENT);
            txtDialogue.setSelectedTextColor(TEXT_COLOR);
            txtDialogue.setFocusable(false);
            txtDialogue.setCaretColor(INNER_BOX_TRANSPARENT);
        }
        
        if (txtBackground != null) {
            txtBackground.setHighlighter(null);
            txtBackground.setSelectionColor(OUTER_BOX_TRANSPARENT_WHITE);
            txtBackground.setSelectedTextColor(TEXT_COLOR);
            txtBackground.setFocusable(false);
            txtBackground.setCaretColor(OUTER_BOX_TRANSPARENT_WHITE);
        }
    }
    
    public void setSpeaker(String name) {
        this.speaker = (name == null) ? "" : name;
        if (speakerLbl != null) {
            speakerLbl.setText(speaker);
        }
    }
    
    public void setDialogue(String text) {
        if (text == null) text = "";
        this.fullText = formatDialogue(text);
        this.charIndex = 0;

        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        if (txtDialogue != null) {
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
    }

    public void setDialogueInstant(String text) {
        if (text == null) text = "";
        this.fullText = formatDialogue(text);
        this.charIndex = fullText.length();
        
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        
        if (txtDialogue != null) {
            txtDialogue.setText(fullText);
        }
    }

    public String formatDialogue(String text) {
        if (mainFrame == null) {
            return text;
        }
        
        try {
            String playerName = mainFrame.getPlayerName();
            String playerGender = mainFrame.getPlayerGender();
            String playerPronoun = mainFrame.getPlayerPronoun();
            
            if (playerName == null || playerName.isEmpty()) {
                return text;
            }
            
            String formatted = text;
            
            formatted = formatted.replace("{name}", playerName);
            
            if (playerPronoun != null && !playerPronoun.isEmpty()) {
                String[] pronouns = playerPronoun.split("/");
                String subject = pronouns.length > 0 ? pronouns[0] : "they";
                String object = pronouns.length > 1 ? pronouns[1] : "them";
                
                formatted = formatted.replace("{pronoun_subject}", subject);
                formatted = formatted.replace("{pronoun_object}", object);
            }
            
            if (playerGender != null) {
                String possessive = playerGender.equals("FEMALE") ? "her" : "his";
                formatted = formatted.replace("{pronoun_possessive}", possessive);
            }
            
            return formatted;
        } catch (Exception e) {
            System.err.println("Error formatting dialogue: " + e.getMessage());
            return text;
        }
    }

    public void skipAnimation() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
            if (txtDialogue != null) {
                txtDialogue.setText(fullText);
            }
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
        stopSpeedUp();
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        if (txtDialogue != null) {
            txtDialogue.setText("");
        }
        speaker = "";
        fullText = "";
        charIndex = 0;
        repaint();
    }
    
    private void loadCustomFonts() {
        try {
            InputStream stream = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            if (stream == null) { 
                mulishFont = null; 
                return; 
            }
            Font base = Font.createFont(Font.TRUETYPE_FONT, stream);
            java.util.Map<java.awt.font.TextAttribute, Object> attrs = new java.util.HashMap<>();
            attrs.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
            mulishFont = base.deriveFont(attrs);
        } catch (FontFormatException e) {
            System.err.println("Font format error: " + e.getMessage());
            mulishFont = null;
        } catch (Exception e) {
            System.err.println("Error loading fonts: " + e.getMessage());
            mulishFont = null;
        }
    }
    
    @Override 
    public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override 
    public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override 
    public Dimension getPreferredSize() { return new Dimension(1280, 720); }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        speedBtn = new javax.swing.JButton();
        speakerLbl = new javax.swing.JLabel();
        txtScrollPane = new javax.swing.JScrollPane();
        txtDialogue = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBackground = new javax.swing.JTextArea();

        setLayout(null);

        speedBtn.setBackground(new java.awt.Color(255, 255, 255));
        speedBtn.setForeground(new java.awt.Color(0, 0, 0));
        speedBtn.setText("▶▶");
        speedBtn.setBorder(null);
        speedBtn.setBorderPainted(false);
        speedBtn.setFocusable(false);
        speedBtn.addActionListener(this::speedBtnActionPerformed);
        add(speedBtn);
        speedBtn.setBounds(1140, 640, 80, 40);

        speakerLbl.setFont(speakerFont);
        speakerLbl.setText("jLabel1");
        add(speakerLbl);
        speakerLbl.setBounds(90, 500, 320, 30);

        txtScrollPane.setBorder(null);
        txtScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        txtScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtDialogue.setEditable(false);
        txtDialogue.setBackground(new java.awt.Color(255, 255, 255));
        txtDialogue.setColumns(20);
        txtDialogue.setFont(textFont);
        txtDialogue.setForeground(new java.awt.Color(0, 0, 0));
        txtDialogue.setLineWrap(true);
        txtDialogue.setRows(5);
        txtDialogue.setBorder(null);
        txtDialogue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtDialogue.setFocusable(false);
        txtDialogue.setMargin(new java.awt.Insets(5, 5, 5, 5));
        txtDialogue.setMaximumSize(new java.awt.Dimension(1110, 180));
        txtDialogue.setPreferredSize(new java.awt.Dimension(1100, 180));
        txtScrollPane.setViewportView(txtDialogue);

        add(txtScrollPane);
        txtScrollPane.setBounds(90, 540, 1100, 120);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setEnabled(false);

        txtBackground.setEditable(false);
        txtBackground.setBackground(new java.awt.Color(255, 255, 255));
        txtBackground.setColumns(20);
        txtBackground.setRows(5);
        txtBackground.setBorder(null);
        jScrollPane1.setViewportView(txtBackground);

        add(jScrollPane1);
        jScrollPane1.setBounds(60, 490, 1160, 190);
    }// </editor-fold>//GEN-END:initComponents

    private void speedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedBtnActionPerformed
        toggleSpeedUp();
    }//GEN-LAST:event_speedBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel speakerLbl;
    private javax.swing.JButton speedBtn;
    private javax.swing.JTextArea txtBackground;
    private javax.swing.JTextArea txtDialogue;
    private javax.swing.JScrollPane txtScrollPane;
    // End of variables declaration//GEN-END:variables
}
