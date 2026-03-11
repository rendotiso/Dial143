/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.panels.titleScreenComponents;

/**
 *
 * @author renmaya
 */

import javax.swing.*;
import java.awt.*;

public class TitleScreenComponents extends JPanel {

    private JLabel titleLabel;
    private JButton btnPlay;
    private JButton btnSave;
    private JButton btnExit;

    private Runnable onPlay;
    private Runnable onSave;
    private Runnable onExit;

    public TitleScreenComponents() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(1280, 720));
        initComponents();
    }

    private void initComponents() {
        titleLabel = new JLabel("DIAL 143");
        titleLabel.setFont(new Font("Jomolhari", Font.ITALIC, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(58, 78, 294, 216);
        add(titleLabel);

        btnPlay = new JButton("New Game");
        btnPlay.setBounds(88, 444, 236, 30);
        btnPlay.addActionListener(e -> { if (onPlay != null) onPlay.run(); });
        btnPlay.setFocusable(false);
        add(btnPlay);

        btnSave = new JButton("Save");
        btnSave.setBounds(88, 492, 236, 30);
        btnSave.addActionListener(e -> { if (onSave != null) onSave.run(); });
        btnSave.setFocusable(false);
        add(btnSave);

        btnExit = new JButton("Exit");
        btnExit.setBounds(88, 540, 236, 30);
        btnExit.addActionListener(e -> { if (onExit != null) onExit.run(); });
        btnExit.setFocusable(false);
        add(btnExit);
    }

    public void setPlayAction(Runnable action)  { this.onPlay = action; }
    public void setSaveAction(Runnable action)  { this.onSave = action; }
    public void setExitAction(Runnable action)  { this.onExit = action; }
}