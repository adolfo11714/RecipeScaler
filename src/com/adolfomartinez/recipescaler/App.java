package com.adolfomartinez.recipescaler;

import com.adolfomartinez.recipescaler.panels.MainMenuPanel;
import com.adolfomartinez.recipescaler.panels.SecondPanel;
import javax.swing.SwingUtilities;

public class App{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiManager gm = GuiManager.getInstance();

            // register all the panels that the card layout will switch between
            gm.registerScreen("MENU", new MainMenuPanel(gm));
            gm.registerScreen("SECOND", new SecondPanel(gm));
            // additional screens can be registered here ("THIRD", "FOURTH", etc.)

            gm.showScreen("MENU");
        });
    }
    
}
