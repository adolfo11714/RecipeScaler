package com.adolfomartinez.recipescaler;
import com.adolfomartinez.recipescaler.panels.MainMenuPanel;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame{

    private CardLayout layout;
    private JPanel container;

    public MainFrame() {
        setTitle("Recipe Scaler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        container = new JPanel(layout);

        // Add screens
        container.add(new MainMenuPanel(this), "MENU");

        add(container);

        layout.show(container, "MENU");
    }

    
    public void showScreen(String name) {
        layout.show(container, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
