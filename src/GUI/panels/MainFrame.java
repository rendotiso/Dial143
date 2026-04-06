package GUI.panels;

import java.awt.*;
import java.awt.CardLayout;
import Entities.Player;
import javax.swing.*;
import GUI.panels.universalComponents.TransitionLayer;
import Storyline.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel     mainContainer;

    private TitleScreenPanel titlePanel;
    private InteractionPanel dialoguePanel;
    private SettingsPanel    settingsPanel;
    private ShiftPanel       shiftPanel;
    private ShopPanel        shopPanel;
    private DaySummaryPanel  daySummary;
    private SavePanel        savePanel;
    private TransitionLayer transitionLayer;

    // ── Shift start snapshots (for "gained this shift" calculation) ───────────
    private int ppAtShiftStart;
    private int lpAtShiftStart;
    private int salaryAtShiftStart;

    // ── Player identity ───────────────────────────────────────────────────────
    private Player playerProfile = new Player();

    public Player  getPlayerProfile() { return playerProfile; }
    public String  getPlayerName()    { return playerProfile.getName(); }
    public String  getPlayerGender()  { return playerProfile.getGender(); }
    public String  getPlayerPronoun() { return playerProfile.getPronoun(); }

    public void setPlayerIdentity(String name, String gender, String pronoun) {
        playerProfile.set(name, gender, pronoun);
    }

    // ── Shared stats ──────────────────────────────────────────────────────────
    private int sharedPP     = 0;
    private int sharedLP     = 0;
    private int sharedSalary = 0;

    public int  getPP()          { return sharedPP; }
    public int  getLP()          { return sharedLP; }
    public int  getSalary()      { return sharedSalary; }
    public void setPP(int v)     { sharedPP     = v; }
    public void setLP(int v)     { sharedLP     = v; }
    public void setSalary(int v) { sharedSalary = v; }

    // ── Day / call progress ───────────────────────────────────────────────────
    private int currentDay          = 1;
    private int callsCompletedToday = 0;
    public static final int CALLS_PER_DAY = 5;
    public static final int TOTAL_DAYS    = 7;

    public int  getCurrentDay()           { return currentDay; }
    public int  getCallsCompleted()       { return callsCompletedToday; }
    public void incrementCallsCompleted() { callsCompletedToday++; }

    // ── Dialogue scene progress ───────────────────────────────────────────────
    private int     dialogueScene = 0;
    private boolean dialogueDone  = false;

    public int     getDialogueScene()          { return dialogueScene; }
    public void    setDialogueScene(int scene) { dialogueScene = scene; }
    public boolean isDialogueDone()            { return dialogueDone; }
    public void    setDialogueDone(boolean v)  { dialogueDone = v; }

    // ── Segment tracking ──────────────────────────────────────────────────────
    public enum Segment { MORNING, EVENING, ENDING }
    private Segment currentSegment = Segment.MORNING;
    public Segment getCurrentSegment()      { return currentSegment; }
    public void    setCurrentSegment(Segment s) { currentSegment = s; }
    public SavePanel getSavePanel()         { return savePanel; }

    // ── Reset ─────────────────────────────────────────────────────────────────
    public void resetStats() {
        sharedPP            = 0;
        sharedLP            = 0;
        sharedSalary        = 0;
        currentDay          = 1;
        callsCompletedToday = 0;
        dialogueScene       = 0;
        dialogueDone        = false;
        currentSegment      = Segment.MORNING;
        playerProfile       = new Player();
        ppAtShiftStart      = 0;
        lpAtShiftStart      = 0;
        salaryAtShiftStart  = 0;
    }

    // ── Screen routing ────────────────────────────────────────────────────────
    public void showScreen(String screenName) {
        transitionLayer.fadeOut(() -> {
            cardLayout.show(mainContainer, screenName);
            switch (screenName) {
                case "dialogue" -> dialoguePanel.loadContent();
                case "shift"    -> shiftPanel.loadCall();
                case "shop"     -> shopPanel.loadShop();
                case "save"     -> {} // use showSave(returnScreen) instead
                case "summary"  -> daySummary.loadSummary(
                    // CORRECT ORDER: ppGained, lpGained, salaryGained, callsDone
                    sharedPP - ppAtShiftStart,      // pp gained this shift
                    sharedLP - lpAtShiftStart,      // lp gained this shift  
                    sharedSalary - salaryAtShiftStart, // salary gained this shift
                    callsCompletedToday,
                    () -> onEndDay(),
                    () -> showScreen("shop")
                );
            }
            transitionLayer.fadeIn();
        });
    }
    // ── Game flow callbacks ───────────────────────────────────────────────────

    public void onMorningComplete() {
        ppAtShiftStart      = sharedPP;
        lpAtShiftStart      = sharedLP;
        salaryAtShiftStart  = sharedSalary;
        callsCompletedToday = 0;
        showScreen("shift");
    }

    public void onCallComplete() {
        currentSegment = Segment.EVENING;
        dialogueScene  = 0;
        dialogueDone   = false;
        showScreen("dialogue");
    }

    public void onEveningComplete() {
        showDaySummary();
    }

    private void showDaySummary() {
        showScreen("summary");
    }

    public void onShopComplete() {
        showDaySummary();
    }

    public void onEndDay() {
        if (currentDay >= TOTAL_DAYS) {
            // Final day done — play ending
            currentSegment = Segment.ENDING;
            dialogueScene = 0;
            dialogueDone = false;
            showScreen("dialogue");
        } else {
            // Advance to the next day
            currentDay++;
            callsCompletedToday = 0;
            dialogueScene = 0;
            dialogueDone = false;
            currentSegment = Segment.MORNING;

            // Reset shift start snapshots for the new day
            ppAtShiftStart = sharedPP;
            lpAtShiftStart = sharedLP;
            salaryAtShiftStart = sharedSalary;

            // Load the correct day script
            dialoguePanel.setDayScript(scriptForDay(currentDay));
            showScreen("dialogue");
        }
    }

    public void onGameComplete() {
        resetStats();
        showScreen("title");
    }

    /**
     * Returns the correct DayScript for a given day number.
     * Add a new case here whenever you add a new DayNScript class.
     */
    private DayInterface scriptForDay(int day) {
        return switch (day) {
            case 1  -> new Day1();
            case 2  -> new Day2();
            // case 3  -> new Day3();
            default -> new Day1(); // fallback
        };
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    public MainFrame() {
        setTitle("Dial 143");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(1280, 720));

        panelObjects();
        setupLayeredContent();

        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
        cardLayout.show(mainContainer, "title");
    }

    // ── Panel setup ───────────────────────────────────────────────────────────

    private void panelObjects() {
        cardLayout    = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        titlePanel    = new TitleScreenPanel(this);
        settingsPanel = new SettingsPanel(this);
        dialoguePanel = new InteractionPanel(this, settingsPanel);
        shiftPanel    = new ShiftPanel(this, settingsPanel);
        shopPanel     = new ShopPanel(this, settingsPanel);
        daySummary    = new DaySummaryPanel(this, settingsPanel);
        savePanel     = new SavePanel(this);

        mainContainer.add(titlePanel,    "title");
        mainContainer.add(dialoguePanel, "dialogue");
        mainContainer.add(shiftPanel,    "shift");
        mainContainer.add(shopPanel,     "shop");
        mainContainer.add(daySummary,    "summary");
        mainContainer.add(savePanel,     "save");
    }

    private void setupLayeredContent() {
        transitionLayer = new TransitionLayer();

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(1280, 720));

        mainContainer.setBounds(0, 0, 1280, 720);
        transitionLayer.setBounds(0, 0, 1280, 720);

        layered.add(mainContainer,   JLayeredPane.DEFAULT_LAYER);
        layered.add(transitionLayer, JLayeredPane.PALETTE_LAYER);

        setContentPane(layered);
    }

    public void showSave(String returnScreen) {
        transitionLayer.fadeOut(() -> {
            cardLayout.show(mainContainer, "save");
            savePanel.loadSave(returnScreen);
            transitionLayer.fadeIn();
        });
    }
}