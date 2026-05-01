package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import javax.swing.*;

public class ScaleRecipePanel extends JPanel {
    public ScaleRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea(10, 30);
        area.setText("Scale Recipe — UI coming next.");

        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        add(backButton, BorderLayout.SOUTH);
    }
}
