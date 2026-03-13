package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import javax.swing.*;

public class FourthPanel extends JPanel {
    public FourthPanel(GuiManager frame) {
        // give the panel a layout so added components are visible
        setLayout(new java.awt.BorderLayout());

        JTextArea area = new JTextArea(10, 30);
        area.setText("Fourth Screeeeeeen!");

        // add the text area (wrapped in scroll pane) to this panel
        add(new JScrollPane(area), java.awt.BorderLayout.CENTER);

        // create back button
        JButton backButton = new JButton("Back to Menu");

        // add action listener when clicked goes back to main menu
        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        add(backButton, BorderLayout.SOUTH);
    }
}
