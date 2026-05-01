package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.model.MeasurementUnit;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EditRecipePanel extends JPanel {
    private static final Pattern INGREDIENT_PATTERN =
            Pattern.compile("^(.+):\\s*([0-9]+(?:\\.[0-9]+)?)\\s+(.+)$");

    public EditRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout(0, 12));

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        JButton addFileButton = new JButton("Add File");
        JLabel selectedFileLabel = new JLabel("No recipe file selected");
        JPanel recipeInfoPanel = new JPanel(new java.awt.GridLayout(2, 2, 10, 10));
        JTextField recipeNameField = new JTextField();
        JTextField baseServingsField = new JTextField();
        recipeNameField.setEditable(false);
        baseServingsField.setEditable(false);

        recipeInfoPanel.add(new JLabel("Recipe Name:"));
        recipeInfoPanel.add(recipeNameField);
        recipeInfoPanel.add(new JLabel("Base Servings:"));
        recipeInfoPanel.add(baseServingsField);

        JPanel fileSelectionPanel = new JPanel(new BorderLayout(8, 0));
        fileSelectionPanel.add(addFileButton, BorderLayout.WEST);
        fileSelectionPanel.add(selectedFileLabel, BorderLayout.CENTER);

        topPanel.add(fileSelectionPanel, BorderLayout.NORTH);
        topPanel.add(recipeInfoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Ingredient", "Amount", "Unit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable ingredientsTable = new JTable(model);
        JComboBox<MeasurementUnit> unitComboBox = new JComboBox<>(MeasurementUnit.values());
        ingredientsTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(unitComboBox));
        add(new JScrollPane(ingredientsTable), BorderLayout.CENTER);

        addFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Recipe File");
            chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            chooser.setCurrentDirectory(new File("saved-recipes"));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                selectedFileLabel.setText(selectedFile.getName());
                try {
                    loadRecipeFromFile(selectedFile, recipeNameField, baseServingsField, model);
                } catch (IllegalArgumentException | IOException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Could not load recipe file: " + ex.getMessage(),
                            "Load Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        add(backButton, BorderLayout.SOUTH);
    }

    private void loadRecipeFromFile(
            File selectedFile,
            JTextField recipeNameField,
            JTextField baseServingsField,
            DefaultTableModel model) throws IOException {
        List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
        if (lines.size() < 4) {
            throw new IllegalArgumentException("Recipe file is incomplete.");
        }

        String recipeName = parseHeaderValue(lines.get(0), "Recipe Name:");
        String baseServings = parseHeaderValue(lines.get(1), "Base Servings:");

        recipeNameField.setText(recipeName);
        baseServingsField.setText(baseServings);
        model.setRowCount(0);

        for (String line : lines) {
            if (!line.startsWith("- ")) {
                continue;
            }

            String ingredientLine = line.substring(2).trim();
            Matcher matcher = INGREDIENT_PATTERN.matcher(ingredientLine);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid ingredient line: " + line);
            }

            String ingredientName = matcher.group(1).trim();
            String amount = matcher.group(2).trim();
            String unitLabel = matcher.group(3).trim();
            MeasurementUnit unit = unitFromLabel(unitLabel);

            model.addRow(new Object[]{ingredientName, amount, unit});
        }
    }

    private String parseHeaderValue(String line, String prefix) {
        if (!line.startsWith(prefix)) {
            throw new IllegalArgumentException("Missing header: " + prefix);
        }
        return line.substring(prefix.length()).trim();
    }

    private MeasurementUnit unitFromLabel(String unitLabel) {
        for (MeasurementUnit unit : MeasurementUnit.values()) {
            if (unit.toString().equalsIgnoreCase(unitLabel) || unit.name().equalsIgnoreCase(unitLabel)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown measurement unit: " + unitLabel);
    }
}
