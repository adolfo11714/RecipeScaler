package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ThirdPanel extends JPanel {
    public ThirdPanel(GuiManager frame) {
        setLayout(new BorderLayout(0, 8));

        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        JButton addFileButton = new JButton("Add File");
        JLabel selectedFileLabel = new JLabel("No recipe file selected");

        topPanel.add(addFileButton, BorderLayout.WEST);
        topPanel.add(selectedFileLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JTextArea area = new JTextArea(10, 30);
        area.setText("Select a recipe .txt file to begin editing.");
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        addFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Recipe File");
            chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            chooser.setCurrentDirectory(new File("saved-recipes"));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                selectedFileLabel.setText(selectedFile.getName());
                area.setText("Selected recipe file:\n" + selectedFile.getAbsolutePath());
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        add(backButton, BorderLayout.SOUTH);
    }
}
