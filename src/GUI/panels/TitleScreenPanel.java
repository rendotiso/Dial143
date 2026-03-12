package GUI.panels;

import GUI.panels.MainFrame; 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.titleScreenComponents.*;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;

public class TitleScreenPanel extends javax.swing.JPanel  {

    private MainFrame mainPanel;

    private BackgroundLayer bg;
    private TitleScreenComponents buttons;

    public TitleScreenPanel(MainFrame mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
        initializeLayers();
    }

    private void initializeLayers() {
        
            bg = new BackgroundLayer();
            bg.setBackgroundFromFile("TitleBackground.jpg");

            buttons = new TitleScreenComponents();
            buttons.setTitleImage("gameTitle.png");
            buttons.setPlayAction(() -> {mainPanel.resetStats(); mainPanel.showScreen("dialogue");}); 
            buttons.setSaveAction(() -> { 
            
            // EMPTY, NO PLANS YET 
            
            });
            buttons.setExitAction(() -> System.exit(0));

            add(buttons);
            add(bg);
   }
    
       
     // BELOW IS THE CODE GENERATURED FROM NETBEANS
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
