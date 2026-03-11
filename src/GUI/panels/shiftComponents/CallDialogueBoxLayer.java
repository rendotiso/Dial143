package GUI.panels.shiftComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallDialogueBoxLayer extends JPanel {

    private String   callerName          = "";
    private int      callNumber          = 0;
    private String[] testChoiceLabels    = {};
    private String[] testPlayerResponses = {};
    private String[] testCallerResponses = {};
    private int[]    testPP              = {};
    private int[]    testSalary          = {};

    private java.util.function.BiConsumer<Integer, Integer> onPointsAwarded;
    private Runnable onCallComplete;

    private int     timerMax     = 10;
    private int     timerLeft    = 10;
    private boolean timerRunning = false;
    private boolean timerPaused  = false;
    private Timer   countdownTimer, typewriterTimer, responseTimer, delayTimer;

    private String fullDesc      = "", displayedDesc = "";
    private int    typeIndex     = 0;

    private String  fullResponse      = "", displayedResponse = "";
    private int     responseIndex     = 0;
    private boolean showingResponse   = false;

    private boolean isLoaded    = false;
    private int     chosenIndex = -1;

    private final Rectangle[] choiceRects = new Rectangle[3];
    private int hoveredChoice = -1;

    private List<Integer> remainingCalls = new ArrayList<>();
    private int totalCalls = 5;

    private static final int RESPONSE_DELAY_MS = 800; 
    private static final int NEXT_CALL_DELAY_MS = 300; 

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
        resetRemainingCalls();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void onPointsAwarded(java.util.function.BiConsumer<Integer, Integer> callback) {
        this.onPointsAwarded = callback;
    }

    public void onCallComplete(Runnable callback) {
        this.onCallComplete = callback;
    }

    public void resetRemainingCalls() {
        remainingCalls.clear();
        for (int i = 0; i < totalCalls; i++) remainingCalls.add(i);
        Collections.shuffle(remainingCalls);
        callNumber = 0;
    }

    public boolean hasMoreCalls()        { return !remainingCalls.isEmpty(); }
    public int     getRemainingCallCount() { return remainingCalls.size(); }
    public int     getCompletedCallCount() { return totalCalls - remainingCalls.size(); }

    // ── Load next call ────────────────────────────────────────────────────────

    public void loadTest() {
        // Stop all timers first
        stopAll();
        
        // Cancel any pending delay timer
        if (delayTimer != null && delayTimer.isRunning()) {
            delayTimer.stop();
            delayTimer = null;
        }

        // ── Guard: pool empty — ShiftPanel's onCallComplete handles routing ──
        if (remainingCalls.isEmpty()) {
            if (onCallComplete != null) {
                // Use SwingUtilities to ensure this runs on EDT
                SwingUtilities.invokeLater(() -> onCallComplete.run());
            }
            return;
        }

        int callIndex = remainingCalls.remove(0);
        callNumber    = totalCalls - remainingCalls.size(); // 1-based

        // ── Call data ─────────────────────────────────────────────────────────
        String[] callerNames = {"Sarah", "Miguel", "Jenny", "Mr. Santos", "Anna"};

        String[] callDescriptions = {
            "Hi, I'm at NAIA Terminal 3 and my flight to Cebu was just cancelled. " +
                "Hay naku, I really need to get home to my kids, can you see what's available?",
            "I booked a hotel through your app but when I arrived, they said my reservation " +
                "was cancelled! I have proof of payment here.",
            "My baggage didn't arrive on the carousel. " +
                "I've been waiting for an hour and all the other bags are gone!",
            "I need to change my flight date because my mother was hospitalized. " +
                "The website won't let me modify it.",
            "The courier delivered my bag, but the lock is broken and my laptop is gone! " +
                "Nanalo na talaga ako sa malas! I want a refund!"
        };

        String[][] choiceSets = {
            {"Check flights",              "Ask for reference",                   "Transfer call"},
            {"Check alternative hotels",   "Offer full refund",                   "Transfer to manager"},
            {"File lost baggage report",   "Check tracking system",               "Offer compensation"},
            {"Waive change fee",           "Offer discount on new flight",         "Explain policy"},
            {"Escalate as theft incident", "Apologize and offer flight refund",    "Remind about no liability policy"}
        };

        String[][] playerResponseSets = {
            {"Let me check the next available flights to Cebu right away.",
             "Can I get your booking reference number first?",
             "Please hold while I transfer you to rebooking."},
            {"Let me check available hotels near your location.",
             "I'll process a full refund for your booking.",
             "I'll connect you to our customer relations team."},
            {"I'll file a lost baggage report immediately.",
             "Let me track your baggage using the reference number.",
             "I'll process compensation for the delay."},
            {"I'll waive the change fee for this emergency.",
             "I can offer 20% off your new booking.",
             "Let me explain our medical emergency policy."},
            {"I am filing an incident report for Theft in Transit immediately.",
             "Let me process a refund for your flight ticket.",
             "Our policy says we aren't liable for jewelry or electronics."}
        };

        String[][] callerResponseSets = {
            {"Thank you so much! I really appreciate your help.",
             "Oh yes, it's CEB-2024-887.",
             "Wait, no — I've been on hold for an hour already!"},
            {"Yes please, I need somewhere to stay tonight!",
             "Thank you, I just want my money back.",
             "Finally someone who can help!"},
            {"Please find my bag, it has my work laptop!",
             "The tracking number is CEB-123-456.",
             "This is so frustrating!"},
            {"Oh thank you so much! You're a lifesaver.",
             "That would really help with the expenses.",
             "I understand, but this is an emergency!"},
            {"You better! That laptop is for my work-from-home job!",
             "Uh... um okay. But the laptop is worth way more than the plane ticket...",
             "Bastos! You think I have time to read fine print when I'm rushing? I'm reporting this to the DTI!"}
        };

        int[][] ppSets = {
            {10,  5,  -5},
            {15, 10, -10},
            {20,  5, -15},
            {25, 15,  -5},
            {10,  5,  -5}
        };

        int[][] salarySets = {
            {500, 300,  100},
            {600, 400,   50},
            {700, 300, -100},
            {800, 500,  200},
            {500, 300,  100}
        };

        callerName          = callerNames[callIndex];
        fullDesc            = callerName + ":\n" + callDescriptions[callIndex];
        testChoiceLabels    = choiceSets[callIndex].clone();
        testPlayerResponses = playerResponseSets[callIndex].clone();
        testCallerResponses = callerResponseSets[callIndex].clone();
        testPP              = ppSets[callIndex].clone();
        testSalary          = salarySets[callIndex].clone();

        // Reset state
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

        startTypewriter();
        repaint();
    }

    // ── Timer controls ────────────────────────────────────────────────────────

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

    public boolean isTimerPaused() { return timerPaused; }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void startTypewriter() {
        // Stop any existing typewriter timer
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        
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
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        
        timerRunning   = true;
        timerPaused    = false;
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
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        if (responseTimer != null && responseTimer.isRunning()) {
            responseTimer.stop();
        }
        if (delayTimer != null && delayTimer.isRunning()) {
            delayTimer.stop();
            delayTimer = null;
        }
        timerRunning = false;
        timerPaused  = false;
    }

    private void selectChoice(int index) {
        if (chosenIndex != -1) return;
        
        stopAll();
        
        chosenIndex  = index;

        if (onPointsAwarded != null) {
            onPointsAwarded.accept(testPP[index], testSalary[index]);
        }

        fullResponse = "You: " + testPlayerResponses[index]
                     + "\n\n" + callerName + ": " + testCallerResponses[index]
                     + "\n\n+Performance Points: " + testPP[index]
                     + "   +Salary: ₱" + testSalary[index];

        displayedResponse = "";
        responseIndex     = 0;
        showingResponse   = true;

        responseTimer = new Timer(10, e -> { 
            if (responseIndex < fullResponse.length()) {
                displayedResponse = fullResponse.substring(0, ++responseIndex);
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
                
                if (delayTimer != null && delayTimer.isRunning()) {
                    delayTimer.stop();
                }
                
                delayTimer = new Timer(RESPONSE_DELAY_MS, de -> {
                    if (onCallComplete != null) {
                        SwingUtilities.invokeLater(() -> onCallComplete.run());
                    }
                    delayTimer = null;
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
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

    // ── Paint ─────────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isLoaded) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        CallCreation.drawBox(g2);
        int y = CallCreation.drawTitle(g2, callNumber, totalCalls);
        y = CallCreation.drawDivider(g2, y);
        y = CallCreation.drawDescription(g2, callerName, displayedDesc, y);

        if (timerPaused) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillRoundRect(CallCreation.BOX_X + 20, CallCreation.BOX_Y + 20,
                             CallCreation.BOX_W - 40, CallCreation.BOX_H - 40, 20, 20);
            g2.setColor(new Color(100, 100, 100, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String pauseText = "\u23F8 PAUSED";
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