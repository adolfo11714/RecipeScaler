package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.model.MeasurementUnit;
import java.awt.*;
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
            model.addRow(new Object[]{"", "", MeasurementUnit.PIECE});
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
            JOptionPane.showMessageDialog(this, "Recipe saved (logic not implemented yet)");
        });
    }
}
