package com.adolfomartinez.recipescaler;

import javax.swing.JFrame;

public class GuiManager {
    private static GuiManager instance;
    private JFrame currentFrame;

    private GuiManager() {}

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager();
        }
        return instance;
    }

    public void switchFrame(JFrame newFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }
}
