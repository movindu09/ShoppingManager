public abstract class Product {
    private String productId;
    private String productName;
    private double price;
    private int availableItems;
    private String category;

    // Constructor
    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.availableItems = availableItems;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getAvailableItems(){
        return availableItems;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category != null ? category : "";
    }
    public void reduceAvailableItems() {
        if (availableItems > 0) {
            availableItems--;
        }
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAvailableItems(int availableItems){
        this.availableItems = availableItems;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


