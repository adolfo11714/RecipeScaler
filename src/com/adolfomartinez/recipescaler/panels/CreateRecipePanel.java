package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.MeasurementUnit;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CreateRecipePanel extends JPanel {

    public CreateRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        // ===== TOP FORM =====
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel nameLabel = new JLabel("Recipe Name:");
        JTextField nameField = new JTextField();

        JLabel servingsLabel = new JLabel("Base Servings:");
        JTextField servingsField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(servingsLabel);
        formPanel.add(servingsField);

        add(formPanel, BorderLayout.NORTH);

        // ===== INGREDIENT TABLE =====
        String[] columns = {"Ingredient", "Amount", "Unit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JComboBox<MeasurementUnit> unitComboBox = new JComboBox<>(MeasurementUnit.values());
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(unitComboBox));

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(tableScrollPane, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton addIngredient = new JButton("Add Ingredient");
        JButton removeIngredient = new JButton("Remove Ingredient");
        JButton resetRecipe = new JButton("Reset Recipe");
        JButton saveRecipe = new JButton("Save Recipe");
        JButton backButton = new JButton("Back to Menu");

        buttonPanel.add(addIngredient);
        buttonPanel.add(removeIngredient);
        buttonPanel.add(resetRecipe);
        buttonPanel.add(saveRecipe);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== BUTTON LOGIC =====

        addIngredient.addActionListener(e -> {
            model.addRow(new Object[]{"", "", MeasurementUnit.TEASPOON});
        });

        removeIngredient.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
            }
        });

        resetRecipe.addActionListener(e -> {
            nameField.setText("");
            servingsField.setText("");
            model.setRowCount(0);
        });

        backButton.addActionListener(e -> {
            frame.showScreen(GuiManager.MAIN_SCREEN);
        });

        saveRecipe.addActionListener(e -> {
            try {
                Recipe recipe = buildRecipeFromForm(nameField, servingsField, model);
                JOptionPane.showMessageDialog(this,
                        "Recipe validated: " + recipe.getName() + " (" + recipe.getIngredients().size() + " ingredients)");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Recipe", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private Recipe buildRecipeFromForm(JTextField nameField, JTextField servingsField, DefaultTableModel model) {
        String recipeName = nameField.getText() == null ? "" : nameField.getText().trim();
        if (recipeName.isEmpty()) {
            throw new IllegalArgumentException("Recipe name is required.");
        }

        int baseServings;
        try {
            baseServings = Integer.parseInt(servingsField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Base servings must be a whole number.");
        }
        if (baseServings <= 0) {
            throw new IllegalArgumentException("Base servings must be greater than 0.");
        }

        List<Ingredient> ingredients = new ArrayList<>();
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

    private String valueAsTrimmedString(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
