package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.MeasurementUnit;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EditRecipePanel extends JPanel {
    // Parses lines like: "Flour: 2.5 cup" regex pattern to be able to accept a certain .txt file format
    private static final Pattern INGREDIENT_PATTERN =
            Pattern.compile("^(.+):\\s*([0-9]+(?:\\.[0-9]+)?)\\s+(.+)$");

    // Builds the Edit Recipe screen and hooks up file load/save actions
    public EditRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout(0, 12));
        // Using a 1-element array allows updating the selected file inside lambda listeners
        final File[] selectedFileRef = new File[1];

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        JButton addFileButton = new JButton("Add File");
        JLabel selectedFileLabel = new JLabel("No recipe file selected");
        JPanel recipeInfoPanel = new JPanel(new java.awt.GridLayout(2, 2, 10, 10));
        JTextField recipeNameField = new JTextField();
        JTextField baseServingsField = new JTextField();
        recipeNameField.setEditable(true);
        baseServingsField.setEditable(true);

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

        JPanel tableActionsPanel = new JPanel();
        JButton addIngredientButton = new JButton("Add Ingredient");
        JButton removeIngredientButton = new JButton("Remove Ingredient");
        tableActionsPanel.add(addIngredientButton);
        tableActionsPanel.add(removeIngredientButton);

        addIngredientButton.addActionListener(e -> {
            model.addRow(new Object[]{"", "", MeasurementUnit.TEASPOON});
        });

        removeIngredientButton.addActionListener(e -> {
            int selectedRow = ingredientsTable.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            }
        });

        addFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Recipe File");
            chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            // Start users in the same folder used by CreateRecipePanel saves
            chooser.setCurrentDirectory(new File("saved-recipes"));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                selectedFileRef[0] = selectedFile;
                selectedFileLabel.setText(selectedFile.getName());
                try {
                    // Populate top fields and ingredient table from selected file
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

        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(e -> {
            if (selectedFileRef[0] == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Select a recipe file first.",
                        "No File Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Save and rename file if recipe name is changed
                File updatedFile = overwriteRecipeFile(selectedFileRef[0], recipeNameField, baseServingsField, model);
                selectedFileRef[0] = updatedFile;
                selectedFileLabel.setText(updatedFile.getName());
                JOptionPane.showMessageDialog(this, "Recipe file updated successfully.");
            } catch (IllegalArgumentException | IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not save recipe file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(tableActionsPanel);
        bottomPanel.add(saveChangesButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Reads a recipe .txt file and fills table
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
        // Clear old rows before loading new file rows
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

    // Extracts value after a required header prefix like "Recipe Name:"
    private String parseHeaderValue(String line, String prefix) {
        if (!line.startsWith(prefix)) {
            throw new IllegalArgumentException("Missing header: " + prefix);
        }
        return line.substring(prefix.length()).trim();
    }

    // Converts unit text from file into a MeasurementUnit enum
    private MeasurementUnit unitFromLabel(String unitLabel) {
        for (MeasurementUnit unit : MeasurementUnit.values()) {
            if (unit.toString().equalsIgnoreCase(unitLabel) || unit.name().equalsIgnoreCase(unitLabel)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown measurement unit: " + unitLabel);
    }

    // Overwrites recipe file content and renames file when recipe name changes
    private File overwriteRecipeFile(
            File selectedFile,
            JTextField recipeNameField,
            JTextField baseServingsField,
            DefaultTableModel model) throws IOException {
        Recipe recipe = buildRecipeFromForm(recipeNameField, baseServingsField, model);
        String fileContent = buildRecipeTextContent(recipe);
        String newFileName = sanitizeFileName(recipe.getName()) + ".txt";
        Path currentPath = selectedFile.toPath();
        Path targetPath = currentPath.resolveSibling(newFileName);

        // Write to the target filename first
        Files.writeString(targetPath, fileContent, StandardCharsets.UTF_8);

        // If renamed, remove old file so only the new name is left
        if (!currentPath.equals(targetPath)) {
            Files.deleteIfExists(currentPath);
        }

        return targetPath.toFile();
    }

    // Validates edited values and builds a Recipe object
    private Recipe buildRecipeFromForm(
            JTextField recipeNameField,
            JTextField baseServingsField,
            DefaultTableModel model) {
        String recipeName = recipeNameField.getText() == null ? "" : recipeNameField.getText().trim();
        if (recipeName.isEmpty()) {
            throw new IllegalArgumentException("Recipe name is required.");
        }

        int baseServings;
        try {
            baseServings = Integer.parseInt(baseServingsField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Base servings must be a whole number.");
        }
        if (baseServings <= 0) {
            throw new IllegalArgumentException("Base servings must be greater than 0.");
        }

        List<Ingredient> ingredients = new ArrayList<>();
        // Validate each table row and convert it into Ingredient objects
        for (int row = 0; row < model.getRowCount(); row++) {
            String ingredientName = valueAsTrimmedString(model.getValueAt(row, 0));
            if (ingredientName.isEmpty()) {
                throw new IllegalArgumentException("Ingredient name is required on row " + (row + 1) + ".");
            }

            double amount;
            try {
                amount = Double.parseDouble(valueAsTrimmedString(model.getValueAt(row, 1)));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Ingredient amount must be numeric on row " + (row + 1) + ".");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Ingredient amount must be greater than 0 on row " + (row + 1) + ".");
            }

            Object unitValue = model.getValueAt(row, 2);
            if (!(unitValue instanceof MeasurementUnit unit)) {
                throw new IllegalArgumentException("Ingredient unit is required on row " + (row + 1) + ".");
            }

            ingredients.add(new Ingredient(ingredientName, amount, unit));
        }

        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("Add at least one ingredient before saving.");
        }

        return new Recipe(recipeName, baseServings, ingredients);
    }

    // Converts Recipe into the same text format used by create/edit screens
    private String buildRecipeTextContent(Recipe recipe) {
        StringBuilder content = new StringBuilder();
        content.append("Recipe Name: ").append(recipe.getName()).append(System.lineSeparator());
        content.append("Base Servings: ").append(recipe.getBaseServings()).append(System.lineSeparator());
        content.append(System.lineSeparator());
        content.append("Ingredients:").append(System.lineSeparator());

        for (Ingredient ingredient : recipe.getIngredients()) {
            content.append("- ")
                    .append(ingredient.getName())
                    .append(": ")
                    .append(ingredient.getAmount())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(System.lineSeparator());
        }

        return content.toString();
    }

    // Safe helper for reading table values: null -> "", otherwise trimmed text
    private String valueAsTrimmedString(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    // Cleans recipe names so they can safely be used as filenames regex for avoiding invalid filenames
    private String sanitizeFileName(String recipeName) {
        String sanitized = recipeName.replaceAll("[^a-zA-Z0-9-_ ]", "").trim().replace(" ", "_");
        return sanitized.isEmpty() ? "recipe" : sanitized;
    }
}
