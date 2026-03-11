package GUI.panels.shiftComponents;

import GUI.panels.shiftComponents.CallCreation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CallDialogueBoxLayer extends JPanel {

    // Hardcoded test fields (replace with CallScene later)
    private String   callerName          = "";
    private int      callNumber          = 0;
    private String[] testChoiceLabels    = {};
    private String[] testPlayerResponses = {};
    private String[] testCallerResponses = {};
    private int[]    testPP              = {};
    private int[]    testSalary          = {};
    // Hardcoded test fields (replace with CallScene later)
    
    
    private int     timerMax     = 10;
    private int     timerLeft    = 10;
    private boolean timerRunning = false;
    private boolean timerPaused  = false;  
    private Timer   countdownTimer, typewriterTimer, responseTimer;

    private String fullDesc      = "", displayedDesc = "";
    private int    typeIndex     = 0;

    private String  fullResponse      = "", displayedResponse = "";
    private int     responseIndex     = 0;
    private boolean showingResponse   = false;

    private boolean isLoaded          = false; 
    private int     chosenIndex       = -1;  
    
    private final Rectangle[] choiceRects = new Rectangle[3];
    private int hoveredChoice = -1;

    public CallDialogueBoxLayer() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateHover(e.getPoint()); }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleClick(e.getPoint()); }
        });
    }

    
        // Hardcoded test methods (replace with CallScene later)
    public void loadTest() {
        stopAll();

        callerName          = "Sarah";
        callNumber          = 1;
        testChoiceLabels    = new String[]{"Check flights", "Ask for reference", "Transfer call"};
        testPlayerResponses = new String[]{
            "Let me check the next available flights to Cebu right away.",
            "Can I get your booking reference number first?",
            "Please hold while I transfer you to rebooking."
        };
        testCallerResponses = new String[]{
            "Thank you so much! I really appreciate your help.",
            "Oh yes, it's CEB-2024-887.",
            "Wait, no — I've been on hold for an hour already!"
        };
        testPP     = new int[]{ 10,  5, -5};
        testSalary = new int[]{500, 300, 100};

        chosenIndex       = -1;
        showingResponse   = false;
        timerMax          = 10;
        timerLeft         = 10;
        timerRunning      = false;
        timerPaused       = false; 
        displayedDesc     = "";
        typeIndex         = 0;
        fullResponse      = "";
        displayedResponse = "";
        isLoaded          = true;

        fullDesc = callerName + ":\nHi, I'm at NAIA Terminal 3 and my flight to Cebu was just "
                 + "cancelled. Hay naku, I really need to get home to my kids, "
                 + "can you see what's available?";

        startTypewriter();
        repaint();
    } 
// Hardcoded test methods (replace with CallScene later)

    public void pauseTimer() {
        if (timerRunning && !timerPaused) {
            timerPaused = true;
            if (countdownTimer != null) countdownTimer.stop();
        }
    }

    public void resumeTimer() {
        if (timerPaused) {
            timerPaused = false;
            if (timerLeft > 0 && chosenIndex == -1) startTimer();
        }
    }

    public boolean isTimerPaused() {
        return timerPaused;
    }

    private void startTypewriter() {
        typewriterTimer = new Timer(14, e -> {
            if (typeIndex < fullDesc.length()) {
                displayedDesc = fullDesc.substring(0, ++typeIndex);
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
                startTimer();
            }
        });
        typewriterTimer.start();
    }

    private void startTimer() {
        timerRunning = true;
        timerPaused = false;
        countdownTimer = new Timer(1000, e -> {
            if (!timerPaused) {
                if (timerLeft > 0) {
                    timerLeft--;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    timerRunning = false;
                    if (chosenIndex == -1 && testChoiceLabels.length > 0) selectChoice(0); 
                }
            }
        });
        countdownTimer.start();
    }

    private void stopAll() {
        if (typewriterTimer != null) typewriterTimer.stop();
        if (countdownTimer  != null) countdownTimer.stop();
        if (responseTimer   != null) responseTimer.stop();
        timerRunning = false;
        timerPaused = false;
    }

    private void selectChoice(int index) {
        if (chosenIndex != -1) return;
        if (countdownTimer != null) countdownTimer.stop();
        timerRunning = false;
        timerPaused = false;
        chosenIndex  = index;

        //HARD CODED TEST RESPONSE
        fullResponse = "You: " + testPlayerResponses[index]
                     + "\n\n" + callerName + ": " + testCallerResponses[index]
                     + "\n\n+PP: " + testPP[index]
                     + "   +Salary: ₱" + testSalary[index];
        //HARD TEST RESPONSE
        
        
        displayedResponse = "";
        responseIndex     = 0;
        showingResponse   = true;

        responseTimer = new Timer(14, e -> {
            if (responseIndex < fullResponse.length()) {
                displayedResponse = fullResponse.substring(0, ++responseIndex);
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        responseTimer.start();
        repaint();
    }

    private void updateHover(Point p) {
        if (chosenIndex != -1 || !timerRunning || timerPaused) return;
        int prev = hoveredChoice;
        hoveredChoice = -1;
        for (int i = 0; i < choiceRects.length; i++)
            if (choiceRects[i] != null && choiceRects[i].contains(p)) { hoveredChoice = i; break; }
        if (hoveredChoice != prev) repaint();
    }

    private void handleClick(Point p) {
        if (chosenIndex != -1 || timerPaused) return; 
        for (int i = 0; i < choiceRects.length; i++)
            if (choiceRects[i] != null && choiceRects[i].contains(p)) { selectChoice(i); return; }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isLoaded) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        CallCreation.drawBox(g2);
        int y = CallCreation.drawTitle(g2, callNumber);
        y = CallCreation.drawDivider(g2, y);
        y = CallCreation.drawDescription(g2, callerName, displayedDesc, y);
        
        if (timerPaused) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillRoundRect(CallCreation.BOX_X + 20, CallCreation.BOX_Y + 20, 
                            CallCreation.BOX_W - 40, CallCreation.BOX_H - 40, 20, 20);
            g2.setColor(new Color(100, 100, 100, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String pauseText = "⏸ PAUSED";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(pauseText, 
                CallCreation.BOX_X + (CallCreation.BOX_W - fm.stringWidth(pauseText)) / 2,
                CallCreation.BOX_Y + CallCreation.BOX_H / 2);
        }
        
        if (showingResponse)
            y = CallCreation.drawResponse(g2, callerName, displayedResponse, y);
        CallCreation.drawTimerBar(g2, timerLeft, timerMax);
        if (timerRunning || chosenIndex != -1)
            CallCreation.drawChoices(g2, testChoiceLabels, chosenIndex, hoveredChoice, choiceRects);

        g2.dispose();
    }

    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
}