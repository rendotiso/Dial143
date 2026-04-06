package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.dialogueComponents.*;
import GUI.panels.inventoryComponents.*;
import Storyline.*;
import Storyline.AmayaRoute.AmayaRoute;
import Entities.Character;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 
public class InteractionPanel extends JPanel implements SceneManager.SceneManagerDelegate {
 
    private MainFrame             mainPanel;
    private BackgroundLayer       bg;
    private SpriteLayer           sprite;
    private DialogueBoxLayer      dialogueBox;
    private TopBarComponents      uiComponents;
    private ChoiceButtonLayer     choices;
    private SettingsPanel         settings;
    private IdentityCreationLayer identityPopup;
 
    private boolean showingIdentityCreation = false;
    private boolean showingRouteSelection   = false;
 
    private Thread       spriteThread;
    private Thread       bgThread;
    private SceneManager sceneManager;
 
    public InteractionPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        initComponents();
        initializeLayers();
        sceneManager = new SceneManager(new Day1(), this, mainPanel.getRouteManager());
    }
 
    // ── SceneManagerDelegate ──────────────────────────────────────────────────
 
    @Override
    public void waitForPreload() {
        try {
            if (spriteThread != null) spriteThread.join();
            if (bgThread     != null) bgThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
 
    @Override
    public void showBackground(String filename) {
        if (filename != null && !filename.isEmpty()) bg.setBackgroundFromFile(filename);
        else bg.setBackgroundColor(new Color(20, 30, 40));
    }
 
@Override
public void showSprite(String spriteSpec) {
    if (spriteSpec == null || spriteSpec.equals("none")) {
        sprite.hideAllSprites();
    } else if (spriteSpec.startsWith("single:")) {
        sprite.showSingleSprite(spriteSpec.substring(7));
    } else if (spriteSpec.startsWith("double:")) {
        String rest = spriteSpec.substring(7);
        String[] p  = rest.split(":");
        if (p.length >= 2) sprite.showTwoSprites(p[0], p[1]);
    } else if (spriteSpec.startsWith("triple:")) {
        String rest = spriteSpec.substring(7);
        String[] p  = rest.split(":");
        if (p.length >= 3) sprite.showThreeSprites(p[0], p[1], p[2]);
    } else if (spriteSpec.startsWith("quadruple:")) {
        String rest = spriteSpec.substring(10);
        String[] p  = rest.split(":");
        if (p.length >= 4) sprite.showFourSprites(p[0], p[1], p[2], p[3]);
    } else {
        sprite.showSingleSprite(spriteSpec);
    }
    // DO NOT hide choices here — choices visibility is managed by
    // showChoices() and the choice listener only
    revalidate();
    repaint();
}
 
    @Override
    public void showDialogue(String speaker, String text) {
        choices.hideChoices();   // ← moved here
        dialogueBox.setSpeaker(resolvePlaceholders(speaker));
        dialogueBox.setDialogue(resolvePlaceholders(text));
    }

    @Override
    public void showNarrator(String text) {
        choices.hideChoices();   // ← moved here
        dialogueBox.setSpeaker("Narrator");
        dialogueBox.setDialogue(resolvePlaceholders(text));
    }
 
    @Override
    public void showChoices(ChoiceEntry[] choiceEntries, Runnable onChosen) {
        choices.clearChoices();
        for (ChoiceEntry entry : choiceEntries) {
            choices.addChoice(entry.label, entry.label, true);
        }
        choices.setChoiceListener((choiceText, nextNode) -> {
            for (ChoiceEntry entry : choiceEntries) {
                if (entry.label.equals(choiceText)) {
                    choices.hideChoices();
                    sceneManager.onChoicePicked(entry);
                    break;
                }
            }
        });
        choices.showChoices();
    }
 
    @Override
    public void showRouteSelection() {
        showingRouteSelection = true;
        RouteManager rm = mainPanel.getRouteManager();

        boolean amayaOk   = rm.isRouteUnlocked(Character.AMAYA);
        boolean celeresOk = rm.isRouteUnlocked(Character.CELERES);
        boolean clomaOk   = rm.isRouteUnlocked(Character.CLOMA);
        boolean rosarioOk = rm.isRouteUnlocked(Character.ROSARIO);
        boolean allLocked = rm.allRoutesLocked();

        choices.clearChoices();

        if (allLocked) {
            choices.addChoice("No one - Walk alone", "none", true);
        } else {
            choices.addChoice("Amaya", Character.AMAYA, amayaOk);
            choices.addChoice("Celeres", Character.CELERES, celeresOk);
            choices.addChoice("Cloma", Character.CLOMA, clomaOk);
            choices.addChoice("Rosario", Character.ROSARIO, rosarioOk);
        }

        choices.setChoiceListener((choiceText, characterId) -> {
            showingRouteSelection = false;
            choices.hideChoices();
            handleRouteChoice(characterId);
        });

        choices.showChoices();
    }
 
    @Override
    public void showIdentityCreation(Runnable onComplete) {
        showingIdentityCreation = true;
        SwingUtilities.invokeLater(() -> {
            identityPopup.reset();
            identityPopup.setOnComplete(() -> {
                showingIdentityCreation = false;
                onComplete.run();
            });
            identityPopup.showAsPopup();
        });
    }
 
    @Override
    public void addPP(int amount) {
        uiComponents.addPpPoints(amount);
        mainPanel.setPP(mainPanel.getPP() + amount);
    }
 
        @Override
        public void addLP(int amount, String lpCharacter) {
            RouteManager rm = mainPanel.getRouteManager();

            if (lpCharacter != null) {
                // Always add LP to the character's pool, even before route is active
                rm.addCharacterLP(lpCharacter, amount);

                // Only update the UI display if this character is the active route
                if (rm.hasActiveRoute() && lpCharacter.equals(rm.getActiveCharacter())) {
                    uiComponents.addLpPoints(amount);
                } else if (!rm.hasActiveRoute()) {
                    // Before route selection, we don't show LP in the UI
                    // But we still track it internally
                    System.out.println("Added " + amount + " LP to " + lpCharacter + " (Total: " + rm.getLPForCharacter(lpCharacter) + ")");
                }
            } else if (rm.hasActiveRoute()) {
                // No character specified, add to active route
                rm.addCharacterLP(rm.getActiveCharacter(), amount);
                uiComponents.addLpPoints(amount);
            }
        }
 
    @Override
    public void onMorningComplete() {
        saveStats();
        mainPanel.onMorningComplete();
    }
 
    @Override
    public void onEveningComplete() {
        saveStats();
        mainPanel.onEveningComplete();
    }
 
    // ── Route choice handling ─────────────────────────────────────────────────
 private void handleRouteChoice(String characterId) {
    if ("none".equals(characterId)) {
        // No route — queue a short ending and finish normally
        ChoiceEntry noOne = new ChoiceEntry("No one", 0, 0,
            SceneEntry.dialogue("{name}", "No. I shouldn't be crushing on anyone.", "none", "StreetEvening.jpg"),
            SceneEntry.dialogue("{name}", "Focus. That's all that matters.", "none", "StreetEvening.jpg"),
            SceneEntry.narrator("Some calls... are meant to be unanswered.", "blackBG.jpg")
        );
        sceneManager.onChoicePicked(noOne);
        return;
    }

    RouteManager rm   = mainPanel.getRouteManager();
    RouteManager route = routeForCharacter(characterId);

    // Register the route and get the Day 3 route-specific script
    DayInterface day3RouteScript = rm.selectRoute(route, characterId);

    // Sync accumulated LP to the top bar now that route is active
    int accLP = rm.getLPForCharacter(characterId);
    uiComponents.setLpValue(accLP);

    // Show LP meter
    uiComponents.updateLoveMeterVisibility(
        mainPanel.getCurrentDay(), MainFrame.Segment.EVENING);

    // Hand the route's Day 3 script to the scene manager and restart
    // EVENING from index 0 — this replaces the shared Day3 evening entirely
    sceneManager.setDayScript(day3RouteScript);
    sceneManager.start(SceneManager.Segment.EVENING, 0);
}
// Helper method to build the scene array for route continuation
private SceneEntry[] buildRouteEveningScenes(SceneEntry[] scenes) {
    // Create a list to hold all scenes
    java.util.ArrayList<SceneEntry> allScenes = new java.util.ArrayList<>();
    
    // Add all the route-specific evening scenes
    for (SceneEntry scene : scenes) {
        allScenes.add(scene);
    }
    
    // Add a special marker scene that will trigger evening completion
    // This scene doesn't show anything but signals the scene manager
    allScenes.add(SceneEntry.narrator("", ""));
    
    return allScenes.toArray(new SceneEntry[0]);
}
 
    private RouteManager routeForCharacter(String name) {
        return switch (name) {
            case Character.AMAYA -> new AmayaRoute();
            // case Character.CELERES -> new CeleresRoute();
            // case Character.CLOMA -> new ClomaRoute();
            // case Character.ROSARIO -> new RosarioRoute();
            default -> new AmayaRoute();
        };
    }
 
    // ── Public API ────────────────────────────────────────────────────────────
 
    public void loadContent() {
        uiComponents.setPpValue(mainPanel.getPP());
        uiComponents.setSalaryValue(mainPanel.getSalary());
 
        RouteManager rm = mainPanel.getRouteManager();
        uiComponents.setLpValue(rm.hasActiveRoute() ? rm.getActiveLP() : 0);
        uiComponents.updateDayLabel(mainPanel.getCurrentDay(), mainPanel.getCurrentSegment());
 
        SceneManager.Segment seg = switch (mainPanel.getCurrentSegment()) {
            case MORNING -> SceneManager.Segment.MORNING;
            case EVENING -> SceneManager.Segment.EVENING;
            case ENDING  -> { mainPanel.onGameComplete(); yield SceneManager.Segment.MORNING; }
        };
 
        if (mainPanel.getCurrentSegment() != MainFrame.Segment.ENDING) {
            sceneManager.start(seg, mainPanel.getDialogueScene());
        }
    }
 
    public void setDayScript(DayInterface script) {
        sceneManager.setDayScript(script);
    }
    
    public void setRouteManager(RouteManager rm) {
        sceneManager.setRouteManager(rm);
    }
 
    // ── Helpers ───────────────────────────────────────────────────────────────
 
    private String resolvePlaceholders(String text) {
        if (text == null) return "";
        String name     = mainPanel.getPlayerName();
        String pronoun  = mainPanel.getPlayerPronoun();
        String subject    = "they", possessive = "their", objective = "them";
        if (pronoun != null && !pronoun.isEmpty()) {
            String[] p = pronoun.split("/");
            subject    = p.length > 0 ? p[0] : "they";
            possessive = p.length > 1 ? p[1] : "their";
            objective  = p.length > 2 ? p[2] : "them";
        }
        return text
            .replace("{name}",               name != null ? name : "")
            .replace("{pronoun_subject}",    subject)
            .replace("{pronoun_possessive}", possessive)
            .replace("{pronoun_objective}",  objective);
    }
 
    private void saveStats() {
        mainPanel.setPP(uiComponents.getCurrentPpValue());
        mainPanel.setSalary(uiComponents.getCurrentSalaryValue());
    }
 
    // ── Layer setup ───────────────────────────────────────────────────────────
 
    private void initializeLayers() {
        bg          = new BackgroundLayer();
        sprite      = new SpriteLayer();
        dialogueBox = new DialogueBoxLayer(mainPanel);
 
        uiComponents = new TopBarComponents(mainPanel);
        uiComponents.setSettingsPanel(settings);
        uiComponents.setParentScreen("dialogue");
 
        InventoryPanel inventory = new InventoryPanel(mainPanel);
        uiComponents.setInventoryPanel(inventory);
        uiComponents.onSettingsClosed(() -> requestFocusInWindow());
 
        identityPopup = new IdentityCreationLayer(mainPanel);
        choices       = new ChoiceButtonLayer();
 
        add(choices);
        add(uiComponents);
        add(dialogueBox);
        add(sprite);
        add(bg);
 
        spriteThread = new Thread(() -> {
            sprite.addSprite("Amaya_Smile",      "Amaya_Smile.png");
            sprite.addSprite("Amaya_Casual",     "Amaya_Casual.png");
            sprite.addSprite("Amaya_Sad",        "Amaya_Sad.png");
            sprite.addSprite("Amaya_Mad",        "Amaya_Mad.png");
            sprite.addSprite("Amaya_Blushing",   "Amaya_Blushing.png");
            sprite.addSprite("Rosario_Smile",    "Rosario_Smile.png");
            sprite.addSprite("Rosario_Casual",   "Rosario_Casual.png");
            sprite.addSprite("Rosario_Sad",      "Rosario_Sad.png");
            sprite.addSprite("Rosario_Mad",      "Rosario_Mad.png");
            sprite.addSprite("Rosario_Blushing", "Rosario_Blushing.png");
            sprite.addSprite("Celeres_Smile",    "Celeres_Smile.png");
            sprite.addSprite("Celeres_Casual",   "Celeres_Casual.png");
            sprite.addSprite("Celeres_Sad",      "Celeres_Sad.png");
            sprite.addSprite("Celeres_Mad",      "Celeres_Mad.png");
            sprite.addSprite("Celeres_Blushing", "Celeres_Blushing.png");
            sprite.addSprite("Cloma_Smile",      "Cloma_Smile.png");
            sprite.addSprite("Cloma_Casual",     "Cloma_Casual.png");
            sprite.addSprite("Cloma_Sad",        "Cloma_Sad.png");
            sprite.addSprite("Cloma_Mad",        "Cloma_Mad.png");
            sprite.addSprite("Cloma_Blushing",   "Cloma_Blushing.png");
        }, "sprite-preload");
 
        bgThread = new Thread(() ->
            bg.preload("MorningOffice.jpg", "EveningOffice.jpg", "BuildingEvening.jpg",
                       "BuildingMorning.jpg", "ConvenienceStore.jpg",
                       "StreetMorning.jpg", "StreetEvening.jpg",
                       "MorningElevator.jpg", "BreakRoom.jpg", "blackBG.jpg"),
            "bg-preload"
        );
 
        spriteThread.setDaemon(true);
        bgThread.setDaemon(true);
        spriteThread.start();
        bgThread.start();
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

        setPreferredSize(new java.awt.Dimension(1280, 720));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
            if (!choices.isVisible() && !showingIdentityCreation) {
                sceneManager.advanceScene();
            }
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
