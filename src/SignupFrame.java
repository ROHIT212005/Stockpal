import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class SignupFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signupButton;
    private DatabaseManager databaseManager;

    public SignupFrame(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        setTitle("StockPal - Signup new user");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Set the icon for the window title bar and taskbar
        Image icon = Toolkit.getDefaultToolkit().getImage("stock\\box.png");
        setIconImage(icon);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font template_font = new Font("Corbel", Font.BOLD, 16);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(template_font);
        usernameField = new JTextField();
        usernameField.setFont(template_font);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(template_font);
        passwordField = new JPasswordField();
        passwordField.setFont(template_font);

        signupButton = new JButton("Signup");
        signupButton.setBackground(new Color(0, 122, 204));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(template_font);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars); // Convert password to a String

                if (signupUser(username, password)) {
                    JOptionPane.showMessageDialog(SignupFrame.this, "User registered successfully.");
                    dispose(); // Close the signup frame
                } else {
                    JOptionPane.showMessageDialog(SignupFrame.this, "User registration failed.");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(signupButton);

        setContentPane(panel);
    }

    private boolean signupUser(String username, String password) {
       
        return databaseManager.signup(username, password);
    }

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        // Initialize the database connection in the databaseManager.

        SwingUtilities.invokeLater(() -> {
            SignupFrame signupFrame = new SignupFrame(databaseManager);
            signupFrame.setVisible(true);
        });
    }
}
