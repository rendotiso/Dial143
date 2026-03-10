/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package GUI.panels;

import GUI.panels.dialogueComponents.*;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;

public class InteractionPanel extends javax.swing.JPanel {

    private MainFrame mainPanel;
    private SettingsPanel settings;

    // Layer components
    private BackgroundLayer bg;
    private SpriteLayer sprite;
    private DialogueBoxLayer dialogueBox;
    private TopBarComponents uiComponents;
    private ChoiceButtonLayer choices;

    private int currentScene = 0;


    public InteractionPanel(MainFrame mainPanel) {

        this.mainPanel = mainPanel;
        this.settings = new SettingsPanel(mainPanel);

        initComponents();
        initializeLayers();
    }

    private void initializeLayers() {

        bg = new BackgroundLayer();
        sprite = new SpriteLayer();
        dialogueBox = new DialogueBoxLayer();
        uiComponents = new TopBarComponents();
        choices = new ChoiceButtonLayer();

        add(choices);
        add(uiComponents);
        add(dialogueBox);
        add(sprite);
        add(bg);

        choices.setChoiceListener((text, node) -> onChoiceSelected(text, node));
        uiComponents.setSettingsButtonAction(settings);
        
         new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                sprite.addSprite("Chiaki", "placeholderSprite.png");
                sprite.addSprite("Rando", "placeholderSprite2.png");
                bg.preload("placeholderBG.jpg", "placeholderBG2.jpg");
                return null;
            }
        }.execute();
    }
    
    //DELETE ONCE WITH STORY
    private String[][] scenes = {

        {"Chiaki", "Hi there! Welcome to the dating sim!", "Chiaki", "placeholderBG.jpg"},
        {"Chiaki", "I'm so glad you decided to talk to me today.", "Chiaki", "placeholderBG.jpg"},
        {"Chiaki", "Would you like to hang out sometime?", "Chiaki", "placeholderBG2.jpg"},
        {"Rando", "Oh, hey! I didn't see you there!", "Rando", "placeholderBG.jpg"},
        {"Rando", "I've been wanting to ask you something...", "Rando", "placeholderBG.jpg"},
        {"Chiaki", "We have class together, right?", "Chiaki", "placeholderBG2.jpg"},
        {"Rando", "The beach is beautiful today!", "Rando", "placeholderBG2.jpg"}

    };
        
        public void loadTestContent() {
            bg.setBackgroundColor(new Color(20, 30, 40));
            currentScene = 0;
            loadScene(currentScene);
            uiComponents.setPpValue(50);
            uiComponents.setLpValue(0);
        }

    private void loadScene(int sceneIndex) {

        if(sceneIndex < scenes.length){

            String[] scene = scenes[sceneIndex];

            String speaker = scene[0];
            String text = scene[1];
            String spriteName = scene[2];
            String backgroundName = scene[3];

            if(backgroundName != null && !backgroundName.isEmpty()){

                bg.setBackgroundFromFile(backgroundName);

            } else {

                bg.setBackgroundColor(new Color(20,30,40));

            }

            dialogueBox.setSpeaker(speaker);
            dialogueBox.setDialogue(text);

            sprite.showCharacter(spriteName);

            if(sceneIndex == 2){

                showChoicesAtScene2();

            }else{

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
