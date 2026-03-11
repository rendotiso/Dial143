package GUI.panels;

import java.awt.CardLayout;
import Entities.Player;
import javax.swing.*;
import GUI.panels.universalComponents.TransitionLayer;

public class MainFrame extends javax.swing.JFrame {

    private CardLayout cardLayout;
    private JPanel     mainContainer;

    private TitleScreenPanel titlePanel;
    private InteractionPanel dialoguePanel;
    private SettingsPanel    settingsPanel;
    private ShiftPanel       shiftPanel;
    private ShopPanel        shopPanel;

    private TransitionLayer transitionLayer;
//    private DaySummary daySummary;

    // ── Player identity ───────────────────────────────────────────────────────
    private Player playerProfile = new Player();

    public Player getPlayerProfile() { return playerProfile; }

    public String getPlayerName() {
        return playerProfile.getName();
    }

    public String getPlayerGender() {
        return playerProfile.getGender();
    }

    public String getPlayerPronoun() {
        return playerProfile.getPronoun();
    }

    public void setPlayerIdentity(String name, String gender, String pronoun) {
        playerProfile.set(name, gender, pronoun);
        System.out.println("Identity set: " + name + " (" + gender + ", " + pronoun + ")");
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

    private int     dialogueScene = 0;
    private boolean dialogueDone  = false;

    public int     getDialogueScene()          { return dialogueScene; }
    public void    setDialogueScene(int scene) { dialogueScene = scene; }
    public boolean isDialogueDone()            { return dialogueDone; }
    public void    setDialogueDone(boolean v)  { dialogueDone = v; }

    public enum Segment { MORNING, EVENING, ENDING }
    private Segment currentSegment = Segment.MORNING;
    public Segment getCurrentSegment() { return currentSegment; }

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
    private String currentScreen = "title";

    public void showScreen(String screenName) {
        transitionLayer.fadeOut(() -> {
            currentScreen = screenName;
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

    /** InteractionPanel: morning scenes finished → start shift */
    public void onMorningComplete() {
        callsCompletedToday = 0;
        showScreen("shift");
    }

    /**
     * ShiftPanel: ALL calls finished (ShiftPanel handles per-call loop internally).
     * Go straight to evening interaction.
     */
    public void onCallComplete() {
        currentSegment = Segment.EVENING;
        dialogueScene  = 0;
        dialogueDone   = false;
        showScreen("dialogue");
    }

    /** InteractionPanel: evening scenes finished → show day summary then shop */
    public void onEveningComplete() {
//        daySummary.show(
//            currentDay,
//            sharedPP,
//            sharedLP,
//            sharedSalary,
//            callsCompletedToday,
//            () -> showScreen("shop")
//        );
    }

    /** ShopPanel: player done shopping → end day */
    public void onShopComplete() {
        onEndDay();
    }

    /**
     * End of day.
     * Only 1 day implemented — after day 1 goes to ending then title.
     * When more days are added, increment currentDay and loop back to morning.
     */
    public void onEndDay() {
        if (currentDay >= TOTAL_DAYS) {
            // No more days — play ending then return to title
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

    /** InteractionPanel: ending scenes finished → force back to title */
    public void onGameComplete() {
        resetStats();
        showScreen("title");
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    public MainFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        panelObjects();
        setupLayeredContent();
        cardLayout.show(mainContainer, "title");
    }

    public void panelObjects() {
        cardLayout    = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        titlePanel    = new TitleScreenPanel(this);
        settingsPanel = new SettingsPanel(this);
        dialoguePanel = new InteractionPanel(this, settingsPanel);
        shiftPanel    = new ShiftPanel(this, settingsPanel);
        shopPanel     = new ShopPanel(this, settingsPanel);

        // Popups — NOT added to CardLayout
//        daySummary    = new DaySummary(this);
        // identityPopup is created inside InteractionPanel when needed

        mainContainer.add(titlePanel,    "title");
        mainContainer.add(dialoguePanel, "dialogue");
        mainContainer.add(shiftPanel,    "shift");
        mainContainer.add(shopPanel,     "shop");
    }

    private void setupLayeredContent() {
        transitionLayer = new TransitionLayer();

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new java.awt.Dimension(1280, 720));

        mainContainer.setBounds(0, 0, 1280, 720);
        transitionLayer.setBounds(0, 0, 1280, 720);

        layered.add(mainContainer,   JLayeredPane.DEFAULT_LAYER);
        layered.add(transitionLayer, JLayeredPane.PALETTE_LAYER);

        setContentPane(layered);
    }
    
    
    
    
    // BELOW IS THE CODE GENERATURED FROM NETBEANS
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setLocation(new java.awt.Point(0, 0));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

