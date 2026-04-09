package GUI.panels;

import GUI.panels.universalComponents.*;
import GUI.panels.InventoryPanel;
import Entities.Item;
import Entities.ActiveEffects;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class DaySummaryPanel extends JPanel {

    private final MainFrame     mainPanel;
    private final SettingsPanel settings;
    private final InventoryPanel inventory;

    private BackgroundLayer  bg;
    private TopBarComponents topBar;
    private DaySummaryLayer  summaryLayer;

    public DaySummaryPanel(MainFrame mainPanel, SettingsPanel sharedSettings, InventoryPanel inventory) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        this.inventory = inventory;
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }
    
    public void loadSummary(int ppGained, int lpGained, int salaryGained,
                            int callsDone, Runnable onEndDay, Runnable onGoToShop) {
        topBar.setPpValue(mainPanel.getPP());
        topBar.setLpValue(mainPanel.getLP());
        topBar.setSalaryValue(mainPanel.getSalary());
        topBar.setDayInfo(mainPanel.getCurrentDay(), "Day Summary");
        topBar.updateForSummary(mainPanel.getCurrentDay());

        summaryLayer.load(
            mainPanel.getCurrentDay(),
            ppGained, lpGained, salaryGained, callsDone,
            mainPanel.getPP(), mainPanel.getLP(), mainPanel.getSalary(),
            onEndDay, onGoToShop
        );
    }

    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("lightBG.jpg");

        topBar = new TopBarComponents(mainPanel);
        topBar.setSettingsPanel(settings);
        topBar.setParentScreen("daySummary");
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

        summaryLayer = new DaySummaryLayer();

        add(topBar);
        add(summaryLayer);
        add(bg);
    }
}