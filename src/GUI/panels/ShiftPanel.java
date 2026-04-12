package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.shiftComponents.CallCreationTimer;
import GUI.panels.InventoryPanel;
import Entities.Item;
import Entities.ItemUse;
import javax.swing.*;
import java.awt.*;
 
public class ShiftPanel extends JPanel {
 
    private final MainFrame      mainPanel;
    private final SettingsPanel  settings;
    private final InventoryPanel inventory;
    private BackgroundLayer      bg;
    private CallCreationTimer    callBox;
    private TopBarComponents     topBar;
    private ItemUse              itemEffect;

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
 
        callBox = new CallCreationTimer();
        topBar = new TopBarComponents(mainPanel);
        
        itemEffect = new ItemUse(mainPanel, topBar, callBox);
 
        callBox.onPointsAwarded((pp, salary) -> {
            int originalPP = pp;
            int boostedPP = itemEffect.applyPPMultiplier(pp);

            topBar.addPpPoints(boostedPP);
            topBar.addSalaryPoints(salary);
        });

        callBox.onCallComplete(() -> {
            itemEffect.resetPerCall();
 
            if (callBox.hasMoreCalls()) {
                Timer t = new Timer(100, e -> {
                    callBox.loadTest();
                    
                    if (itemEffect.isHintAvailable()) {
                        callBox.activateHint();
                    }
                });
                t.setRepeats(false);
                t.start();
            } else {
                saveStats();
                Timer t = new Timer(100, e -> mainPanel.onCallComplete());
                t.setRepeats(false);
                t.start();
            }
        });
 
        topBar.setSettingsPanel(settings);
        topBar.setParentScreen("shift");
        topBar.setInventoryPanel(inventory);
 
        topBar.onSettingsOpening(() -> callBox.pauseTimer());
        topBar.onSettingsClosed(() -> {
            saveStats();
            callBox.resumeTimer();
        });
 
        topBar.onInventoryOpening(() -> callBox.pauseTimer());
        topBar.onInventoryClosed(() -> {
            saveStats();
            callBox.resumeTimer();
            if (itemEffect.isHintAvailable()) { 
                callBox.activateHint();
            }
        });
        
        inventory.setOnItemEffect((effectType, effectValue) -> {
            itemEffect.handleItemEffect(effectType, effectValue);
        });
 
        add(topBar);
        add(callBox);
        add(bg);
    }
    
    public ItemUse getItemUse() {
    return itemEffect;
}
 
    public void loadCall() {
        topBar.updateForShift(mainPanel.getCurrentDay());
        topBar.setPpValue(mainPanel.getPP());
        topBar.setLpValue(mainPanel.getLP());
        topBar.setSalaryValue(mainPanel.getSalary());
        callBox.resetRemainingCalls();
        callBox.loadTest();
        
        if (itemEffect.isHintAvailable()) {  
            callBox.activateHint();
        }
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
