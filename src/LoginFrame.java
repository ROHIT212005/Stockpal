import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DatabaseManager databaseManager;
    private ProductDisplayFrame displayFrame;

    public LoginFrame() {
        databaseManager = new DatabaseManager();
        displayFrame = new ProductDisplayFrame(databaseManager);

        setTitle("StockPal - Login");

        Image icon = Toolkit.getDefaultToolkit().getImage("stock\\box.png");
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 2, 10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font template_font = new Font("Corbel", Font.BOLD, 18);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(template_font);
        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usernameField.setFont(usernameLabel.getFont()); 

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(template_font);
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passwordField.setFont(passwordLabel.getFont());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (databaseManager.authenticate(username, password)) {
                    openDisplayFrame();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Authentication failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(template_font);

        contentPanel.add(usernameLabel);
        contentPanel.add(usernameField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(new JLabel());
        contentPanel.add(loginButton);

        setContentPane(contentPanel);
    }

    private void openDisplayFrame() {
        displayFrame.setVisible(true);
        setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
