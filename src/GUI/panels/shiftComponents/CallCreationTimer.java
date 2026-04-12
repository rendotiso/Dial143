package GUI.panels.shiftComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallCreationTimer extends JPanel {

    private String   callerName          = "";
    private int      callNumber          = 0;
    private String[] testChoiceLabels    = {};
    private String[] testPlayerResponses = {};
    private String[] testCallerResponses = {};
    private int[]    testPP              = {};
    private int[]    testSalary          = {};

    private java.util.function.BiConsumer<Integer, Integer> onPointsAwarded;
    private Runnable onCallComplete;

    // ── Timer ─────────────────────────────────────────────────────────────────
    private static final int BASE_TIMER = 5;

    private int     timerMax          = BASE_TIMER;
    private int     timerLeft         = BASE_TIMER;
    private int     timerBoostPending = 0;   // Queue for next call
    private boolean timerRunning      = false;
    private boolean timerPaused       = false;
    private Timer   countdownTimer, typewriterTimer, responseTimer, delayTimer;

    // ── Timer Boost Display ───────────────────────────────────────────────────
    private int timerBoostDisplay = 0;

    // ── Typewriter ────────────────────────────────────────────────────────────
    private String fullDesc      = "", displayedDesc = "";
    private int    typeIndex     = 0;

    // ── Response ──────────────────────────────────────────────────────────────
    private String  fullResponse      = "", displayedResponse = "";
    private int     responseIndex     = 0;
    private boolean showingResponse   = false;

    // ── State ─────────────────────────────────────────────────────────────────
    private boolean isLoaded    = false;
    private int     chosenIndex = -1;

    // ── Sticky Note hint ──────────────────────────────────────────────────────
    private int hintIndex = -1;

    private final Rectangle[] choiceRects = new Rectangle[3];
    private int hoveredChoice = -1;

    private List<Integer> remainingCalls = new ArrayList<>();
    private int totalCalls = 3;

    private static final int RESPONSE_DELAY_MS = 800;

    private Rectangle skipButtonRect;
    private int       hoveredSkip = -1;

    public CallCreationTimer() {
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

    public void onPointsAwarded(java.util.function.BiConsumer<Integer, Integer> cb) {
        this.onPointsAwarded = cb;
    }

    public void onCallComplete(Runnable cb) {
        this.onCallComplete = cb;
    }

    public void resetRemainingCalls() {
        remainingCalls.clear();
        for (int i = 0; i < totalCalls; i++) remainingCalls.add(i);
        Collections.shuffle(remainingCalls);
        callNumber = 0;
    }
public void applyTimerBoostToCurrentCall(int extraSeconds) {
    if (extraSeconds <= 0) return;
    if (!isLoaded) {
        timerBoostPending += extraSeconds;
        return;
    }
    if (chosenIndex == -1 && !showingResponse) {
        timerMax += extraSeconds;
        timerLeft += extraSeconds;
        timerBoostDisplay = extraSeconds;
        
        Timer fadeTimer = new Timer(2000, e -> {
            timerBoostDisplay = 0;
            repaint();
        });
        fadeTimer.setRepeats(false);
        fadeTimer.start();
        repaint();
    } else {
        timerBoostPending += extraSeconds;
    }
}

    public void applyTimerBoostForNextCall(int extraSeconds) {
        timerBoostPending += extraSeconds;
    }

    public void activateHint() {
        if (testPP == null || testPP.length == 0) { 
            hintIndex = -1; 
            return; 
        }
        int best = 0;
        for (int i = 1; i < testPP.length; i++) {
            if (testPP[i] > testPP[best]) best = i;
        }
        hintIndex = best;
        repaint();
    }

    public boolean hasMoreCalls()          { return !remainingCalls.isEmpty(); }
    public int     getRemainingCallCount() { return remainingCalls.size(); }
    public int     getCompletedCallCount() { return totalCalls - remainingCalls.size(); }
    public boolean isLoaded()              { return isLoaded; }
    public int     getTimerLeft()          { return timerLeft; }
    public int     getChosenIndex()        { return chosenIndex; }

    // ── Load next call ────────────────────────────────────────────────────────

    public void loadTest() {
        stopAll();
        if (delayTimer != null && delayTimer.isRunning()) { 
            delayTimer.stop(); 
            delayTimer = null; 
        }

        if (remainingCalls.isEmpty()) {
            if (onCallComplete != null) SwingUtilities.invokeLater(() -> onCallComplete.run());
            return;
        }

        int callIndex = remainingCalls.remove(0);
        callNumber    = totalCalls - remainingCalls.size();

        // ── Call data ─────────────────────────────────────────────────────────
        String[]   callerNames        = {"Sarah", "Miguel", "Jenny"};
        String[]   callDescriptions   = {
            "Hi, I'm at NAIA Terminal 3 and my flight to Cebu was just cancelled. " +
                "Hay naku, I really need to get home to my kids, can you see what's available?",
            "I booked a hotel through your app but when I arrived, they said my reservation " +
                "was cancelled! I have proof of payment here.",
            "My baggage didn't arrive on the carousel. " +
                "I've been waiting for an hour and all the other bags are gone!"
        };
        String[][] choiceSets         = {
            {"Check flights",             "Ask for reference",                  "Transfer call"},
            {"Check alternative hotels",  "Offer full refund",                  "Transfer to manager"},
            {"File lost baggage report",  "Check tracking system",              "Offer compensation"}
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
             "I'll process compensation for the delay."}
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
             "This is so frustrating!"}
        };
        int[][] ppSets     = { {10, 5, -5}, {15, 10, -10}, {20, 5, -15} };
        int[][] salarySets = { {500, 300, 100}, {600, 400, 50}, {700, 300, -100} };

        callerName          = callerNames[callIndex];
        fullDesc            = callerName + ":\n" + callDescriptions[callIndex];
        testChoiceLabels    = choiceSets[callIndex].clone();
        testPlayerResponses = playerResponseSets[callIndex].clone();
        testCallerResponses = callerResponseSets[callIndex].clone();
        testPP              = ppSets[callIndex].clone();
        testSalary          = salarySets[callIndex].clone();

        // ── Apply any pending timer boost ────────────────────────────────────
        timerMax = BASE_TIMER + timerBoostPending;
        timerLeft = timerMax;
        
        if (timerBoostPending > 0) {
            timerBoostDisplay = timerBoostPending;
            Timer fadeTimer = new Timer(2000, e -> {
                timerBoostDisplay = 0;
                repaint();
            });
            fadeTimer.setRepeats(false);
            fadeTimer.start();
        }
        
        timerBoostPending = 0;  // Clear the queue

        // Reset per-call state
        chosenIndex       = -1;
        hintIndex         = -1;
        showingResponse   = false;
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
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
            }
            if (typewriterTimer != null && typewriterTimer.isRunning()) {
                typewriterTimer.stop();
            }
            repaint();
        }
    }

    public void resumeTimer() {
        if (timerPaused) {
            timerPaused = false;
            
            if (typeIndex < fullDesc.length()) {
                startTypewriter();
            } else if (timerLeft > 0 && chosenIndex == -1 && !showingResponse) {
                startTimer();
            } else if (timerLeft <= 0 && chosenIndex == -1 && !showingResponse) {
                int randomChoice = (int) (Math.random() * testChoiceLabels.length);
                selectChoice(randomChoice);
            }
            repaint();
        }
    }

    public boolean isTimerPaused() { return timerPaused; }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void skipAllCalls() {
        stopAll();
        while (!remainingCalls.isEmpty()) remainingCalls.remove(0);
        if (chosenIndex == -1 && onPointsAwarded != null) {
            onPointsAwarded.accept(0, 0);
        }
        if (onCallComplete != null) SwingUtilities.invokeLater(() -> onCallComplete.run());
    }

    private void startTypewriter() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        typewriterTimer = new Timer(20, e -> {
            if (timerPaused) return;
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
        if (chosenIndex != -1 || showingResponse) return;
        if (countdownTimer != null && countdownTimer.isRunning()) countdownTimer.stop();
        
        timerRunning = true;
        timerPaused = false;
        
        countdownTimer = new Timer(1000, e -> {
            if (timerPaused) return;
            if (timerLeft > 0 && chosenIndex == -1 && !showingResponse) {
                timerLeft--;
                repaint();
                if (timerLeft <= 0) {
                    ((Timer) e.getSource()).stop();
                    timerRunning = false;
                    if (chosenIndex == -1 && testChoiceLabels.length > 0) {
                        int randomChoice = (int) (Math.random() * testChoiceLabels.length);
                        selectChoice(randomChoice);
                    }
                }
            } else if (chosenIndex != -1 || showingResponse) {
                ((Timer) e.getSource()).stop();
                timerRunning = false;
            }
        });
        countdownTimer.start();
    }

    private void stopAll() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        if (countdownTimer  != null && countdownTimer.isRunning())  countdownTimer.stop();
        if (responseTimer   != null && responseTimer.isRunning())   responseTimer.stop();
        if (delayTimer      != null && delayTimer.isRunning())      { delayTimer.stop(); delayTimer = null; }
        timerRunning = false;
        timerPaused  = false;
    }

    private void selectChoice(int index) {
        if (chosenIndex != -1) return;
        stopAll();
        chosenIndex = index;
        hintIndex   = -1;
        
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

        responseTimer = new Timer(15, e -> {
            if (responseIndex < fullResponse.length()) {
                displayedResponse = fullResponse.substring(0, ++responseIndex);
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
                if (delayTimer != null && delayTimer.isRunning()) delayTimer.stop();
                delayTimer = new Timer(RESPONSE_DELAY_MS, de -> {
                    if (onCallComplete != null) SwingUtilities.invokeLater(() -> onCallComplete.run());
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
        int prev     = hoveredChoice;
        int prevSkip = hoveredSkip;
        hoveredChoice = -1;
        hoveredSkip   = -1;
        if (skipButtonRect != null && skipButtonRect.contains(p) && chosenIndex == -1 && !showingResponse)
            hoveredSkip = 0;
        for (int i = 0; i < choiceRects.length; i++) {
            if (choiceRects[i] != null && choiceRects[i].contains(p)
                    && chosenIndex == -1 && !timerPaused && !showingResponse) {
                hoveredChoice = i;
                break;
            }
        }
        if (hoveredChoice != prev || hoveredSkip != prevSkip) repaint();
    }

    private void handleClick(Point p) {
        if (skipButtonRect != null && skipButtonRect.contains(p) && chosenIndex == -1 && !showingResponse) {
            skipAllCalls();
            return;
        }
        if (chosenIndex != -1 || timerPaused || showingResponse) return;
        for (int i = 0; i < choiceRects.length; i++) {
            if (choiceRects[i] != null && choiceRects[i].contains(p)) {
                selectChoice(i);
                return;
            }
        }
    }

    // ── Paint ─────────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isLoaded) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        CallCreationGUI.drawBox(g2);
        int y = CallCreationGUI.drawTitle(g2, callNumber, totalCalls);
        y = CallCreationGUI.drawDivider(g2, y);
        y = CallCreationGUI.drawDescription(g2, callerName, displayedDesc, y);

        if (!showingResponse && chosenIndex == -1)
            skipButtonRect = CallCreationGUI.drawSkipButton(g2, hoveredSkip == 0);

        if (timerPaused) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillRoundRect(CallCreationGUI.BOX_X + 20, CallCreationGUI.BOX_Y + 20,
                             CallCreationGUI.BOX_W - 40, CallCreationGUI.BOX_H - 40, 20, 20);
            g2.setColor(new Color(100, 100, 100, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String pauseText = "\u23F8 PAUSED";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(pauseText,
                CallCreationGUI.BOX_X + (CallCreationGUI.BOX_W - fm.stringWidth(pauseText)) / 2,
                CallCreationGUI.BOX_Y + CallCreationGUI.BOX_H / 2);
        }

        if (showingResponse)
            y = CallCreationGUI.drawResponse(g2, callerName, displayedResponse, y);

        // Draw timer bar with boost display
        CallCreationGUI.drawTimerBar(g2, timerLeft, timerMax, timerBoostDisplay);

        if (timerRunning && chosenIndex == -1 && !showingResponse)
            CallCreationGUI.drawChoices(g2, testChoiceLabels, chosenIndex,
                                     hoveredChoice, choiceRects, hintIndex);

        g2.dispose();
    }
}