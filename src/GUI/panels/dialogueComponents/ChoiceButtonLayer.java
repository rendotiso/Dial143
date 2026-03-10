package GUI.panels.dialogueComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChoiceButtonLayer extends JPanel {
    
    private List<ChoiceButton> choiceButtons;
    private ChoiceListener listener;
    private Color unlockedColor = new Color(70, 70, 70, 220);
    private Color lockedColor = new Color(40, 40, 40, 180);
    private Color hoverColor = new Color(100, 100, 100, 240);
    private Color textUnlocked = Color.WHITE;
    private Color textLocked = new Color(150, 150, 150);
    
    public interface ChoiceListener {
        void onChoiceSelected(String choiceText, String nextNode);
    }
    
    private class ChoiceButton extends JButton {
        private String nextNode;
        private boolean unlocked;
        private Color normalColor;
        
        public ChoiceButton(String text, String nextNode, boolean unlocked) {
            super(text);
            this.nextNode = nextNode;
            this.unlocked = unlocked;
            this.normalColor = unlocked ? unlockedColor : lockedColor;
            
            setFont(new Font("Arial", Font.PLAIN, 18));
            setForeground(unlocked ? textUnlocked : textLocked);
            setBackground(normalColor);
            setFocusPainted(false);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(unlocked ? new Color(200, 200, 200, 100) : new Color(100, 100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            setContentAreaFilled(false);
            setOpaque(true);
            
            if (unlocked) {
                // Add hover effect for unlocked buttons
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        setBackground(hoverColor);
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        setBackground(normalColor);
                    }
                });
            } else {
                // Add lock icon indicator for locked choices
                setText("🔒 " + text);
            }
        }
        
        public String getNextNode() { return nextNode; }
        public boolean isUnlocked() { return unlocked; }
    }
    
    public ChoiceButtonLayer() {
        setBounds(0, 0, 1280, 720);
        setLayout(null);
        setOpaque(false);
        choiceButtons = new ArrayList<>();
        setVisible(false);
    }
    
    @Override public Dimension getMinimumSize() { return new Dimension(1280, 720); }
    @Override public Dimension getMaximumSize() { return new Dimension(1280, 720); }
    @Override public Dimension getPreferredSize() { return new Dimension(1280, 720); }

    public void addChoice(String text, String nextNode) {
        addChoice(text, nextNode, true); // Default to unlocked
    }
    
    public void addChoice(String text, String nextNode, boolean unlocked) {
        ChoiceButton btn = new ChoiceButton(text, nextNode, unlocked);
        
        if (unlocked) {
            btn.addActionListener(e -> {
                if (listener != null) {
                    listener.onChoiceSelected(btn.getText(), btn.getNextNode());
                }
                setVisible(false);
            });
        } else {
            // Locked buttons don't do anything when clicked
            btn.addActionListener(e -> {
                // Play error sound or show message
                System.out.println("This choice is locked!");
                // You could also show a tooltip or temporary message
            });
        }
        
        choiceButtons.add(btn);
        add(btn);
    }
    
    public void clearChoices() {
        for (ChoiceButton btn : choiceButtons) {
            remove(btn);
        }
        choiceButtons.clear();
    }
    
    public void showChoices() {
        int numChoices = choiceButtons.size();
        
        // Calculate positions based on number of choices
        int centerX = 640; // Center of screen (1280/2)
        int startY;
        int buttonWidth = 400;
        int buttonHeight = 60;
        int spacing = 15;
        
        switch(numChoices) {
            case 2:
                startY = 300;
                positionButtons(startY, buttonWidth, buttonHeight, spacing);
                break;
            case 3:
                startY = 250;
                positionButtons(startY, buttonWidth, buttonHeight, spacing);
                break;
            case 4:
                startY = 200;
                positionButtons(startY, buttonWidth, buttonHeight, spacing);
                break;
            case 5:
                startY = 150;
                positionButtons(startY, buttonWidth, buttonHeight, spacing);
                break;
            default:
                // Default positioning
                startY = 200;
                positionButtons(startY, buttonWidth, buttonHeight, spacing);
        }
        
        setVisible(true);
        repaint();
    }
    
    private void positionButtons(int startY, int width, int height, int spacing) {
        int centerX = 640;
        
        for (int i = 0; i < choiceButtons.size(); i++) {
            ChoiceButton btn = choiceButtons.get(i);
            int x = centerX - (width / 2);
            int y = startY + (i * (height + spacing));
            btn.setBounds(x, y, width, height);
        }
    }
    
    public void hideChoices() {
        setVisible(false);
    }
    
    public void setChoiceListener(ChoiceListener listener) {
        this.listener = listener;
    }
    
    // Method to update a choice's locked/unlocked state
    public void setChoiceLocked(int index, boolean locked) {
        if (index >= 0 && index < choiceButtons.size()) {
            ChoiceButton btn = choiceButtons.get(index);
            btn.unlocked = !locked;
            btn.normalColor = btn.unlocked ? unlockedColor : lockedColor;
            btn.setBackground(btn.normalColor);
            btn.setForeground(btn.unlocked ? textUnlocked : textLocked);
            
            // Update button text with lock icon if locked
            String currentText = btn.getText().replace("🔒 ", "");
            btn.setText(btn.unlocked ? currentText : "🔒 " + currentText);
            
            // Remove old action listeners and add new ones based on state
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            
            if (btn.unlocked) {
                btn.addActionListener(e -> {
                    if (listener != null) {
                        listener.onChoiceSelected(btn.getText(), btn.getNextNode());
                    }
                    setVisible(false);
                });
            }
        }
    }
    
    // Method to check if a choice is unlocked
    public boolean isChoiceUnlocked(int index) {
        if (index >= 0 && index < choiceButtons.size()) {
            return choiceButtons.get(index).isUnlocked();
        }
        return false;
    }
    
    // Method to get the number of choices
    public int getChoiceCount() {
        return choiceButtons.size();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (isVisible()) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Darken the background behind choices
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.dispose();
        }
    }
    
    // Custom methods for different choice configurations
    
    public void show2Choices(String choice1, String node1, 
                             String choice2, String node2) {
        clearChoices();
        addChoice(choice1, node1);
        addChoice(choice2, node2);
        showChoices();
    }
    
    public void show3Choices(String choice1, String node1,
                             String choice2, String node2,
                             String choice3, String node3) {
        clearChoices();
        addChoice(choice1, node1);
        addChoice(choice2, node2);
        addChoice(choice3, node3);
        showChoices();
    }
    
    public void show4Choices(String choice1, String node1,
                             String choice2, String node2,
                             String choice3, String node3,
                             String choice4, String node4) {
        clearChoices();
        addChoice(choice1, node1);
        addChoice(choice2, node2);
        addChoice(choice3, node3);
        addChoice(choice4, node4);
        showChoices();
    }
    
    public void show5Choices(String choice1, String node1,
                             String choice2, String node2,
                             String choice3, String node3,
                             String choice4, String node4,
                             String choice5, String node5) {
        clearChoices();
        addChoice(choice1, node1);
        addChoice(choice2, node2);
        addChoice(choice3, node3);
        addChoice(choice4, node4);
        addChoice(choice5, node5);
        showChoices();
    }
    
    // Methods for choices with locked states
    
    public void show2ChoicesWithLock(String choice1, String node1, boolean unlocked1,
                                     String choice2, String node2, boolean unlocked2) {
        clearChoices();
        addChoice(choice1, node1, unlocked1);
        addChoice(choice2, node2, unlocked2);
        showChoices();
    }
    
    public void show3ChoicesWithLock(String choice1, String node1, boolean unlocked1,
                                     String choice2, String node2, boolean unlocked2,
                                     String choice3, String node3, boolean unlocked3) {
        clearChoices();
        addChoice(choice1, node1, unlocked1);
        addChoice(choice2, node2, unlocked2);
        addChoice(choice3, node3, unlocked3);
        showChoices();
    }
    
    public void show4ChoicesWithLock(String choice1, String node1, boolean unlocked1,
                                     String choice2, String node2, boolean unlocked2,
                                     String choice3, String node3, boolean unlocked3,
                                     String choice4, String node4, boolean unlocked4) {
        clearChoices();
        addChoice(choice1, node1, unlocked1);
        addChoice(choice2, node2, unlocked2);
        addChoice(choice3, node3, unlocked3);
        addChoice(choice4, node4, unlocked4);
        showChoices();
    }
    
    public void show5ChoicesWithLock(String choice1, String node1, boolean unlocked1,
                                     String choice2, String node2, boolean unlocked2,
                                     String choice3, String node3, boolean unlocked3,
                                     String choice4, String node4, boolean unlocked4,
                                     String choice5, String node5, boolean unlocked5) {
        clearChoices();
        addChoice(choice1, node1, unlocked1);
        addChoice(choice2, node2, unlocked2);
        addChoice(choice3, node3, unlocked3);
        addChoice(choice4, node4, unlocked4);
        addChoice(choice5, node5, unlocked5);
        showChoices();
    }
    
    // Setters for customizing colors
    public void setUnlockedColor(Color color) {
        this.unlockedColor = color;
    }
    
    public void setLockedColor(Color color) {
        this.lockedColor = color;
    }
    
    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }
    
    public void setTextColors(Color unlocked, Color locked) {
        this.textUnlocked = unlocked;
        this.textLocked = locked;
    }
}