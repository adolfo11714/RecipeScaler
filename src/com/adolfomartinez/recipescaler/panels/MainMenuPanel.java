package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(GuiManager frame) {

        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Setting button size
        Dimension buttonSize = new Dimension(400, 70);

        JButton secondButton = new JButton("Go to Second Screen");
        JButton thirdButton = new JButton("Go to Third Screen");
        JButton fourthButton = new JButton("Go to Fourth Screen");
        JButton fifthButton = new JButton("Go to Fifth Screen");

        // Center Buttons (BoxLayout needs this)
        secondButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        thirdButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fifthButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Center vertically
        menu.add(Box.createVerticalGlue());
        menu.add(secondButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(thirdButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(fourthButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(fifthButton);
        menu.add(Box.createVerticalGlue());

        add(menu, BorderLayout.CENTER);

        // Set button size
        secondButton.setPreferredSize(buttonSize);
        secondButton.setMaximumSize(buttonSize);
        secondButton.setMinimumSize(buttonSize);

        thirdButton.setPreferredSize(buttonSize);
        thirdButton.setMaximumSize(buttonSize);
        thirdButton.setMinimumSize(buttonSize);

        fourthButton.setPreferredSize(buttonSize);
        fourthButton.setMaximumSize(buttonSize);
        fourthButton.setMinimumSize(buttonSize);

        fifthButton.setPreferredSize(buttonSize);
        fifthButton.setMaximumSize(buttonSize);
        fifthButton.setMinimumSize(buttonSize);

        // When user clicks on the button it will lead them to respective screen
        secondButton.addActionListener(e -> frame.showScreen("SECOND"));

        thirdButton.addActionListener(e -> frame.showScreen("THIRD"));

        fourthButton.addActionListener(e -> frame.showScreen("FOURTH"));

        fifthButton.addActionListener(e -> frame.showScreen("FIFTH"));
    }
}