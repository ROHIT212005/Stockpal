import javax.swing.*;

public class InventoryManagement {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //SplashScreen splashScreen = new SplashScreen();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
