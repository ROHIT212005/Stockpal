import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;



//import javax.swing.JOptionPane;

public class DatabaseManager {
    private Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed.
    }
public int addProduct(String in_name, int in_quantity, double in_price) {
    String name = in_name;
    int quantity = in_quantity;
    double price = in_price;

    try {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (quantity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Quantity exceeds the maximum value.");
        }

        if (price > Double.MAX_VALUE) {
            throw new IllegalArgumentException("Price exceeds the maximum value.");
        }

        Timestamp dateOfEntry = new Timestamp(new Date().getTime()); // Current date and time

        String sql = "INSERT INTO products (name, quantity, price, date_of_entry) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, quantity);
        statement.setDouble(3, price);
        statement.setTimestamp(4, dateOfEntry);

        int rowsAffected = statement.executeUpdate();
        return rowsAffected;
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
    }
    return 0;
}

    public void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product with ID " + productId + " deleted successfully.");
            } else {
                System.out.println("No product with ID " + productId + " found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet displayProducts() {
        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM products";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

        return resultSet;
    }
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }
    }
    protected boolean signup(String username, String password) {

        String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);


            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {
                System.out.println("User registration successful.");
                return true;
            } else {
                System.err.println("User registration failed.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
        }
    
public boolean updateProductQuantity(int productId, int newQuantity) {
        PreparedStatement preparedStatement = null;
        try {
            String updateQuery = "UPDATE products SET quantity = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}





