package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.dialogueComponents.*;
import GUI.panels.InventoryPanel;
import Storyline.*;
import Storyline.AmayaRoute.AmayaRoute;
import Entities.Character;
import Entities.*;
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
    private InventoryPanel        inventory;
    private IdentityCreationLayer identityPopup;
    private ItemUse               itemUse;
 
    private boolean showingIdentityCreation = false;
    private boolean showingRouteSelection   = false;
    private boolean dialogueAdvanceLock     = false;  
 
    private Thread       spriteThread;
    private Thread       bgThread;
    private SceneManager sceneManager;
 
    public InteractionPanel(MainFrame mainPanel, SettingsPanel sharedSettings, InventoryPanel inventory) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        this.inventory = inventory;
        initComponents();
        initializeLayers();
        sceneManager = new SceneManager(new Day1(), this, mainPanel.getRouteManager());
    }
 
    public ItemUse getItemUse() { 
        return itemUse; 
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
        if (filename != null && !filename.isEmpty()) {
            bg.setBackgroundFromFile(filename);
        } else {
            bg.setBackgroundColor(new Color(20, 30, 40));
        }
    }
 
    @Override
    public void showSprite(String spriteSpec) {
        if (spriteSpec == null || spriteSpec.equals("none")) {
            sprite.hideAllSprites();
        } else if (spriteSpec.startsWith("single:")) {
            String spriteName = spriteSpec.substring(7);
            sprite.showSingleSprite(spriteName);
        } else if (spriteSpec.startsWith("double:")) {
            String rest = spriteSpec.substring(7);
            String[] p  = rest.split(":");
            if (p.length >= 2) {
                System.out.println("  Double sprite: " + p[0] + ", " + p[1]);
                sprite.showTwoSprites(p[0], p[1]);
            }
        } else {
            sprite.showSingleSprite(spriteSpec);
        }
        revalidate();
        repaint();
    }
 
    @Override
    public void showDialogue(String speaker, String text) {
        dialogueAdvanceLock = false;
        choices.hideChoices();
        dialogueBox.onDialogueShown();
        dialogueBox.setSpeaker(resolvePlaceholders(speaker));
        dialogueBox.setDialogue(resolvePlaceholders(text));
    }
 
    @Override
    public void showNarrator(String text) {
        dialogueAdvanceLock = false;
        choices.hideChoices();
        dialogueBox.onDialogueShown();
        dialogueBox.setSpeaker("Narrator");
        dialogueBox.setDialogue(resolvePlaceholders(text));
    }
 
    @Override
    public void showChoices(ChoiceEntry[] choiceEntries, Runnable onChosen) {
        for (int i = 0; i < choiceEntries.length; i++) {
            System.out.println("  Choice " + (i+1) + ": " + choiceEntries[i].label);
        }
        
        dialogueAdvanceLock = true;
        dialogueBox.onChoicesShown();
        choices.clearChoices();
        
        for (ChoiceEntry entry : choiceEntries) {
            choices.addChoice(entry.label, entry.label, true);
        }
        
        choices.setChoiceListener((choiceText, nextNode) -> {
            dialogueAdvanceLock = false;
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
        dialogueAdvanceLock = true;
        dialogueBox.onChoicesShown();
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
            choices.addChoice("Amaya",   Character.AMAYA,   amayaOk);
            choices.addChoice("Celeres", Character.CELERES, celeresOk);
            choices.addChoice("Cloma",   Character.CLOMA,   clomaOk);
            choices.addChoice("Rosario", Character.ROSARIO, rosarioOk);
        }
        
        choices.setChoiceListener((choiceText, characterId) -> {
            showingRouteSelection = false;
            dialogueAdvanceLock = false;
            choices.hideChoices();
            handleRouteChoice(characterId);
        });
        choices.showChoices();
    }
 
    @Override
    public void showIdentityCreation(Runnable onComplete) {
        dialogueAdvanceLock = true;
        dialogueBox.onChoicesShown();
        showingIdentityCreation = true;
        
        SwingUtilities.invokeLater(() -> {
            identityPopup.reset();
            identityPopup.setOnComplete(() -> {
                showingIdentityCreation = false;
                dialogueAdvanceLock = false;
                dialogueBox.onDialogueShown();
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
            rm.addCharacterLP(lpCharacter, amount);
            if (rm.hasActiveRoute() && lpCharacter.equals(rm.getActiveCharacter())) {
                uiComponents.addLpPoints(amount);
            } 
        } else if (rm.hasActiveRoute()) {
            rm.addCharacterLP(rm.getActiveCharacter(), amount);
            uiComponents.addLpPoints(amount);
        }
    }
 
    @Override
    public void onMorningComplete() {
        dialogueAdvanceLock = false;
        dialogueBox.onSceneEnd();
        saveStats();
        mainPanel.onMorningComplete();
    }
 
    @Override
    public void onEveningComplete() {
        dialogueAdvanceLock = false;
        dialogueBox.onSceneEnd();
        saveStats();
        mainPanel.onEveningComplete();
    }
 
    @Override
    public void onSceneEnd() {
        dialogueAdvanceLock = false;
        dialogueBox.onSceneEnd();
    }
 
    public void loadContent() {
        dialogueAdvanceLock = false;
        dialogueBox.onDialogueShown();
        uiComponents.setPpValue(mainPanel.getPP());
        uiComponents.setSalaryValue(mainPanel.getSalary());
        
        RouteManager rm = mainPanel.getRouteManager();
        int currentLP = rm.hasActiveRoute() ? rm.getActiveLP() : 0;
        uiComponents.setLpValue(currentLP);
        uiComponents.updateDayLabel(mainPanel.getCurrentDay(), mainPanel.getCurrentSegment());;
 
        // ── Audio ─────────────────────────────────────────────────────────────
        AudioPlayer audio = AudioPlayer.getInstance();
        switch (mainPanel.getCurrentSegment()) {
            case MORNING -> {
                if (!audio.isPlaying(AudioPlayer.Track.GAMEPLAY)) {
                    audio.crossfadeTo(AudioPlayer.Track.GAMEPLAY, 800);
                }
            }
            case EVENING -> {
                if (!audio.isPlaying(AudioPlayer.Track.GAMEPLAY)) {
                    audio.crossfadeTo(AudioPlayer.Track.GAMEPLAY, 800);
                }
            }
            case ENDING -> {
                mainPanel.playEndingMusic();
            }
        }
        // ─────────────────────────────────────────────────────────────────────
 
        SceneManager.Segment seg = switch (mainPanel.getCurrentSegment()) {
            case MORNING -> {
                yield SceneManager.Segment.MORNING;
            }
            case EVENING -> {
                yield SceneManager.Segment.EVENING;
            }
            case ENDING -> {
                mainPanel.onGameComplete();
                yield SceneManager.Segment.MORNING;
            }
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
 
    // ── Internal helpers ──────────────────────────────────────────────────────
 
    private void handleRouteChoice(String characterId) {
        if ("none".equals(characterId)) {
            System.out.println("  Player chose to walk alone");
            ChoiceEntry noOne = new ChoiceEntry("No one", 0, 0,
                SceneEntry.dialogue("{name}", "No. I shouldn't be crushing on anyone.", "none", "StreetEvening.jpg"),
                SceneEntry.dialogue("{name}", "Focus. That's all that matters.", "none", "StreetEvening.jpg"),
                SceneEntry.narrator("Some calls... are meant to be unanswered.", "blackBG.jpg")
            );
            sceneManager.onChoicePicked(noOne);
            return;
        }
        
        RouteManager rm        = mainPanel.getRouteManager();
        RouteManager route     = routeForCharacter(characterId);
        DayInterface dayScript = rm.selectRoute(route, characterId);
        int accLP = rm.getLPForCharacter(characterId);
        
        uiComponents.setLpValue(accLP);
        uiComponents.updateLoveMeterVisibility(mainPanel.getCurrentDay(), MainFrame.Segment.EVENING);
        sceneManager.setDayScript(dayScript);
        sceneManager.start(SceneManager.Segment.EVENING, 0);
    }
 
    private RouteManager routeForCharacter(String name) {
        return switch (name) {
            case Character.AMAYA -> {
                yield new AmayaRoute();
            }
            default -> {
                // FIXXX 
                yield new AmayaRoute();
            }
        };
    }
 
    private String resolvePlaceholders(String text) {
        if (text == null) return "";
        String name    = mainPanel.getPlayerName();
        String pronoun = mainPanel.getPlayerPronoun();
        String subject = "they", possessive = "their", objective = "them";
        
        if (pronoun != null && !pronoun.isEmpty()) {
            String[] p = pronoun.split("/");
            subject    = p.length > 0 ? p[0] : "they";
            possessive = p.length > 1 ? p[1] : "their";
            objective  = p.length > 2 ? p[2] : "them";
        }
        
        String resolved = text
            .replace("{name}",               name != null ? name : "")
            .replace("{pronoun_subject}",    subject)
            .replace("{pronoun_possessive}", possessive)
            .replace("{pronoun_objective}",  objective);
            
        if (resolved.contains("{name}") || resolved.contains("{pronoun_")) {
        }
        
        return resolved;
    }
 
    private void saveStats() {
        mainPanel.setPP(uiComponents.getCurrentPpValue());
        mainPanel.setSalary(uiComponents.getCurrentSalaryValue());
    }
 
    private void initializeLayers() {
        bg          = new BackgroundLayer();
        sprite      = new SpriteLayer();
        dialogueBox = new DialogueBoxLayer(mainPanel);
 
        itemUse = new ItemUse(mainPanel, uiComponents, null);
        dialogueBox.setOnAdvanceRequest(() -> {
            if (!choices.isVisible() && !showingIdentityCreation && !dialogueAdvanceLock) {
                sceneManager.advanceScene();
            } else if (dialogueAdvanceLock) {
            }
        });
 
        uiComponents = new TopBarComponents(mainPanel);
        uiComponents.setSettingsPanel(settings);
        uiComponents.setParentScreen("dialogue");
        uiComponents.setInventoryPanel(inventory);
        uiComponents.onSettingsClosed(() -> {
            requestFocusInWindow();
        });
 
        identityPopup = new IdentityCreationLayer(mainPanel);
        choices       = new ChoiceButtonLayer();
        
        add(choices);
        add(uiComponents);
        add(dialogueBox);
        add(sprite);
        add(bg);
    
        spriteThread = new Thread(() -> {
            System.out.println("  Loading sprites...");
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
 
        bgThread = new Thread(() -> {
            bg.preload("MorningOffice.jpg", "EveningOffice.jpg", "BuildingEvening.jpg",
                       "BuildingMorning.jpg", "ConvenienceStore.jpg",
                       "StreetMorning.jpg", "StreetEvening.jpg",
                       "MorningElevator.jpg", "BreakRoom.jpg", "blackBG.jpg");
            System.out.println("  Background preload complete");
        }, "bg-preload");
 
        spriteThread.setDaemon(true);
        bgThread.setDaemon(true);
        spriteThread.start();
        bgThread.start();
        
    }
    
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
