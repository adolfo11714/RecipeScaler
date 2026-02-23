package com.adolfomartinez.recipescaler;

import com.adolfomartinez.recipescaler.panels.MainMenuPanel;
import javax.swing.SwingUtilities;

public class App{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiManager gm = GuiManager.getInstance();

            gm.registerScreen("MENU", new MainMenuPanel());

            gm.showScreen("MENU");
        });
    }
    
}
