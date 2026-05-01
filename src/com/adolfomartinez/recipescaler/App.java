package com.adolfomartinez.recipescaler;

import com.adolfomartinez.recipescaler.panels.FifthPanel;
import com.adolfomartinez.recipescaler.panels.ScaleRecipePanel;
import com.adolfomartinez.recipescaler.panels.MainMenuPanel;
import com.adolfomartinez.recipescaler.panels.CreateRecipePanel;
import com.adolfomartinez.recipescaler.panels.EditRecipePanel;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiManager gm = GuiManager.getInstance();

            // register all the panels that the card layout will switch between
            gm.registerScreen(GuiManager.MAIN_SCREEN, new MainMenuPanel(gm));
            gm.registerScreen(GuiManager.CREATE_RECIPE, new CreateRecipePanel(gm));
            gm.registerScreen(GuiManager.EDIT_RECIPE, new EditRecipePanel(gm));
            gm.registerScreen(GuiManager.SCALE_RECIPE, new ScaleRecipePanel(gm));
            gm.registerScreen(GuiManager.FIFTH_SCREEN, new FifthPanel(gm));
            // additional screens can be registered here ("THIRD", "FOURTH", etc.)

            gm.showScreen(GuiManager.MAIN_SCREEN);
        });
    }
    
}
