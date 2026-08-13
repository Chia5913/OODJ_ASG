import ui.LoginFrame;

import javax.swing.*;

/**
 * Application entry point.
 * Launches the modern Login window.
 */
public class Main {
    public static void main(String[] args) {
        // Optional: use a clean cross-platform look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
