package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.MainFrame;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainFrame frame) {
        JButton goButton = new JButton("Go to Second Screen");
        JButton goButton = new JButton("Go to Second Screen");
        JButton goButton = new JButton("Go to Second Screen");
        JButton goButton = new JButton("Go to Second Screen");


        goButton.addActionListener(e -> {
            frame.showScreen("SECOND");
        });

        add(goButton);
    }
}