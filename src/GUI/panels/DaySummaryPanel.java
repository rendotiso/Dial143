package GUI.panels;

import GUI.panels.universalComponents.*;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class DaySummaryPanel extends JPanel {

    private final MainFrame     mainPanel;
    private final SettingsPanel settings;

    private BackgroundLayer  bg;
    private TopBarComponents topBar;
    private DaySummaryLayer  summaryLayer;

    public DaySummaryPanel(MainFrame mainPanel, SettingsPanel sharedSettings) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;

        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }

    // ── Called by MainFrame ───────────────────────────────────────────────────

    public void loadSummary(int ppGained, int lpGained, int salaryGained,
                            int callsDone, Runnable onEndDay, Runnable onGoToShop) {
        topBar.setPpValue(mainPanel.getPP());
        topBar.setLpValue(mainPanel.getLP());
        topBar.setSalaryValue(mainPanel.getSalary());
        topBar.setDayInfo(mainPanel.getCurrentDay(), "Day Summary");

        summaryLayer.load(
            mainPanel.getCurrentDay(),
            ppGained, lpGained, salaryGained, callsDone,
            mainPanel.getPP(), mainPanel.getLP(), mainPanel.getSalary(),
            onEndDay, onGoToShop
        );
    }

    // ── Layer setup ───────────────────────────────────────────────────────────

    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("blackBG.jpg");

        topBar = new TopBarComponents(mainPanel);
        topBar.setSettingsPanel(settings);
        topBar.setParentScreen("daySummary");

        InventoryPanel inventory = new InventoryPanel(mainPanel);
        topBar.setInventoryPanel(inventory);

        summaryLayer = new DaySummaryLayer();

        add(topBar);
        add(summaryLayer);
        add(bg);
    }
}