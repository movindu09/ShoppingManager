import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class WMShoppingManagerGUI extends JFrame {

    private final JComboBox<String> productTypeDropdown;
    private final JTable productTable;
    private final DefaultTableModel tableModel;
    private final JTextArea detailsTextArea;
    private final JButton addToCartButton;
    private final ShoppingCartGUI shoppingCartGUI;

    // Create the gui interface
    public WMShoppingManagerGUI(List<Product> productList) {
        super("Westminster Shopping Center");

        productTypeDropdown = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);
        detailsTextArea = new JTextArea(10, 30);
        addToCartButton = new JButton("Add to Shopping Cart");

        shoppingCartGUI = new ShoppingCartGUI(); // Initialize the shopping cart GUI

        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Info");

        JButton openNewGuiButton = new JButton("Shopping Cart");
        openNewGuiButton.addActionListener(e -> shoppingCartGUI.setVisible(true));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        JScrollPane scrollPane = new JScrollPane(detailsTextArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Center the JComboBox in the top panel
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboBoxPanel.add(new JLabel("Select Product Type:"));
        comboBoxPanel.add(productTypeDropdown);

        topPanel.add(comboBoxPanel, BorderLayout.CENTER);
        topPanel.add(openNewGuiButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(detailsScrollPane, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addToCartButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        this.pack();
        this.setVisible(true);

        productTypeDropdown.addActionListener(e -> displayProducts(productList));

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    displayProductDetails(selectedRow, productList);
                }
            }
        });

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    displayProductDetails(selectedRow, productList);
                }
            }
        });

        addToCartButton.addActionListener(e -> addToShoppingCart(productList));
        // Enable sorting by clicking on the "Product ID" column
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);

        displayProducts(productList);
        highlightRowsWithLessThanFourItems(productList);
    }

    private void addToShoppingCart(List<Product> productList) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = getProductById(productId, productList);
    
            if (selectedProduct != null && selectedProduct.getAvailableItems() > 0) {
                // Check if there are available items
                shoppingCartGUI.addProductToCart(selectedProduct);
                selectedProduct.reduceAvailableItems(); // Reduce available items
                displayProducts(productList); // Update the displayed products
            } else {
                JOptionPane.showMessageDialog(this, "No available items for this product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private void displayProducts(List<Product> productList) {
        String selectedType = (String) productTypeDropdown.getSelectedItem();
        tableModel.setRowCount(0);

        for (Product product : productList) {
            if (selectedType != null && (selectedType.equals("All") ||
                    (selectedType.equals("Electronics") && product instanceof Electronics) ||
                    (selectedType.equals("Clothing") && product instanceof Clothing))) {

                String category = (product instanceof Electronics) ? "Electronics" : "Clothing";
                Object[] rowData = {
                        product.getProductId(),
                        product.getProductName(),
                        category,
                        product.getPrice(),
                        getInfoText(product)
                };
                tableModel.addRow(rowData);
            }
        }
        detailsTextArea.setText("");
        addToCartButton.setEnabled(false); // Disable button initially
    }

    private String getInfoText(Product product) {
        if (product instanceof Electronics electronics) {
            return "Brand: " + electronics.getBrand() + "   " +"Warranty: " + electronics.getwarranty();
        } else if (product instanceof Clothing clothing) {
            return "Size: " + clothing.getSize() + "   " + "Color: " + clothing.getColor();
        }
        return "";
    }

    private void displayProductDetails(int selectedRow, List<Product> productList) {
        if (selectedRow != -1) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = getProductById(productId, productList);

            if (selectedProduct != null) {
                detailsTextArea.setText(
                        "Product ID: " + selectedProduct.getProductId() + "\n" +
                                "Category: " + getCategoryLabel(selectedProduct) + "\n" +
                                "Name: " + selectedProduct.getProductName() + "\n" +
                                "Price: " + selectedProduct.getPrice() + "\n" +
                                getInfoText(selectedProduct)+ "\n" +
                                "Available Items: " + selectedProduct.getAvailableItems()
                );
                addToCartButton.setEnabled(true); // Enable the button
            }
        }
    }

    private Product getProductById(String productId, List<Product> productList) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private String getCategoryLabel(Product product) {
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothing";
        }
        return "";
    }

    private void highlightRowsWithLessThanFourItems(List<Product> productList) {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String productId = (String) table.getValueAt(row, 0);
                Product product = getProductById(productId, productList);

                if (product != null && product.getAvailableItems() < 4) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                return c;
            }
        };

        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    public void showGUI() {
    }
}

/* to create this java swing part I refer from https://youtu.be/Jj_6tS1g4cE */
