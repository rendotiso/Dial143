package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.dialogueComponents.*;
import GUI.panels.MainFrame;
import GUI.panels.inventoryComponents.*;
import Storyline.*;
 
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
 
    private Thread       spriteThread;
    private Thread       bgThread;
    private SceneManager sceneManager;
 
    public InteractionPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        initComponents();
        initializeLayers();
        sceneManager = new SceneManager(new Day1(), this);
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
            sprite.showSingleSprite(spriteSpec.split(":")[1]);
        } else if (spriteSpec.startsWith("double:")) {
            String[] p = spriteSpec.split(":");
            sprite.showTwoSprites(p[1], p[2]);
        } else if (spriteSpec.startsWith("triple:")) {
            String[] p = spriteSpec.split(":");
            sprite.showThreeSprites(p[1], p[2], p[3]);
        } else if (spriteSpec.startsWith("quadruple:")) {
            String[] p = spriteSpec.split(":");
            sprite.showFourSprites(p[1], p[2], p[3], p[4]);
        } else {
            sprite.showSingleSprite(spriteSpec);
        }
        choices.hideChoices();
        revalidate();
        repaint();
    }
 
    @Override
    public void showDialogue(String speaker, String text) {
        // Replace placeholders in BOTH speaker and text
        String resolvedSpeaker = resolvePlaceholders(speaker);
        String resolvedText = resolvePlaceholders(text);
        dialogueBox.setSpeaker(resolvedSpeaker);
        dialogueBox.setDialogue(resolvedText);
    }

    @Override
    public void showNarrator(String text) {
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
    public void addLP(int amount) {
        uiComponents.addLpPoints(amount);
        mainPanel.setLP(mainPanel.getLP() + amount);
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
 
    // ── Public API ────────────────────────────────────────────────────────────
 
    // In InteractionPanel.java - Update loadContent() method
    public void loadContent() {
        uiComponents.setPpValue(mainPanel.getPP());
        uiComponents.setLpValue(mainPanel.getLP());
        uiComponents.setSalaryValue(mainPanel.getSalary());

        // Update day label based on current segment
        switch (mainPanel.getCurrentSegment()) {
            case MORNING -> uiComponents.updateDayLabel(mainPanel.getCurrentDay(), MainFrame.Segment.MORNING);
            case EVENING -> uiComponents.updateDayLabel(mainPanel.getCurrentDay(), MainFrame.Segment.EVENING);
            case ENDING -> uiComponents.updateDayLabel(mainPanel.getCurrentDay(), MainFrame.Segment.ENDING);
        }

        SceneManager.Segment segment = switch (mainPanel.getCurrentSegment()) {
            case MORNING -> SceneManager.Segment.MORNING;
            case EVENING -> SceneManager.Segment.EVENING;
            case ENDING -> { mainPanel.onGameComplete(); yield SceneManager.Segment.MORNING; }
        };

        if (mainPanel.getCurrentSegment() != MainFrame.Segment.ENDING) {
            sceneManager.start(segment, mainPanel.getDialogueScene());
        }
    }
 
    public void setDayScript(DayInterface script) {
        sceneManager.setDayScript(script);
    }
 
    // ── Helpers ───────────────────────────────────────────────────────────────
 
    private String resolvePlaceholders(String text) {
        if (text == null) return "";
        String playerName = mainPanel.getPlayerName();
        String playerPronoun = mainPanel.getPlayerPronoun();

        // Handle pronoun format like "she/her" or "they/them"
        String subject = "they";
        String possessive = "their";
        String objective = "them";

        if (playerPronoun != null && !playerPronoun.isEmpty()) {
            String[] parts = playerPronoun.split("/");
            subject = parts.length > 0 ? parts[0] : "they";
            possessive = parts.length > 1 ? parts[1] : "their";
            objective = parts.length > 2 ? parts[2] : "them";
        }

        return text
            .replace("{name}", playerName != null ? playerName : "")
            .replace("{pronoun_subject}", subject)
            .replace("{pronoun_possessive}", possessive)
            .replace("{pronoun_objective}", objective);
    }
 
    private void saveStats() {
        mainPanel.setPP(uiComponents.getCurrentPpValue());
        mainPanel.setLP(uiComponents.getCurrentLpValue());
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
            sprite.addSprite("Amaya_Smile",     "Amaya_Smile.png");
            sprite.addSprite("Amaya_Casual",    "Amaya_Casual.png");
            sprite.addSprite("Amaya_Sad",       "Amaya_Sad.png");
            sprite.addSprite("Amaya_Mad",       "Amaya_Mad.png");
            sprite.addSprite("Amaya_Blushing",  "Amaya_Blushing.png");
 
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
 
            sprite.addSprite("Cloma_Smile",    "Cloma_Smile.png");
            sprite.addSprite("Cloma_Casual",   "Cloma_Casual.png");
            sprite.addSprite("Cloma_Sad",      "Cloma_Sad.png");
            sprite.addSprite("Cloma_Mad",      "Cloma_Mad.png");
            sprite.addSprite("Cloma_Blushing", "Cloma_Blushing.png");
        }, "sprite-preload");
 
        bgThread = new Thread(() ->
            bg.preload("MorningOffice.jpg", "EveningOffice.jpg", "BuildingEvening.jpg",
                       "BuildingMorning.jpg", "ConvenienceStore.jpg",
                       "StreetMorning.jpg", "StreetEvening.jpg",
                       "MorningElevator.jpg"),
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
