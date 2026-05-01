package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Piece 1: pick a recipe .txt file and preview its contents. (Parsing / scaling comes later.)
 */
public class ScaleRecipePanel extends JPanel {

    public ScaleRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout(10, 10));

        // Edit panel uses boxed grids — here we use a simple horizontal “toolbar” instead.
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        JButton browseButton = new JButton("Browse recipe…");
        JLabel fileBadge = new JLabel("No file selected");
        toolbar.add(browseButton);
        toolbar.add(fileBadge);

        JTextArea preview = new JTextArea();
        preview.setEditable(false);
        Font baseFont = preview.getFont();
        preview.setFont(new Font(Font.MONOSPACED, Font.PLAIN, baseFont.getSize()));

        preview.setText("Choose a recipe .txt file to preview it here.");
        JPanel previewWrap = new JPanel(new BorderLayout());
        previewWrap.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        JLabel heading = new JLabel("File preview");
        heading.setBorder(BorderFactory.createEmptyBorder(0, 16, 4, 0));
        previewWrap.add(heading, BorderLayout.NORTH);
        previewWrap.add(new JScrollPane(preview), BorderLayout.CENTER);

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select recipe file");
            chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
            chooser.setCurrentDirectory(new File("saved-recipes"));

            int pick = chooser.showOpenDialog(this);
            if (pick != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();
            fileBadge.setText(file.getName());
            try {
                preview.setText(Files.readString(file.toPath(), StandardCharsets.UTF_8));
                preview.setCaretPosition(0);
            } catch (IOException ex) {
                preview.setText("");
                JOptionPane.showMessageDialog(this,
                        "Could not read file: " + ex.getMessage(),
                        "Read error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(previewWrap, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        south.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> frame.showScreen(GuiManager.MAIN_SCREEN));
        south.add(backButton);
        add(south, BorderLayout.SOUTH);
    }
}
