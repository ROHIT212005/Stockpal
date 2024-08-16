import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.sql.SQLException;

import javax.swing.border.EmptyBorder;

public class ProductAddFrame extends JFrame {
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private DatabaseManager databaseManager;
    private ProductDisplayFrame displayFrame;

    public ProductAddFrame(DatabaseManager databaseManager, ProductDisplayFrame displayFrame) {
        this.databaseManager = databaseManager;
        this.displayFrame = displayFrame;
        setTitle("StockPal - Add Product");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

       
        Image icon = Toolkit.getDefaultToolkit().getImage("stock\\box.png"); 
        setIconImage(icon);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(4, 2, 10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font template_font = new Font("Corbel", Font.BOLD, 16);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(template_font);
        nameField = new JTextField();
        nameField.setFont(template_font);
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(template_font);
        quantityField = new JTextField();
        quantityField.setFont(template_font);
        quantityField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel priceLabel = new JLabel("Rate:");
        priceLabel.setFont(template_font);
        priceField = new JTextField();
        priceField.setFont(template_font);
        priceField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 122, 204));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(template_font);
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
                displayFrame.displayProductsFromDatabase();
            }
        });

        contentPanel.add(nameLabel);
        contentPanel.add(nameField);
        contentPanel.add(quantityLabel);
        contentPanel.add(quantityField);
        contentPanel.add(priceLabel);
        contentPanel.add(priceField);
        contentPanel.add(new JLabel()); // Empty label for spacing
        contentPanel.add(addButton);

        setContentPane(contentPanel);
    }

    private void addProduct() {
        String name = nameField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();
    
        try {
            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                throw new IllegalArgumentException("Please fill in all fields.");
            }
    
            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);
    
            if (quantity < 0 || price < 0.0) {
                throw new IllegalArgumentException("Quantity and price cannot be negative.");
            }
    
            if (quantity > 2147483647) {
                throw new IllegalArgumentException("Quantity exceeds the data type's range.");
            }
            if (price > 99999999.99) {
                throw new IllegalArgumentException("Price exceeds the data type's range.");
            }
    

            int rowsAffected = databaseManager.addProduct(name, quantity, price);
            if (rowsAffected > 0) {
                nameField.setText("");
                quantityField.setText("");
                priceField.setText("");
                dispose();
                displayFrame.displayProductsFromDatabase();
            }
            // else {
            //     JOptionPane.showMessageDialog(this, "Failed to add the product.", "Error", JOptionPane.ERROR_MESSAGE);
            // }
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
         }// catch (NumberFormatException e) {
        //     showErrorDialog("Invalid quantity or price format.");
        // }
    }
    
    
    
    
    private void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    

    
}
