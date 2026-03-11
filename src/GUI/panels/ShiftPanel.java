package GUI.panels;

import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.shiftComponents.CallDialogueBoxLayer;
import javax.swing.*;
import java.awt.*;

public class ShiftPanel extends JPanel {

    private final MainFrame          mainPanel;
    private BackgroundLayer          bg;
    private CallDialogueBoxLayer     callBox;
    private TopBarComponents         topBar;
    private SettingsPanel            settings;

    public ShiftPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
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

        InventoryPanel inventory = new InventoryPanel(mainPanel);
        topBar.setInventoryPanel(inventory);

        callBox = new CallDialogueBoxLayer();

        // Pause around settings, save stats on resume
        topBar.onSettingsOpening(() -> callBox.pauseTimer());
        topBar.onSettingsClosed(() -> {
            saveStats();
            callBox.resumeTimer();
        });

        // Points from each call → top bar
        callBox.onPointsAwarded((pp, salary) -> {
            topBar.addPpPoints(pp);
            topBar.addSalaryPoints(salary);
        });

        callBox.onCallComplete(() -> {
            if (callBox.hasMoreCalls()) {
                Timer timer = new Timer(100, e -> {
                    callBox.loadTest();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                saveStats();
                Timer timer = new Timer(100, e -> { 
                    mainPanel.onCallComplete();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        add(topBar);
        add(callBox);
        add(bg);
    }

    /**
     * Called by MainFrame.showScreen("shift").
     * Restores stats, resets call pool, loads first call.
     */
    public void loadCall() {
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
