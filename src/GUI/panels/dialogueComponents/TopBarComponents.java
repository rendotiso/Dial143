/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.panels.dialogueComponents;

import GUI.panels.SettingsPanel;
import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.awt.event.*;

public class TopBarComponents extends JPanel {
    private Font pixelFont; 
    private SettingsPanel settings;
    private MainFrame mainFrame;
    private String parentScreen = "shift";
    
    private Runnable onSettingsOpening;
    private Runnable onSettingsClosed;
    
    private javax.swing.JButton btnSettings;
    private javax.swing.JLabel lpLabel;
    private javax.swing.JLabel lpValue;
    private javax.swing.JLabel ppLabel;
    private javax.swing.JLabel ppValue;
    
    // Modified constructor - remove SettingsPanel creation
    public TopBarComponents(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        loadCustomFont();
        
        setBounds(0, 0, 1280, 720);
        setLayout(null);
        setOpaque(false);
        
        ppLabel = new JLabel("Performance Points:");
        ppLabel.setBounds(20, 10, 180, 30);  
        ppLabel.setForeground(Color.WHITE);
        ppLabel.setFont(pixelFont != null ? pixelFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
        add(ppLabel);
        
        ppValue = new JLabel("0");
        ppValue.setBounds(170, 10, 60, 30); 
        ppValue.setForeground(Color.WHITE);
        ppValue.setFont(pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Arial", Font.BOLD, 18));
        add(ppValue);
        
        lpLabel = new JLabel("Love Points:");
        lpLabel.setBounds(20, 35, 130, 30);
        lpLabel.setForeground(Color.WHITE);
        lpLabel.setFont(pixelFont != null ? pixelFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
        add(lpLabel);
        
        lpValue = new JLabel("0");
        lpValue.setBounds(170, 35, 60, 30);  
        lpValue.setForeground(Color.WHITE);
        lpValue.setFont(pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Arial", Font.BOLD, 18));
        add(lpValue);
        
        btnSettings = new JButton("Settings");
        btnSettings.setBounds(1150, 15, 100, 35);
        btnSettings.setBackground(new Color(70, 70, 70));
        btnSettings.setForeground(Color.WHITE);
        btnSettings.setFocusPainted(false);
        btnSettings.setFont(pixelFont != null ? pixelFont.deriveFont(15f) : new Font("Arial", Font.BOLD, 15));
        btnSettings.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // DON'T call setupSettingsButton() here - wait until settings panel is set
        
        btnSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSettings.setBackground(new Color(100, 100, 100));
                btnSettings.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSettings.setBackground(new Color(70, 70, 70));
                btnSettings.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        add(btnSettings);

        setComponentZOrder(ppLabel, 0);
        setComponentZOrder(ppValue, 1);
        setComponentZOrder(lpLabel, 2);
        setComponentZOrder(lpValue, 3);
        setComponentZOrder(btnSettings, 4);
    }
    
    public void setSettingsPanel(SettingsPanel settings) {
        this.settings = settings;
        setupSettingsButton();
    }
    
    private void setupSettingsButton() {
        for (ActionListener al : btnSettings.getActionListeners()) {
            btnSettings.removeActionListener(al);
        }
        
        btnSettings.addActionListener(e -> {
            if (settings == null) return;

            btnSettings.setEnabled(false);

            if (onSettingsOpening != null) {
                onSettingsOpening.run();
            }

            settings.setPreviousScreen(parentScreen);
            settings.showAsPopup(); 
            
            handleSettingsClosed();

            if (onSettingsClosed != null) {
                onSettingsClosed.run();
            }
        });
    }
    
    private void handleSettingsClosed() {
        btnSettings.setEnabled(true);
        
        if (onSettingsClosed != null) {
            onSettingsClosed.run();
        }
        
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
        
        System.out.println("Settings closed - button re-enabled"); 
    }
    
    public void setParentScreen(String screenName) {
        this.parentScreen = screenName;
    }
    
    public void onSettingsOpening(Runnable callback) {
        this.onSettingsOpening = callback;
    }
    
    public void onSettingsClosed(Runnable callback) {
        this.onSettingsClosed = callback;
    }
    
    private void loadCustomFont() {
        try {
            InputStream stream = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            
            if (stream != null) {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, stream);
            } else {
                pixelFont = null;
            }
        } catch (FontFormatException e) {
            System.err.println("Font format error: " + e.getMessage());
            e.printStackTrace();
            pixelFont = null;
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
            e.printStackTrace();
            pixelFont = null;
        }
    }
    
    @Override 
    public Dimension getMinimumSize() { 
        return new Dimension(1280, 720); 
    }
    
    @Override 
    public Dimension getMaximumSize() { 
        return new Dimension(1280, 720); 
    }
    
    @Override 
    public Dimension getPreferredSize() { 
        return new Dimension(1280, 720); 
    }

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
    
    public Rectangle getSettingsButtonBounds() {
        return btnSettings.getBounds();
    }
}