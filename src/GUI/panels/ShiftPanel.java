package GUI.panels;

import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.shiftComponents.CallDialogueBoxLayer;
import GUI.panels.InventoryPanel;
import Entities.Item;
import Entities.ActiveEffects;
import javax.swing.*;
import java.awt.*;

public class ShiftPanel extends JPanel {

    private final MainFrame          mainPanel;
    private final SettingsPanel      settings;
    private final InventoryPanel     inventory;
    private BackgroundLayer          bg;
    private CallDialogueBoxLayer     callBox;
    private TopBarComponents         topBar;

    public ShiftPanel(MainFrame mainPanel, SettingsPanel sharedSettings, InventoryPanel inventory) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        this.inventory = inventory;
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }

    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("MorningOffice.jpg");

        topBar = new TopBarComponents(mainPanel);
        topBar.setSettingsPanel(settings);
        topBar.setParentScreen("shift");
        topBar.setInventoryPanel(inventory);

        inventory.setOnItemEffect((effectType, effectValue) -> {
            ActiveEffects fx = mainPanel.getActiveEffects();
            if (effectType == Item.EffectType.LP_FLAT) {
                String activeChar = mainPanel.getRouteManager().hasActiveRoute()
                    ? mainPanel.getRouteManager().getActiveCharacter() : null;
                if (activeChar != null) {
                    mainPanel.getRouteManager().addCharacterLP(activeChar, effectValue);
                    topBar.addLpPoints(effectValue);
                }
            } else {
                fx.apply(effectType, effectValue);
            }
        });

    topBar.onSettingsOpening(() -> callBox.pauseTimer());
    topBar.onSettingsClosed(() -> {
        saveStats();
        callBox.resumeTimer();
    });

    topBar.onInventoryOpening(() -> callBox.pauseTimer());
    topBar.onInventoryClosed(() -> { 
        saveStats(); 
        callBox.resumeTimer();
    });  // Fixed: Added missing closing parenthesis and semicolon

        callBox = new CallDialogueBoxLayer();
        callBox.onPointsAwarded((pp, salary) -> {
            topBar.addPpPoints(pp);
            topBar.addSalaryPoints(salary);
        });

        callBox.onCallComplete(() -> {
            if (callBox.hasMoreCalls()) {
                Timer timer = new Timer(100, e -> callBox.loadTest());
                timer.setRepeats(false);
                timer.start();
            } else {
                saveStats();
                Timer timer = new Timer(100, e -> mainPanel.onCallComplete());
                timer.setRepeats(false);
                timer.start();
            }
        });

        add(topBar);
        add(callBox);
        add(bg);
    }

    public void loadCall() {
        topBar.updateForShift(mainPanel.getCurrentDay());
        topBar.setPpValue(mainPanel.getPP());
        topBar.setLpValue(mainPanel.getLP());
        topBar.setSalaryValue(mainPanel.getSalary());
        callBox.resetRemainingCalls();
        callBox.loadTest();
    }

    private void saveStats() {
        mainPanel.setPP(topBar.getCurrentPpValue());
        mainPanel.setLP(topBar.getCurrentLpValue());
        mainPanel.setSalary(topBar.getCurrentSalaryValue());
    }

    public void pauseTimer()       { callBox.pauseTimer(); }
    public void resumeTimer()      { callBox.resumeTimer(); }
    public boolean isTimerPaused() { return callBox.isTimerPaused(); }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
