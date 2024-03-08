public class Electronics extends Product {
    // Additional attributes specific to Electronics
    private String brand;
    private String warranty;

    // Constructor
    public Electronics(String productId, String productName, int availableItems, double price, String brand, String warranty) {
        super(productId, productName, availableItems, price);
        this.brand = brand;
        this.warranty = warranty;
    }

    // Getter & Setters for brand
    public String getBrand() {
        return brand;
    }

    public String getwarranty(){
        return warranty;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setwarranty(String warranty) {
        this.warranty = warranty;
    }


}