package GUI.panels;

import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.universalComponents.TopBarComponents;
import GUI.panels.shopComponents.ShopLayer;
import Entities.Item;
import Entities.ItemUse;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class ShopPanel extends JPanel {

    private final MainFrame     mainPanel;
    private final SettingsPanel settings;
    private final InventoryPanel inventory;
     private ItemUse itemUse;   
    private BackgroundLayer  bg;
    private TopBarComponents topBar;
    private ShopLayer        shopBox;
    
    public ItemUse getItemUse() {
        return itemUse;
    }
    public ShopPanel(MainFrame mainPanel, SettingsPanel sharedSettings, InventoryPanel inventory) {
        this.mainPanel = mainPanel;
        this.settings  = sharedSettings;
        this.inventory = inventory;
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }

    public void loadShop() {
        topBar.updateForShop(mainPanel.getCurrentDay());
        topBar.setPpValue(mainPanel.getPP());
        topBar.setLpValue(mainPanel.getLP());
        topBar.setSalaryValue(mainPanel.getSalary());
        shopBox.load();
        shopBox.setPlayerFunds(mainPanel.getSalary());
        shopBox.setNavButtonImages("back.png", "next.png");

        shopBox.setOnPurchase((item, cost) -> {
            topBar.setSalaryValue(shopBox.getPlayerFunds());
            item.setQuantity(1);   
            inventory.addItem(item);
            topBar.refreshInventoryButton();
        });

        shopBox.setOnExit(() -> {
            mainPanel.setSalary(shopBox.getPlayerFunds());  
            saveStats();
            mainPanel.onShopComplete();
        });
    }
    
    private void saveStats() {
        mainPanel.setPP(topBar.getCurrentPpValue());
        mainPanel.setSalary(shopBox.getPlayerFunds());  
    }

    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("ConvenienceStore.jpg");

        topBar = new TopBarComponents(mainPanel);
        topBar.setSettingsPanel(settings);
        topBar.setParentScreen("shop");
        topBar.setInventoryPanel(inventory);
        shopBox = new ShopLayer();

        itemUse = new ItemUse(mainPanel, topBar, null);

        add(topBar);
        add(shopBox);
        add(bg);
    }
    
    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
