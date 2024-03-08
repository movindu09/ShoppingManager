import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartGUI extends JFrame {

    private final DefaultTableModel cartTableModel;
    private final JLabel totalLabel;
    private final JLabel discount1Label;
    private final JLabel discount2Label;
    private final JLabel finalTotalLabel;
    private final Map<String, Integer> productQuantities; // Map to store the quantity of each product
    private final Map<String, Integer> categoryQuantities; // Map to store the quantity of each category


    public ShoppingCartGUI() {
        super("Shopping Cart");

        cartTableModel = new DefaultTableModel();
        JTable cartTable = new JTable(cartTableModel);
        totalLabel = new JLabel("Total: $0.00");
        discount1Label = new JLabel("First Purchase Discount: 10%");
        discount2Label = new JLabel("Three Items in the Same Category Discount: 20%");
        finalTotalLabel = new JLabel("Final Total: $0.00");
        productQuantities = new HashMap<>();
        categoryQuantities = new HashMap<>();

        cartTableModel.addColumn("Product");
        cartTableModel.addColumn("Quantity");
        cartTableModel.addColumn("Price");

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel discountPanel = new JPanel(new GridLayout(4, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JScrollPane cartScrollPane = new JScrollPane(cartTable);

        mainPanel.add(cartScrollPane, BorderLayout.CENTER);
        mainPanel.add(discountPanel, BorderLayout.SOUTH);

        discountPanel.add(totalLabel);
        discountPanel.add(discount1Label);
        discountPanel.add(discount2Label);
        discountPanel.add(finalTotalLabel);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }


    public void addProductToCart(Product product) {
        int quantity = productQuantities.getOrDefault(product.getProductId(), 0) + 1;
        productQuantities.put(product.getProductId(), quantity);

        int rowIndex = findRowIndex(product.getProductId());
        if (rowIndex == -1) {
            // Product not in the cart, add a new row
            Object[] rowData = {
                    getProductInfo(product),
                    quantity,
                    quantity * product.getPrice()
            };
            cartTableModel.addRow(rowData);
        } else {
            // Product already in the cart, update the quantity and total price
            cartTableModel.setValueAt(quantity, rowIndex, 1);
            cartTableModel.setValueAt(quantity * product.getPrice(), rowIndex, 2);
        }

        // Update category quantity
        int categoryQuantity = categoryQuantities.getOrDefault(product.getCategory(), 0);
        categoryQuantities.put(product.getCategory(), categoryQuantity + 1);

        updateTotal();
    }

    private String getProductInfo(Product product) {
        if (product instanceof Electronics electronics) {
            return String.format("%s %s Brand: %s, \nWarranty: %s", product.getProductId(), product.getProductName(), electronics.getBrand(), electronics.getwarranty());
        } else if (product instanceof Clothing clothing) {
            return String.format("%s %s Size: %s, \nColor: %s", product.getProductId(), product.getProductName(), clothing.getSize(), clothing.getColor());
        }
        return "";
    }

    private int findRowIndex(String productId) {
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String productInfo = (String) cartTableModel.getValueAt(i, 0);
            if (productInfo.startsWith(productId)) {
                return i;
            }
        }
        return -1;
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            total += (double) cartTableModel.getValueAt(i, 2);
        }
        totalLabel.setText("Total: $" + total);
        updateDiscounts(total);
    }

    private void updateDiscounts(double total) {
        // Apply discounts
        double firstPurchaseDiscount = total * 0.10;
        double threeItemsDiscount = calculateThreeItemsDiscount();

        // Update labels
        discount1Label.setText("First Purchase Discount: 10% (-$" + String.format("%.2f", firstPurchaseDiscount) + ")");
        discount2Label.setText("Three Items in the Same Category Discount: 20% (-$" + String.format("%.2f", threeItemsDiscount) + ")");

        // Calculate final total
        double finalTotal = total - firstPurchaseDiscount - threeItemsDiscount;
        finalTotalLabel.setText("Final Total: $" + String.format("%.2f", finalTotal));
    }

    private double calculateThreeItemsDiscount() {
        double discount = 0;
        for (int quantity : categoryQuantities.values()) {
            if (quantity >= 3) {
                discount += totalCategoryPrice(quantity);
            }
        }
        return discount;
    }

    // Assuming each item in the same category has the same price calculate total
    private double totalCategoryPrice(int quantity) {
        return quantity * 0.20;
    }
}
