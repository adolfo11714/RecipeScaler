package com.adolfomartinez.recipescaler.panels;

import com.adolfomartinez.recipescaler.GuiManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import javax.swing.*;

public class MainMenuPanel extends JPanel {

    private JButton editRecipeButton;
    private JButton scaleRecipeButton;
    private JButton exportButton;

    public MainMenuPanel(GuiManager frame) {

        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Setting button size
        Dimension buttonSize = new Dimension(400, 70);

        JButton createRecipeButton = new JButton("Create Recipe");
        editRecipeButton = new JButton("Edit Recipe");
        scaleRecipeButton = new JButton("Scale Recipe");
        exportButton = new JButton("Export Recipe");

        // Center Buttons (BoxLayout needs this)
        createRecipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editRecipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scaleRecipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Center vertically
        menu.add(Box.createVerticalGlue());
        menu.add(createRecipeButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(editRecipeButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(scaleRecipeButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(exportButton);
        menu.add(Box.createVerticalGlue());

        add(menu, BorderLayout.CENTER);

        // Set button size
        createRecipeButton.setPreferredSize(buttonSize);
        createRecipeButton.setMaximumSize(buttonSize);
        createRecipeButton.setMinimumSize(buttonSize);

        editRecipeButton.setPreferredSize(buttonSize);
        editRecipeButton.setMaximumSize(buttonSize);
        editRecipeButton.setMinimumSize(buttonSize);

        scaleRecipeButton.setPreferredSize(buttonSize);
        scaleRecipeButton.setMaximumSize(buttonSize);
        scaleRecipeButton.setMinimumSize(buttonSize);

        exportButton.setPreferredSize(buttonSize);
        exportButton.setMaximumSize(buttonSize);
        exportButton.setMinimumSize(buttonSize);

        refreshRecipeDependentButtons();

        // When user clicks on the button it will lead them to respective screen
        createRecipeButton.addActionListener(e -> frame.showScreen(GuiManager.CREATE_RECIPE));

        editRecipeButton.addActionListener(e -> frame.showScreen(GuiManager.EDIT_RECIPE));

        scaleRecipeButton.addActionListener(e -> frame.showScreen(GuiManager.SCALE_RECIPE));

        exportButton.addActionListener(e -> frame.showScreen(GuiManager.EXPORT_RECIPE));

        // Re-check saved-recipes/*.txt whenever this menu becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshRecipeDependentButtons();
            }
        });
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshRecipeDependentButtons();
        }
    }

    private boolean hasSavedRecipeFiles() {
        File saveDir = new File("saved-recipes");
        if (!saveDir.isDirectory()) {
            return false;
        }

        File[] recipeFiles = saveDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        return recipeFiles != null && recipeFiles.length > 0;
    }

    // Edit / Scale / Export require at least one .txt in saved-recipes
    private void refreshRecipeDependentButtons() {
        if (editRecipeButton == null) {
            return;
        }
        boolean hasSavedRecipes = hasSavedRecipeFiles();
        String tooltip = hasSavedRecipes ? null : "Add at least one .txt recipe in saved-recipes first.";
        editRecipeButton.setEnabled(hasSavedRecipes);
        editRecipeButton.setToolTipText(tooltip);
        scaleRecipeButton.setEnabled(hasSavedRecipes);
        scaleRecipeButton.setToolTipText(tooltip);
        exportButton.setEnabled(hasSavedRecipes);
        exportButton.setToolTipText(tooltip);
    }
}