package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.io.RecipeTextFileReader;
import com.adolfomartinez.recipescaler.io.RecipeTextFormatter;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

// Open a recipe and preview plain text, then print
public class ExportPanel extends JPanel {

    private static final String DIR_SAVED = "saved-recipes";
    private static final String DIR_SCALED = "saved-scaled-recipes";

    public ExportPanel(GuiManager frame) {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        final Recipe[] loaded = new Recipe[1];

        JPanel north = new JPanel(new BorderLayout(0, 10));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JLabel folderLabel = new JLabel("Recipes folder:");
        JComboBox<String> folderCombo = new JComboBox<>(new String[]{DIR_SAVED, DIR_SCALED});
        folderCombo.setSelectedItem(DIR_SAVED);

        JButton browseButton = new JButton("Browse recipe…");
        JButton printButton = new JButton("Print…");
        printButton.setEnabled(false);

        JLabel fileChip = new JLabel("No file loaded");
        JLabel hint = new JLabel("Pick folder, browse a .txt, then print.");

        toolbar.add(folderLabel);
        toolbar.add(folderCombo);
        toolbar.add(browseButton);
        toolbar.add(printButton);
        toolbar.add(fileChip);

        JPanel hintRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC));
        hintRow.add(hint);

        north.add(toolbar, BorderLayout.NORTH);
        north.add(hintRow, BorderLayout.SOUTH);

        JTextArea preview = new JTextArea();
        preview.setEditable(false);
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, preview.getFont().getSize());
        preview.setFont(mono);
        preview.setText("Choose a recipes folder above, then browse to load a recipe .txt.");

        JScrollPane scroll = new JScrollPane(preview);
        scroll.setPreferredSize(new Dimension(420, 280));
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Recipe text (print layout)"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose recipe to print");
            chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

            String folderKey = folderCombo.getItemAt(folderCombo.getSelectedIndex());
            File start = new File(folderKey);
            if (start.isDirectory()) {
                chooser.setCurrentDirectory(start);
            } else {
                chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            }

            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();
            try {
                Recipe recipe = RecipeTextFileReader.readRecipe(file);
                loaded[0] = recipe;
                fileChip.setText(file.getName());
                preview.setText(RecipeTextFormatter.formatExport(recipe));
                preview.setCaretPosition(0);
                printButton.setEnabled(true);
            } catch (IllegalArgumentException | IOException ex) {
                loaded[0] = null;
                fileChip.setText("No file loaded");
                preview.setText("");
                printButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot load recipe",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        printButton.addActionListener(e -> {
            if (loaded[0] == null) {
                JOptionPane.showMessageDialog(this, "Browse and load a recipe first.",
                        "Nothing to print", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                preview.print(null, null, true, null, null, true);
            } catch (PrinterException | SecurityException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not print: " + ex.getMessage(),
                        "Print error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(ev -> frame.showScreen(GuiManager.MAIN_SCREEN));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        south.add(backButton);

        add(north, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}
