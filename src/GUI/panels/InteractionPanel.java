package GUI.panels;

import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.dialogueComponents.*;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import GUI.panels.inventoryComponents.ItemLayer;

public class InteractionPanel extends javax.swing.JPanel {

    private MainFrame         mainPanel;
    private BackgroundLayer   bg;
    private SpriteLayer       sprite;
    private DialogueBoxLayer  dialogueBox;
    private TopBarComponents  uiComponents;
    private ChoiceButtonLayer choices;
    private SettingsPanel     settings;
    private IdentityCreationLayer identityPopup; // Changed from IdentityCreationLayer
    private boolean showingIdentityCreation = false;

    private int currentScene = 0;

    public InteractionPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        initComponents();
        initializeLayers();
    }

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
        identityPopup.setOnComplete(() -> {
            showingIdentityCreation = false;
            advanceScene(); 
        });

        choices = new ChoiceButtonLayer();
        
        add(choices);
        add(uiComponents);
        add(dialogueBox);
        add(sprite);
        add(bg);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                sprite.addSprite("Amaya",     "Amaya_Default_Smiling.PNG");
                sprite.addSprite("Rosario",   "Rosario_Default_Listening.PNG");
                bg.preload("placeholderBG.jpg", "placeholderBG2.jpg", "placeholderBG3.jpg");
                return null;
            }
        }.execute();
    }

    // LOADING CONTENT, FOR MAIN FRAME
    public void loadContent() {
        uiComponents.setPpValue(mainPanel.getPP());
        uiComponents.setLpValue(mainPanel.getLP());
        uiComponents.setSalaryValue(mainPanel.getSalary());
        currentScene = mainPanel.getDialogueScene();
        loadScene(currentScene);
    }

    // SAVING STATS
    private void saveStats() {
        mainPanel.setPP(uiComponents.getCurrentPpValue());
        mainPanel.setLP(uiComponents.getCurrentLpValue());
        mainPanel.setSalary(uiComponents.getCurrentSalaryValue());
    }

    // SCENE DEFINITIONS
    private final String[][] morningScenes = {
        {"Narrator", "Your day started as normal. However, this day was one of the ones where you were much more upbeat than usual.", "none", "placeholderBG.jpg"},
        
        {"Narrator", "It was now time to start your very first job after graduation. It was not an easy one to get hold of, and the starting pay was enough to pay off bills and necessities.", 
            "none", "placeholderBG2.jpg"},
        
        {"Narrator", "You pat off any creases from your office wear, and finally took one last look at your company ID.", "none", "placeholderBG3.jpg"},

        {"IDENTITY_CREATION", "", "", ""},
        
        {"Narrator", "You step inside the building, ready for your first day.", "none", "placeholderBG3.jpg"},
        
        {"Amaya", "Good morning, {name}! Ready for your first shift?", "single:Amaya", "placeholderBG.jpg"},
        {"Rosario", "Don't worry Amaya, {pronoun_subject} can do this. ","single:Rosario", "placeholderBG.jpg"},
    };

    private final String[][] eveningScenes = {
        {"Narrator", "The shift is finally over. The evening air feels cool and refreshing.", "none", "placeholderBG2.jpg"},
        {"Amaya", "Great job today, {name}! You handled those calls really well.", "single:Amaya", "placeholderBG.jpg"},
        {"Rosario", "I'm impressed with how quickly {pronoun_subject}'s learning.", "single:Rosario", "placeholderBG.jpg"},
        {"Narrator", "You feel a sense of accomplishment as you head home.", "none", "placeholderBG.jpg"},
    };

    private final String[][] endingScenes = {
        
        {"Narrator", "And so your journey comes to an end...", "none", "placeholderBG2.jpg"},
        {"Amaya", "{name}, I'll never forget the time we spent working together.", "single:Amaya", "placeholderBG.jpg"},
        {"Rosario", "You've grown so much since your first day.", "single:Rosario", "placeholderBG.jpg"},
        {"Narrator", "THE END", "none", "placeholderBG2.jpg"},
    };

    // Get current scenes based on segment
    private String[][] getCurrentScenes() {
        switch (mainPanel.getCurrentSegment()) {
            case MORNING:
                return morningScenes;
            case EVENING:
                return eveningScenes;
            case ENDING:
                return endingScenes;
            default:
                return morningScenes;
        }
    }

    // Load a specific scene
    private void loadScene(int sceneIndex) {
        String[][] scenes = getCurrentScenes();

        if (sceneIndex >= scenes.length) {
            saveStats();
            switch (mainPanel.getCurrentSegment()) {
                case MORNING -> mainPanel.onMorningComplete();
                case EVENING -> mainPanel.onEveningComplete();
                case ENDING  -> mainPanel.onGameComplete();
            }
            return;
        }

        String[] scene = scenes[sceneIndex];
        String speaker = scene[0];
        String text = scene[1];
        String spriteSpec = scene[2];
        String backgroundName = scene[3];
        
        if (speaker.equals("IDENTITY_CREATION")) {
            
            showingIdentityCreation = true;
            
            SwingUtilities.invokeLater(() -> {
                identityPopup.reset();
                identityPopup.showAsPopup(); 
            });
            
            return;
        }
        
        if (showingIdentityCreation) {
            showingIdentityCreation = false;
        }
        
        if (backgroundName != null && !backgroundName.isEmpty()) {
            bg.setBackgroundFromFile(backgroundName);
        } else {
            bg.setBackgroundColor(new Color(20, 30, 40));
        }

        dialogueBox.setSpeaker(speaker);
        dialogueBox.setDialogue(text);

        if (spriteSpec.equals("none")) {
            sprite.hideAllSprites();
        } else if (spriteSpec.startsWith("two:")) {
            String[] parts = spriteSpec.split(":");
            sprite.showTwoSprites(parts[1], parts[2]);
        } else if (spriteSpec.startsWith("single:")) {
            String[] parts = spriteSpec.split(":");
            sprite.showSingleSprite(parts[1]);
        } else if (spriteSpec.startsWith("triple:")) {
            String[] parts = spriteSpec.split(":");
            sprite.showThreeSprites(parts[1], parts[2], parts[3]);
        } else if (spriteSpec.startsWith("quadruple:")) {
            String[] parts = spriteSpec.split(":");
            sprite.showFourSprites(parts[1], parts[2], parts[3], parts[4]);
        } else {
            sprite.showSingleSprite(spriteSpec);
        }

        choices.hideChoices();

        revalidate();
        repaint();
    }

    private void advanceScene() {
        currentScene++;
        mainPanel.setDialogueScene(currentScene);
        loadScene(currentScene);
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
                advanceScene();
            }
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
