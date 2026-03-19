
package GUI.panels;
 
import GUI.panels.universalComponents.BackgroundLayer;
import GUI.panels.saveComponents.SaveDisplayLayer;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
 
public class SavePanel extends JPanel {
 
    private final MainFrame mainFrame;
 
    private BackgroundLayer bg;
    private SaveDisplayLayer       saveLayer;
    private String          returnScreen = "title";
 
    public SavePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new OverlayLayout(this));
        initializeLayers();
    }
 
    // ── Called by MainFrame.showSave(returnScreen) ────────────────────────────
 
    public void loadSave(String returnScreen) {
        this.returnScreen = returnScreen;
        saveLayer.setOnBack(() -> mainFrame.showScreen(returnScreen));
        saveLayer.refresh();
    }
 
    // ── Layer setup ───────────────────────────────────────────────────────────
 
    private void initializeLayers() {
        bg = new BackgroundLayer();
        bg.setBackgroundFromFile("lightBG.jpg");
 
        saveLayer = new SaveDisplayLayer(mainFrame);
 
        add(saveLayer);
        add(bg);
    }
 
    // ── Size overrides ────────────────────────────────────────────────────────
 
    @Override public Dimension getMinimumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize()   { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setVerifyInputWhenFocusTarget(false);
        setLayout(new javax.swing.OverlayLayout(this));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
