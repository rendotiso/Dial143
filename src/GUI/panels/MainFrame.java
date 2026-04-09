package GUI.panels;

import java.awt.*;
import java.awt.CardLayout;
import Entities.ActiveEffects;
import Entities.Player;
import javax.swing.*;
import GUI.panels.universalComponents.TransitionLayer;
import GUI.panels.InventoryPanel;
import Storyline.*;
import Storyline.AmayaRoute.AmayaRoute;

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
    private TransitionLayer  transitionLayer;
    private InventoryPanel   inventory;  
    private final RouteManager routeManager = new AmayaRoute();
    public RouteManager getRouteManager() { return routeManager; }
    
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

    
    private final ActiveEffects activeEffects = new ActiveEffects();
    public ActiveEffects getActiveEffects() { return activeEffects; }


    // ── Shared stats (PP and Salary are global) ───────────────────────────────
    private int sharedPP     = 0;
    private int sharedSalary = 0;

    public int  getPP()          { return sharedPP; }
    public int  getSalary()      { return sharedSalary; }
    public void setPP(int v)     { sharedPP = v; }
    public void setSalary(int v) { sharedSalary = v; }
    
    // ── LP delegates to RouteManager ──────────────────────────────────────────
    
    public int getLP() {
        if (routeManager.hasActiveRoute()) {
            return routeManager.getActiveLP();
        }
        return 0;
    }
    
    public void setLP(int v) {
        if (routeManager.hasActiveRoute()) {
            String activeChar = routeManager.getActiveCharacter();
            int current = routeManager.getLPForCharacter(activeChar);
            int diff = v - current;
            if (diff != 0) {
                routeManager.addCharacterLP(activeChar, diff);
            }
        }
    }
    
    public void addLP(int amount) {
        if (routeManager.hasActiveRoute()) {
            routeManager.addCharacterLP(routeManager.getActiveCharacter(), amount);
        }
    }
   
    public int getLPForCharacter(String character) {
        return routeManager.getLPForCharacter(character);
    }
    public void setLPForCharacter(String character, int value) {
        int current = routeManager.getLPForCharacter(character);
        int diff = value - current;
        if (diff != 0) {
            routeManager.addCharacterLP(character, diff);
        }
    }

    // ── Day / call progress ───────────────────────────────────────────────────
    private int currentDay          = 1;
    private int callsCompletedToday = 0;
    public static final int CALLS_PER_DAY = 5;
    public static final int TOTAL_DAYS    = 7;

    public int  getCurrentDay()           { return currentDay; }
    public void setCurrentDay(int day)    { this.currentDay = day; }
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
    public Segment  getCurrentSegment()         { return currentSegment; }
    public void     setCurrentSegment(Segment s){ currentSegment = s; }
    public SavePanel getSavePanel()             { return savePanel; }
    public InventoryPanel getInventory()        { return inventory; }

    // ── Reset ─────────────────────────────────────────────────────────────────
    public void resetStats() {
        sharedPP            = 0;
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
        routeManager.reset();
        activeEffects.reset();
        if (inventory != null) inventory.clear(); 
    }

    // ── Screen routing ────────────────────────────────────────────────────────
public void showScreen(String screenName) {
    if (transitionLayer.isTransitioning()) return;
    
    transitionLayer.fadeOut(() -> {
        cardLayout.show(mainContainer, screenName);
        SwingUtilities.invokeLater(() -> {
            switch (screenName) {
                case "dialogue" -> dialoguePanel.loadContent();
                case "shift"    -> shiftPanel.loadCall();
                case "shop"     -> shopPanel.loadShop();
                case "save"     -> {}
                case "summary"  -> daySummary.loadSummary(
                    sharedPP     - ppAtShiftStart,
                    getLP()      - lpAtShiftStart,
                    sharedSalary - salaryAtShiftStart,
                    callsCompletedToday,
                    () -> onEndDay(),
                    () -> showScreen("shop")
                );
            }
            Timer timer = new Timer(50, e -> transitionLayer.fadeIn());
            timer.setRepeats(false);
            timer.start();
        });
    });
}

    // ── Game flow callbacks ───────────────────────────────────────────────────

    public void onMorningComplete() {
        ppAtShiftStart      = sharedPP;
        lpAtShiftStart      = getLP();
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
        showScreen("summary");
    }

    public void onShopComplete() {
        showScreen("summary");
    }

    public void onEndDay() {
        if (currentDay >= TOTAL_DAYS) {
            currentSegment = Segment.ENDING;
            dialogueScene  = 0;
            dialogueDone   = false;
            showScreen("dialogue");
        } else {
            activeEffects.resetPerDay();
            currentDay++;
            callsCompletedToday = 0;
            dialogueScene       = 0;
            dialogueDone        = false;
            currentSegment      = Segment.MORNING;
            ppAtShiftStart      = sharedPP;
            lpAtShiftStart      = getLP();
            salaryAtShiftStart  = sharedSalary;
            dialoguePanel.setDayScript(scriptForDay(currentDay));
            showScreen("dialogue");
        }
    }

    public void onGameComplete() {
        resetStats();
        showScreen("title");
    }


    public DayInterface scriptForDay(int day) {
        if (day == 1) return new Day1();
        if (day == 2) return new Day2();
        if (day == 3) return new Day3();

        if (routeManager.hasActiveRoute()) {
            return routeManager.getActiveRoute().getDayScript(day);
        }

        return new Day3();
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    public MainFrame() {
        setTitle("Dial 143");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(1280, 720));

        // Initialize inventory FIRST
        inventory = new InventoryPanel(this);
        
        panelObjects();
        setupLayeredContent();

        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
        cardLayout.show(mainContainer, "title");
    }

    private void panelObjects() {
        cardLayout    = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        titlePanel    = new TitleScreenPanel(this);
        settingsPanel = new SettingsPanel(this);
        // Pass the SAME inventory instance to all panels
        dialoguePanel = new InteractionPanel(this, settingsPanel, inventory);
        shiftPanel    = new ShiftPanel(this, settingsPanel, inventory);
        shopPanel     = new ShopPanel(this, settingsPanel, inventory);
        daySummary    = new DaySummaryPanel(this, settingsPanel, inventory);
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