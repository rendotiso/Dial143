/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package GUI.panels;

import GUI.panels.dialogueComponents.*;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InteractionPanel extends javax.swing.JPanel {
    private MainFrame mainPanel;
    private BackgroundLayer bg;
    private SpriteLayer sprite;
    private DialogueBoxLayer dialogueBox;
    private TopBarComponents uiComponents;
    private ChoiceButtonLayer choices;
    private SettingsPanel settings; // Keep this

    private int currentScene = 0;

    public InteractionPanel(MainFrame mainPanel, SettingsPanel sharedSettings) { // Add parameter
        this.mainPanel = mainPanel;
        this.settings = sharedSettings; // Use shared settings
        
        initComponents();
        initializeLayers();
    }

    private void initializeLayers() {
        bg = new BackgroundLayer();
        sprite = new SpriteLayer();
        dialogueBox = new DialogueBoxLayer();

    uiComponents = new TopBarComponents(mainPanel);
    uiComponents.setSettingsPanel(settings); // This line is crucial!
    uiComponents.setParentScreen("dialogue");

        // Optional: Add callbacks if you need to pause anything
        uiComponents.onSettingsOpening(() -> {
            // Pause any ongoing animations if needed
        });

        uiComponents.onSettingsClosed(() -> {
            // Resume any paused animations
            requestFocusInWindow();
        });

        choices = new ChoiceButtonLayer();

        add(choices);
        add(uiComponents);
        add(dialogueBox);
        add(sprite);
        add(bg);

        choices.setChoiceListener((text, node) -> onChoiceSelected(text, node));
        
         new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                sprite.addSprite("Chiaki", "placeholderSprite.png");
                sprite.addSprite("Rando", "placeholderSprite2.png");
                sprite.addSprite("Homosexual", "placeholderSprite3.png");
                sprite.addSprite("Broke ahh", "placeholderSprite4.png");
                bg.preload("placeholderBG.jpg", "placeholderBG2.jpg");
                return null;
            }
        }.execute();
    }
    
    //DELETE ONCE WITH STORY
private String[][] scenes = {
    // Single sprite
    {"Chiaki", "Hi there! Welcome to the dating sim!", "single:Chiaki", "placeholderBG.jpg"},
    // No sprite
    {"Narrator", "The room falls silent...", "none", "placeholderBG.jpg"},
    // Two sprites
    {"Chiaki", "Would you like to hang out sometime?", "two:Chiaki:Rando", "placeholderBG2.jpg"},
    // No sprite
    {"Narrator", "An awkward silence fills the air.", "none", "placeholderBG2.jpg"},
    // Single sprite, different character
    {"Rando", "Oh, hey! I didn't see you there!", "single:Rando", "placeholderBG.jpg"},
    // Two sprites again
    {"Chiaki", "We have class together, right?", "two:Rando:Chiaki", "placeholderBG2.jpg"},
    
    {"Rando", "Yes, we do!","two:Rando:Chiaki", "placeholderBG2.jpg"},
    // No sprite
    {"Narrator", "The beach stretches out endlessly.", "none", "placeholderBG2.jpg"},
    // Single sprite
    {"Rando", "The beach is beautiful today!", "single:Rando", "placeholderBG2.jpg"},
    
    {"Homosexual", "I like men", "triple:Homosexual:Rando:Chiaki","placeholderBG2.jpg"},
    
    {"Broke ahh", "Dawg you spitting fax frfr", "quadruple:Homosexual:Broke ahh:Rando:Chiaki", "placeholderBG2.jpg"},
    
    {"Narrator", "what the fuck did I just witness. What kind of testing is this. Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy." +  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy."+  "Are you crazy? Crazy? I was crazy once. "
        + "They locked me in a room. A rubber room. A rubber room with rats. And rats make me crazy.", "none", "placeholderBG2.jpg"}
   
};
        
        public void loadTestContent() {
            bg.setBackgroundColor(new Color(20, 30, 40));
            currentScene = 0;
            loadScene(currentScene);
        }

    private void loadScene(int sceneIndex) {
     if (sceneIndex < scenes.length) {
         String[] scene = scenes[sceneIndex];

         String speaker = scene[0];
         String text = scene[1];
         String spriteSpec = scene[2];
         String backgroundName = scene[3];

         if (backgroundName != null && !backgroundName.isEmpty()) {
             bg.setBackgroundFromFile(backgroundName);
         } else {
             bg.setBackgroundColor(new Color(20, 30, 40));
         }

         dialogueBox.setSpeaker(speaker);
         dialogueBox.setDialogue(text);

         // Parse sprite spec
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
           }else {
             // fallback: treat as single character name
             sprite.showSingleSprite(spriteSpec);
         }

         if (sceneIndex == 2) {
             showChoicesAtScene2();
         } else {
             choices.hideChoices();
         }

         revalidate();
         repaint();
     }
 }

    private void showChoicesAtScene2(){

        choices.clearChoices();

        choices.addChoice("Sure, I'd love to!","accept_date");
        choices.addChoice("Maybe another time","decline_date");
        choices.addChoice("What did you have in mind?","ask_details");

        choices.showChoices();
    }

    private void advanceScene() {
        if (currentScene + 1 < scenes.length) {
            currentScene++;
            loadScene(currentScene);
        } else {
            mainPanel.showScreen("shift");
        }
    }
    
    private void onChoiceSelected(String choiceText,String nextNode){

        if(choiceText.equals("Sure, I'd love to!")){

            dialogueBox.setSpeaker("Chiaki");
            dialogueBox.setDialogue("Great! How about coffee this weekend?");
            sprite.showCharacter("Chiaki");
            uiComponents.addLpPoints(10);

        }

        else if(choiceText.equals("Maybe another time")){

            dialogueBox.setSpeaker("Chiaki");
            dialogueBox.setDialogue("Oh, okay... maybe next time then.");
            sprite.showCharacter("Chiaki");
            uiComponents.addLpPoints(-5);

        }

        else if(choiceText.equals("What did you have in mind?")){

            dialogueBox.setSpeaker("Chiaki");
            dialogueBox.setDialogue("I was thinking dinner downtown.");
            sprite.showCharacter("Chiaki");
            uiComponents.addLpPoints(5);

        }

        choices.hideChoices();

        Timer timer = new Timer(1500,e->{

            advanceScene();

        });

        timer.setRepeats(false);
        timer.start();
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
                // Only advance if choices are not showing
                // EDIT ONCE WITH STORY
                if (!choices.isVisible()) {
                    advanceScene();
                }
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
