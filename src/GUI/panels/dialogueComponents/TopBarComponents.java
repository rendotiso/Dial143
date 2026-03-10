/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.panels.dialogueComponents;

import GUI.panels.SettingsPanel;
import javax.swing.*;
import java.awt.*;

public class TopBarComponents extends JPanel {
    
    private SettingsPanel settings;
    
    private javax.swing.JButton btnSettings;
    private javax.swing.JLabel lpLabel;
    private javax.swing.JLabel lpValue;
    private javax.swing.JLabel ppLabel;
    private javax.swing.JLabel ppValue;
    
    public TopBarComponents() {
        setBounds(0, 0, 1280, 720);
        setLayout(null);
        setOpaque(false);
        
        ppLabel = new JLabel("Performance Points:");
        ppLabel.setBounds(20, 10, 120, 20);
        ppLabel.setForeground(Color.WHITE);
        add(ppLabel);
        
        ppValue = new JLabel("0");
        ppValue.setBounds(150, 10, 50, 20);
        ppValue.setForeground(Color.WHITE);
        add(ppValue);
        
        lpLabel = new JLabel("Love Points:");
        lpLabel.setBounds(20, 35, 80, 20);
        lpLabel.setForeground(Color.WHITE);
        add(lpLabel);
        
        lpValue = new JLabel("0");
        lpValue.setBounds(110, 35, 50, 20);
        lpValue.setForeground(Color.WHITE);
        add(lpValue);
        
        btnSettings = new JButton("Settings");
        btnSettings.setBounds(1150, 10, 100, 30);
        btnSettings.setBackground(new Color(60, 60, 60));
        btnSettings.setForeground(Color.WHITE);
        btnSettings.setFocusPainted(false);
        add(btnSettings);
    }
    
    @Override public Dimension getMinimumSize() { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize() { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }


    public int getCurrentPpValue() {
        try {
            return Integer.parseInt(ppValue.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public int getCurrentLpValue() {
        try {
            return Integer.parseInt(lpValue.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public JButton getBtnSettings() {
        return btnSettings;
    }
    
    
    public void setPpValue(int points) {
        ppValue.setText(String.valueOf(points));
    }
    
    public void setLpValue(int points) {
        lpValue.setText(String.valueOf(points));
    }

    public void setStats(int pp, int lp) {
        setPpValue(pp);
        setLpValue(lp);
    }

    public void addPpPoints(int points) {
        int current = getCurrentPpValue();
        setPpValue(current + points);
    }

    public void addLpPoints(int points) {
        int current = getCurrentLpValue();
        setLpValue(current + points);
    }

    public void resetStats() {
        setPpValue(0);
        setLpValue(0);
    }
    
    public void setSettingsButtonVisible(boolean visible) {
        btnSettings.setVisible(visible);
    }
    
    public void setSettingsButtonEnabled(boolean enabled) {
        btnSettings.setEnabled(enabled);
    }

    public void setSettingsButtonAction(SettingsPanel settingsPanel) {
        this.settings = settingsPanel;
        btnSettings.addActionListener(e -> {
            if (settings != null) {
                settings.showAsPopup();
            }
        });
    }
    
}