package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.MainFrame;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainFrame frame) {
        JButton secondButton = new JButton("Go to Second Screen");
        JButton thirdButton = new JButton("Go to Third Screen");
        JButton fourthButton = new JButton("Go to Fourth Screen");
        JButton fifthButton = new JButton("Go to Fifth Screen");


        secondButton.addActionListener(e -> {
            frame.showScreen("SECOND");
        });

        thirdButton.addActionListener(e -> {
            frame.showScreen("THIRD");
        });

        fourthButton.addActionListener(e -> {
            frame.showScreen("FOURTH");
        });

        fifthButton.addActionListener(e -> {
            frame.showScreen("FIFTH");
        });

        add(secondButton);
        add(thirdButton);
        add(fourthButton);
        add(fifthButton);

    }
}