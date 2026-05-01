package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import com.adolfomartinez.recipescaler.io.RecipeTextFileReader;
import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.Recipe;
import com.adolfomartinez.recipescaler.service.RecipeScaler;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;


// Load a recipe file (base servings come from the file), enter how many servings you want, then scale amounts
public class ScaleRecipePanel extends JPanel {

    public ScaleRecipePanel(GuiManager frame) {
        setLayout(new BorderLayout(10, 10));

        final Recipe[] loadedRecipe = new Recipe[1];

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        JButton browseButton = new JButton("Browse recipe…");
        JLabel fileBadge = new JLabel("No file selected");
        toolbar.add(browseButton);
        toolbar.add(fileBadge);

        JPanel scaleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        scaleBar.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        scaleBar.add(new JLabel("Recipe:"));
        JLabel recipeNameLabel = new JLabel("—");
        scaleBar.add(recipeNameLabel);
        scaleBar.add(new JLabel("Base servings (from file):"));
        JLabel baseServingsLabel = new JLabel("—");
        scaleBar.add(baseServingsLabel);
        scaleBar.add(new JLabel("Desired servings:"));
        JTextField desiredServingsField = new JTextField(5);
        scaleBar.add(desiredServingsField);
        JButton scaleButton = new JButton("Scale ingredients");
        scaleBar.add(scaleButton);
        JLabel factorLabel = new JLabel(" ");
        scaleBar.add(factorLabel);

        JPanel northStack = new JPanel(new BorderLayout());
        northStack.add(toolbar, BorderLayout.NORTH);
        northStack.add(scaleBar, BorderLayout.SOUTH);

        JTextArea preview = new JTextArea();
        preview.setEditable(false);
        Font baseFont = preview.getFont();
        preview.setFont(new Font(Font.MONOSPACED, Font.PLAIN, baseFont.getSize()));
        preview.setText("Choose a recipe .txt file to preview it here.");

        DefaultTableModel scaledModel = new DefaultTableModel(
                new String[]{"Ingredient", "Scaled amount", "Unit"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable scaledTable = new JTable(scaledModel);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(preview),
                new JScrollPane(scaledTable));
        split.setResizeWeight(0.45);
        split.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));

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
            loadedRecipe[0] = null;
            baseServingsLabel.setText("—");
            scaledModel.setRowCount(0);
            factorLabel.setText(" ");
            try {
                String raw = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                preview.setText(raw);
                preview.setCaretPosition(0);

                Recipe recipe = RecipeTextFileReader.readRecipe(file);
                loadedRecipe[0] = recipe;
                recipeNameLabel.setText(recipe.getName());
                baseServingsLabel.setText(Integer.toString(recipe.getBaseServings()));
                desiredServingsField.setText("");
            } catch (IllegalArgumentException ex) {
                preview.setText("");
                baseServingsLabel.setText("—");
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid recipe file",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                preview.setText("");
                baseServingsLabel.setText("—");
                JOptionPane.showMessageDialog(this, "Could not read file: " + ex.getMessage(),
                        "Read error", JOptionPane.ERROR_MESSAGE);
            }
        });

        scaleButton.addActionListener(e -> {
            if (loadedRecipe[0] == null) {
                JOptionPane.showMessageDialog(this, "Browse and load a valid recipe first.",
                        "No recipe", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int targetServings;
            try {
                targetServings = Integer.parseInt(desiredServingsField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Desired servings must be a whole number.",
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int baseFromFile = loadedRecipe[0].getBaseServings();
            try {
                double factor = RecipeScaler.scalingFactor(baseFromFile, targetServings);
                factorLabel.setText(String.format("Factor: %.4g×", factor));

                scaledModel.setRowCount(0);
                for (Ingredient ing : RecipeScaler.scaleForServings(
                        loadedRecipe[0], baseFromFile, targetServings)) {
                    scaledModel.addRow(new Object[]{ing.getName(), ing.getAmount(), ing.getUnit()});
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot scale",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        add(northStack, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        south.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> frame.showScreen(GuiManager.MAIN_SCREEN));
        south.add(backButton);
        add(south, BorderLayout.SOUTH);
    }
}
