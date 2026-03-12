package GUI.panels;

import java.awt.*;
import java.awt.CardLayout;
import Entities.Player;
import javax.swing.*;
import GUI.panels.universalComponents.TransitionLayer;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel     mainContainer;

    private TitleScreenPanel titlePanel;
    private InteractionPanel dialoguePanel;
    private SettingsPanel    settingsPanel;
    private ShiftPanel       shiftPanel;
    private ShopPanel        shopPanel;

    private TransitionLayer transitionLayer;
//    private DaySummaryPanel daySummary;

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
    public static final int TOTAL_DAYS    = 1;

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
    public Segment getCurrentSegment() { return currentSegment; }

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
    }

    // ── Screen routing ────────────────────────────────────────────────────────
    public void showScreen(String screenName) {
        transitionLayer.fadeOut(() -> {
            cardLayout.show(mainContainer, screenName);
            switch (screenName) {
                case "dialogue" -> dialoguePanel.loadContent();
                case "shift"    -> shiftPanel.loadCall();
//                case "shop"     -> shopPanel.loadShop();
            }
            transitionLayer.fadeIn();
        });
    }

    // ── Game flow callbacks ───────────────────────────────────────────────────

    public void onMorningComplete() {
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
//        daySummary.show(currentDay, sharedPP, sharedLP, sharedSalary,
//            callsCompletedToday, () -> showScreen("shop"));
    }

    public void onShopComplete() {
        onEndDay();
    }

    public void onEndDay() {
        if (currentDay >= TOTAL_DAYS) {
            currentSegment = Segment.ENDING;
            dialogueScene  = 0;
            dialogueDone   = false;
            showScreen("dialogue");
        } else {
            currentDay++;
            callsCompletedToday = 0;
            dialogueScene       = 0;
            dialogueDone        = false;
            currentSegment      = Segment.MORNING;
            showScreen("dialogue");
        }
    }

    public void onGameComplete() {
        resetStats();
        showScreen("title");
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

        mainContainer.add(titlePanel,    "title");
        mainContainer.add(dialoguePanel, "dialogue");
        mainContainer.add(shiftPanel,    "shift");
        mainContainer.add(shopPanel,     "shop");
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
}