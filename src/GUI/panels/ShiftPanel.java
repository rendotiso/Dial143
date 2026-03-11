/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.panels;

import GUI.panels.shiftComponents.CallDialogueBoxLayer;
import GUI.panels.dialogueComponents.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShiftPanel extends JPanel {
    
    private final MainFrame mainPanel;
    private BackgroundLayer bg;
    private CallDialogueBoxLayer callBox;
    private TopBarComponents topBar;
    private SettingsPanel settings;

    public ShiftPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;  
        this.settings = sharedSettings;
        
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }
    
    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("placeholderBG3.jpg");

        topBar = new TopBarComponents(mainPanel);
        topBar.setSettingsPanel(settings); 
        topBar.setParentScreen("shift");


        callBox = new CallDialogueBoxLayer();
        
        topBar.onSettingsOpening(() -> {
            callBox.pauseTimer();
        });

    topBar.onSettingsClosed(() -> {
        callBox.resumeTimer();
    });

        add(topBar);
        add(callBox);
        add(bg);
    }

    public void loadTestCall() {
        callBox.loadTest();
    }
    
    // These methods delegate to callBox
    public void pauseTimer() {
        callBox.pauseTimer();
    }
    
    public void resumeTimer() {
        callBox.resumeTimer();
    }
    
    public boolean isTimerPaused() {
        return callBox.isTimerPaused();
    }

    public void setBackground(String filename) {
        bg.setBackgroundFromFile(filename);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
