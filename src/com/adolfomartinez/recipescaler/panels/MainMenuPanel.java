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
    public MainMenuPanel(GuiManager frame) {

        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Setting button size
        Dimension buttonSize = new Dimension(400, 70);

        JButton createRecipeButton = new JButton("Create Recipe");
        JButton editRecipeButton = new JButton("Edit Recipe");
        JButton fourthButton = new JButton("Go to Fourth Screen");
        JButton fifthButton = new JButton("Go to Fifth Screen");

        // Center Buttons (BoxLayout needs this)
        createRecipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editRecipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fifthButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Center vertically
        menu.add(Box.createVerticalGlue());
        menu.add(createRecipeButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(editRecipeButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(fourthButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(fifthButton);
        menu.add(Box.createVerticalGlue());

        add(menu, BorderLayout.CENTER);

        // Set button size
        createRecipeButton.setPreferredSize(buttonSize);
        createRecipeButton.setMaximumSize(buttonSize);
        createRecipeButton.setMinimumSize(buttonSize);

        editRecipeButton.setPreferredSize(buttonSize);
        editRecipeButton.setMaximumSize(buttonSize);
        editRecipeButton.setMinimumSize(buttonSize);

        fourthButton.setPreferredSize(buttonSize);
        fourthButton.setMaximumSize(buttonSize);
        fourthButton.setMinimumSize(buttonSize);

        fifthButton.setPreferredSize(buttonSize);
        fifthButton.setMaximumSize(buttonSize);
        fifthButton.setMinimumSize(buttonSize);

        // Variable to check if there are saved recipes
        refreshEditRecipeButtonState(editRecipeButton);

        // When user clicks on the button it will lead them to respective screen
        createRecipeButton.addActionListener(e -> frame.showScreen(GuiManager.CREATE_RECIPE));

        editRecipeButton.addActionListener(e -> frame.showScreen(GuiManager.EDIT_RECIPE));

        fourthButton.addActionListener(e -> frame.showScreen(GuiManager.FOURTH_SCREEN));

        fifthButton.addActionListener(e -> frame.showScreen(GuiManager.FIFTH_SCREEN));

        // Re-check saved files each time this panel is shown in the card layout
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshEditRecipeButtonState(editRecipeButton);
            }
        });
    }

    private boolean hasSavedRecipeFiles() {
        File saveDir = new File("saved-recipes");
        if (!saveDir.isDirectory()) {
            return false;
        }

        File[] recipeFiles = saveDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        return recipeFiles != null && recipeFiles.length > 0;
    }

    private void refreshEditRecipeButtonState(JButton editRecipeButton) {
        boolean hasSavedRecipes = hasSavedRecipeFiles();
        editRecipeButton.setEnabled(hasSavedRecipes);
        editRecipeButton.setToolTipText(hasSavedRecipes ? null : "Create and save a recipe first.");
    }
}