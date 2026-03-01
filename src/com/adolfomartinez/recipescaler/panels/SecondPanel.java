package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import javax.swing.*;

public class SecondPanel extends JPanel {
    public SecondPanel(GuiManager frame) {
        // give the panel a layout so added components are visible
        setLayout(new java.awt.BorderLayout());

        JTextArea area = new JTextArea(10, 30);
        area.setText("Second Screeeeeeen!");

        // add the text area (wrapped in scroll pane) to this panel
        add(new JScrollPane(area), java.awt.BorderLayout.CENTER);
    }
}
