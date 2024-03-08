public class Clothing extends Product {
    // Additional attributes specific to Clothing
    private String size;
    private String color;

    // Constructor
    public Clothing(String productId, String productName, int availableItems, double price, String size, String color) {
        super(productId, productName, availableItems, price);
        this.size = size;
        this.color = color;
    }

    // Getter & Setters for size
    public String getSize() {
        return size;
    }

    public String getColor(){
        return color;
    }

    public void setSize(String size) {
        this.size = size;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
