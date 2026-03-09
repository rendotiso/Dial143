package dial143;

import GUI.panels.MainFrame;
import java.awt.EventQueue;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author renmaya
 */
public class Main {
public static void main(String args[]) {
    EventQueue.invokeLater(new Runnable() {
        public void run() {
            MainFrame frame = new MainFrame();
            
            // Ensure frame has size
            frame.pack();            
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            java.awt.Dimension frameSize = frame.getSize();
            
            // Calculate center position
            int x = (screenSize.width - frameSize.width) / 2;
            int y = (screenSize.height - frameSize.height) / 2;
            
            // Force position
            frame.setLocation(x, y);
            frame.setVisible(true);
            
        }
    });
}
    
}
