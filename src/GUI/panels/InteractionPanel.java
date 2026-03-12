package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.dialogueComponents.*;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import GUI.panels.inventoryComponents.*;
 
public class InteractionPanel extends javax.swing.JPanel {
 
    private MainFrame             mainPanel;
    private BackgroundLayer       bg;
    private SpriteLayer           sprite;
    private DialogueBoxLayer      dialogueBox;
    private TopBarComponents      uiComponents;
    private ChoiceButtonLayer     choices;
    private SettingsPanel         settings;
    private IdentityCreationLayer identityPopup;
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
            @Override protected Void doInBackground() {
                sprite.addSprite("Amaya",   "Amaya_Default_Smiling.PNG");
                sprite.addSprite("Rosario", "Rosario_Default_Listening.PNG");
                bg.preload("MorningOffice.jpg", "EveningOffice.jpg", "BuildingEvening.jpg",
                        "BuildingMorning.jpg", "ConvenienceStore.jpg",
                        "StreetMorning.jpg", "StreetEvening.jpg");
                return null;
            }
        }.execute();
    }
 
    // ── Load / save ───────────────────────────────────────────────────────────
 
    public void loadContent() {
        uiComponents.setPpValue(mainPanel.getPP());
        uiComponents.setLpValue(mainPanel.getLP());
        uiComponents.setSalaryValue(mainPanel.getSalary());
        currentScene = mainPanel.getDialogueScene();
        loadScene(currentScene);
    }
 
    private void saveStats() {
        mainPanel.setPP(uiComponents.getCurrentPpValue());
        mainPanel.setLP(uiComponents.getCurrentLpValue());
        mainPanel.setSalary(uiComponents.getCurrentSalaryValue());
    }
 
    // ── Scene definitions ─────────────────────────────────────────────────────
    //
    // Format: { speaker, text, spriteSpec, background }
    // Special speakers:
    //   "IDENTITY_CREATION" — shows identity popup
    //   "CHOICE"            — shows choice buttons; define options in getChoiceLabelsForScene()
 
    private final String[][] morningScenes = {
        // 0
        {"Narrator",
         "Your day started as normal. However, this day was one of the ones where you were much more upbeat than usual.",
         "none", "StreetMorning.jpg"},
 
        // 1
        {"Narrator",
         "It was now time to start your very first job after graduation. It was not easy to get, and the starting pay was enough to cover bills and necessities.",
         "none", "BuildingMorning.jpg"},
 
        // 2
        {"Narrator",
         "You pat off any creases from your office wear, and finally took one last look at your company ID.",
         "none", "BuildingMorning.jpg"},
 
        // 3 — identity creation popup
        {"IDENTITY_CREATION", "", "", ""},
 
        // 4
        {"Narrator",
         "You step inside the building, ready for your first day.",
         "none", "BuildingMorning.jpg"},
 
        // 5
        {"Amaya",
         "Good morning, {name}! Ready for your first shift?",
         "single:Amaya", "MorningOffice.jpg"},
 
        // 6 — choice: respond to Amaya
        {"CHOICE", "", "", ""},
 
        // 7
        {"Rosario",
         "Don't worry Amaya, {pronoun_subject} can do this.",
         "single:Rosario", "MorningOffice.jpg"},
 
        // 8 — choice: respond to Rosario
        {"CHOICE", "", "", ""},
    };
 
    private final String[][] eveningScenes = {
        {"Narrator",  "The shift is finally over. The evening air feels cool and refreshing.",  "none",           "EveningOffice.jpg"},
        {"Amaya",     "Great job today, {name}! You handled those calls really well.",          "single:Amaya",   "EveningOffice.jpg"},
        {"Rosario",   "I'm impressed with how quickly {pronoun_subject}'s learning.",           "single:Rosario", "EveningOffice.jpg"},
        {"Narrator",  "You feel a sense of accomplishment as you head home.",                   "none",           "StreetEvening.jpg"},
    };
 
    private final String[][] endingScenes = {
        {"Narrator",  "And so your journey comes to an end...",                               "none",           "placeholderBG2.jpg"},
        {"Amaya",     "{name}, I'll never forget the time we spent working together.",        "single:Amaya",   "placeholderBG.jpg"},
        {"Rosario",   "You've grown so much since your first day.",                           "single:Rosario", "placeholderBG.jpg"},
        {"Narrator",  "THE END",                                                              "none",           "placeholderBG2.jpg"},
    };
 
    // ── Choice definitions ────────────────────────────────────────────────────
    //
    // Each entry: { "Button label", "ppDelta", "lpDelta" }
    // Return null for scenes with no choices.
 
    private String[][] getChoiceLabelsForScene(int sceneIndex) {
        if (mainPanel.getCurrentSegment() != MainFrame.Segment.MORNING) return null;
        switch (sceneIndex) {
            case 6: return new String[][] {
                {"Of course! I'm excited.",    "15", "10"},
                {"A little nervous, but yes.", "10", "15"},
                {"Let's just get it done.",    "5",  "5" },
            };
            case 8: return new String[][] {
                {"Thank you, Rosario!",        "5",  "20"},
                {"I'll try my best.",          "10", "10"},
                {"I don't need reassurance.",  "15", "0" },
            };
            default: return null;
        }
    }
 
    // ── Scene routing ─────────────────────────────────────────────────────────
 
    private String[][] getCurrentScenes() {
        switch (mainPanel.getCurrentSegment()) {
            case MORNING: return morningScenes;
            case EVENING: return eveningScenes;
            case ENDING:  return endingScenes;
            default:      return morningScenes;
        }
    }
 
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

    String[] scene      = scenes[sceneIndex];
    String   speaker    = scene[0];
    String   text       = scene[1];
    String   spriteSpec = scene[2];
    String   bgName     = scene[3];

    // ── Identity creation ─────────────────────────────────────────────────
    if (speaker.equals("IDENTITY_CREATION")) {
        showingIdentityCreation = true;
        SwingUtilities.invokeLater(() -> {
            identityPopup.reset();
            identityPopup.showAsPopup();
        });
        return;
    }

    if (showingIdentityCreation) showingIdentityCreation = false;

// ── Choice scene ──────────────────────────────────────────────────────
if (speaker.equals("CHOICE")) {
    String[][] choiceData = getChoiceLabelsForScene(sceneIndex);
    if (choiceData != null) {
        // Clear any existing choices
        choices.clearChoices();
        
        // Add each choice with its node (using index as node for simplicity)
        for (int i = 0; i < choiceData.length; i++) {
            String label = choiceData[i][0];
            String node = "choice_" + i; // Simple node identifier
            choices.addChoice(label, node, true); // All choices unlocked for now
        }
        
        // Set the listener to handle choice selection
        choices.setChoiceListener(new ChoiceButtonLayer.ChoiceListener() {
            @Override
            public void onChoiceSelected(String choiceText, String nextNode) {
                // Find which choice was selected by matching text
                for (int i = 0; i < choiceData.length; i++) {
                    if (choiceData[i][0].equals(choiceText)) {
                        // Award points based on choice
                        int pp = Integer.parseInt(choiceData[i][1]);
                        int lp = Integer.parseInt(choiceData[i][2]);
                        uiComponents.addPpPoints(pp);
                        uiComponents.addLpPoints(lp);
                        
                        // Hide choices
                        choices.hideChoices();
                        
                        // Advance to next scene
                        SwingUtilities.invokeLater(() -> {
                            advanceScene();
                        });
                        break;
                    }
                }
            }
        });
        
        // Show the choices
        choices.showChoices();
    } else {
        // No choices defined, just advance
        advanceScene();
    }
    return;
}

    // ── Normal dialogue scene ─────────────────────────────────────────────
    if (bgName != null && !bgName.isEmpty()) {
        bg.setBackgroundFromFile(bgName);
    } else {
        bg.setBackgroundColor(new Color(20, 30, 40));
    }

    // Substitute {name} and {pronoun_subject}
    String playerName = mainPanel.getPlayerName();
    String playerPronoun = mainPanel.getPlayerPronoun();
    String pronounSubject = (playerPronoun != null && playerPronoun.contains("/"))
        ? playerPronoun.split("/")[0] : "they";
    String possessive = (playerPronoun != null && playerPronoun.equals("she/her")) ? "her" : "his";
    
    text = text.replace("{name}", playerName != null ? playerName : "")
               .replace("{pronoun_subject}", pronounSubject)
               .replace("{pronoun_possessive}", possessive);

    dialogueBox.setSpeaker(speaker);
    dialogueBox.setDialogue(text);

    // Handle sprite display
    if (spriteSpec.equals("none")) {
        sprite.hideAllSprites();
    } else if (spriteSpec.startsWith("single:")) {
        sprite.showSingleSprite(spriteSpec.split(":")[1]);
    } else if (spriteSpec.startsWith("two:")) {
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
