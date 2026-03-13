package com.adolfomartinez.recipescaler;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Singleton class to manage the GUI screens using CardLayout
public class GuiManager {
    // screen name constants
    public static final String MAIN_SCREEN = "main";
    public static final String SECOND_SCREEN = "second";
    public static final String THIRD_SCREEN = "third";
    public static final String FOURTH_SCREEN = "fourth";
    public static final String FIFTH_SCREEN = "fifth";

    private static GuiManager instance;
    
    private final JFrame window;
    private final CardLayout layout;
    private final JPanel container;

    private GuiManager() {
        window = new JFrame("Recipe Scaler");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);

        layout = new CardLayout();
        container = new JPanel(layout);

        window.setContentPane(container);
    }

    public static GuiManager getInstance() {
        if (instance == null) instance = new GuiManager();
        return instance;
    }

    public void registerScreen(String name, JPanel panel) {
        container.add(panel, name);
    }

    public void showScreen(String name) {
        layout.show(container, name);
        window.setVisible(true);
    }

    public JFrame getWindow() {
        return window;
    }
}
