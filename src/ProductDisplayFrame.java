import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProductDisplayFrame extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton incrementButton;
    private JButton decrementButton;
    private JTextField searchField;
    private JButton searchButton;
    private DatabaseManager databaseManager;
    private TableRowSorter<DefaultTableModel> tableRowSorter;
    private ProductAddFrame ProductAddFrame;
    private Font template_font;

    public ProductDisplayFrame(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        ProductAddFrame = new ProductAddFrame(databaseManager, this);
        template_font = new Font("Corbel", Font.BOLD, 14);

        setTitle("StockPal - Product Display");


        Image icon = Toolkit.getDefaultToolkit().getImage("stock\\box.png");
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Checkbox column
                } else if (columnIndex == 1) {
                    return Integer.class; // "S.no" column
                } else if (columnIndex == getColumnCount() - 6) {
                    return Timestamp.class; // "Date of Entry" column
                } else if (columnIndex == getColumnCount() - 1) {
                    return Double.class; // "Total" column
                } else if (columnIndex == 5) {
                    return Integer.class; // Quantity column
                } else if (columnIndex == 6) {
                    return Double.class; // Rate column
                }
                return super.getColumnClass(columnIndex);
            }
        };

        productTable = new JTable(tableModel);
        tableRowSorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(tableRowSorter);
        JScrollPane scrollPane = new JScrollPane(productTable);

        tableModel.addColumn("Select");
        tableModel.addColumn("S.no"); 
        tableModel.addColumn("Date of Entry");
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Rate");
        tableModel.addColumn("Total");

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        productTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);


        JTableHeader header = productTable.getTableHeader();
        header.setFont(template_font);


        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setFont(template_font);


        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by product name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchButton.setBackground(new Color(24, 190, 188));
        searchButton.setForeground(Color.WHITE);
        searchField.setForeground(Color.BLACK);

        searchField.setFont(template_font);
        searchPanel.getComponent(0).setFont(template_font);
        searchButton.setFont(template_font);

        JPanel buttonPanel = new JPanel();
        JLabel actionsLabel = new JLabel("Actions for selected items:");
        actionsLabel.setFont(template_font);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedItems());
        deleteButton.setBackground(new Color(239, 96, 85));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(template_font);

        incrementButton = new JButton("+");
        incrementButton.addActionListener(e -> incrementSelectedItems());
        incrementButton.setBackground(new Color(172, 247, 129));
        incrementButton.setForeground(Color.WHITE);
        incrementButton.setFont(template_font);

        decrementButton = new JButton("-");
        decrementButton.addActionListener(e -> decrementSelectedItems());
        decrementButton.setBackground(new Color(239, 96, 85));
        decrementButton.setForeground(Color.WHITE);
        decrementButton.setFont(template_font);

        buttonPanel.add(actionsLabel);
        buttonPanel.add(deleteButton);
        buttonPanel.add(incrementButton);
        buttonPanel.add(decrementButton);
        buttonPanel.add(searchPanel);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));


        JMenuItem addProductMenuItem = new JMenuItem("Add Product");
        addProductMenuItem.setBackground(new Color(69, 39, 160));
        addProductMenuItem.setForeground(Color.WHITE);
        JMenuItem signupMenuItem = new JMenuItem("Signup");
        signupMenuItem.setBackground(Color.ORANGE);
        signupMenuItem.setForeground(Color.WHITE);
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setBackground(new Color(211, 47, 47));
        logoutMenuItem.setForeground(Color.WHITE);

 
        Font menuFont = new Font("Corbel", Font.BOLD, 12); // Smaller font
        addProductMenuItem.setFont(menuFont);
        signupMenuItem.setFont(menuFont);
        logoutMenuItem.setFont(menuFont);

        Dimension menuSize = new Dimension(150, 20); 
        addProductMenuItem.setPreferredSize(menuSize);
        signupMenuItem.setPreferredSize(menuSize);
        logoutMenuItem.setPreferredSize(menuSize);


        addProductMenuItem.addActionListener(e -> openProductAddFunctionality());
        signupMenuItem.addActionListener(e -> openSignupFunctionality());
        logoutMenuItem.addActionListener(e -> logout());

  
        menuBar.add(addProductMenuItem);
        menuBar.add(signupMenuItem);
        menuBar.add(logoutMenuItem);

        setJMenuBar(menuBar);

        add(buttonPanel, BorderLayout.SOUTH);
        renderCheckboxColumn(0);
        displayProductsFromDatabase();
        addSearchFunctionality();
    }

    protected void displayProductsFromDatabase() {
        tableModel.setRowCount(0);

        String query = "SELECT ROW_NUMBER() OVER (ORDER BY id) AS `S.no`, id, date_of_entry, name, quantity, price FROM products";
        ResultSet resultSet = databaseManager.executeQuery(query);

        try {
            while (resultSet.next()) {
                int sno = resultSet.getInt("S.no");
                int id = resultSet.getInt("id");
                Timestamp timestamp = resultSet.getTimestamp("date_of_entry");
                String dateOfEntry = new SimpleDateFormat("dd-MM-yyyy").format(timestamp); // Format the date
                String productName = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                double total = quantity * price;

                tableModel.addRow(new Object[] { false, sno, dateOfEntry, id, productName, quantity, price, total });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void renderCheckboxColumn(int column) {
        TableColumnModel columnModel = productTable.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(new TableCellRenderer() {
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                checkBox.setSelected((Boolean) value);
                return checkBox;
            }
        });
    }

    private void deleteSelectedItems() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Boolean selected = (Boolean) tableModel.getValueAt(row, 0);
            if (selected != null && selected) {
                int productId = (int) tableModel.getValueAt(row, 3);
                databaseManager.deleteProduct(productId);
                tableModel.removeRow(row);
                row--;
            }
        }


        for (int row = 0; row < tableModel.getRowCount(); row++) {
            tableModel.setValueAt(row + 1, row, 1); 
        }
    }

    private void incrementSelectedItems() {
        modifyQuantityOfSelectedItems(1);
        updateTotalValues(); 
    }

    private void decrementSelectedItems() {
        modifyQuantityOfSelectedItems(-1);
        updateTotalValues(); 
    }

    private void updateTotalValues() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int quantity = (int) tableModel.getValueAt(row, 5); // Quantity column
            double price = (double) tableModel.getValueAt(row, 6); // Price column
            double total = quantity * price;
            tableModel.setValueAt(total, row, 7); // Total column
        }
    }

    private void modifyQuantityOfSelectedItems(int quantityChange) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Boolean selected = (Boolean) tableModel.getValueAt(row, 0);
            if (selected != null && selected) {
                int productId = (int) tableModel.getValueAt(row, 3);
                int currentQuantity = (int) tableModel.getValueAt(row, 5);
                int newQuantity = currentQuantity + quantityChange;
                if (newQuantity < 0) {
                    // Avoid negative quantity
                    JOptionPane.showMessageDialog(this, "Quantity can't be lower than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                databaseManager.updateProductQuantity(productId, newQuantity);
                tableModel.setValueAt(newQuantity, row, 5); 
            }
        }
    }

    private void addSearchFunctionality() {
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                tableRowSorter.setRowFilter(null);
            } else {
                tableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            }
        }

        );
    }

    private void openSignupFunctionality() {
        SwingUtilities.invokeLater(() -> {
            SignupFrame signupFrame = new SignupFrame(databaseManager);
            signupFrame.setVisible(true);
        });
    }

    private void openProductAddFunctionality() {
        ProductAddFrame.setVisible(true);
    }

    private void logout() {
        this.dispose();


        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        SwingUtilities.invokeLater(() -> {
            ProductDisplayFrame displayFrame = new ProductDisplayFrame(dbManager);
            displayFrame.setVisible(true);
        });
    }
}
