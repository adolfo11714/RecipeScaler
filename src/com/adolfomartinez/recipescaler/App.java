package com.adolfomartinez.recipescaler;

import com.adolfomartinez.recipescaler.panels.FifthPanel;
import com.adolfomartinez.recipescaler.panels.FourthPanel;
import com.adolfomartinez.recipescaler.panels.MainMenuPanel;
import com.adolfomartinez.recipescaler.panels.CreateRecipePanel;
import com.adolfomartinez.recipescaler.panels.ThirdPanel;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiManager gm = GuiManager.getInstance();

            // register all the panels that the card layout will switch between
            gm.registerScreen(GuiManager.MAIN_SCREEN, new MainMenuPanel(gm));
            gm.registerScreen(GuiManager.CREATE_RECIPE, new CreateRecipePanel(gm));
            gm.registerScreen(GuiManager.THIRD_SCREEN, new ThirdPanel(gm));
            gm.registerScreen(GuiManager.FOURTH_SCREEN, new FourthPanel(gm));
            gm.registerScreen(GuiManager.FIFTH_SCREEN, new FifthPanel(gm));
            // additional screens can be registered here ("THIRD", "FOURTH", etc.)

            gm.showScreen(GuiManager.MAIN_SCREEN);
        });
    }
    
}
