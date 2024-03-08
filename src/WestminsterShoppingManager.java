import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager {


    private static final List<Product> productList = new ArrayList<>();
    //set arraylist to max products 50
    private static final int MAX_PRODUCTS = 50;
    public static void main(String[] args) {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.readFromFile(); // Load products from file

        int choice;
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                manager.displayMenu();
                System.out.println("Enter your choice:");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        manager.addProduct(scanner);
                        break;
                    case 2:
                        manager.deleteProduct(scanner);
                        break;
                    case 3:
                        manager.printProducts();
                        break;
                    case 4:
                        manager.saveToFile();
                        break;
                    case 5:
                        openWMShoppingManagerGUI();
                        break;
                    case 6:
                        System.out.println("Exiting the program.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                choice = 0;
            } catch (Exception e) {
                System.out.println("An error occurred." + e.getMessage());
                choice = 0;
            }
        } while (choice != 6);

        scanner.close();
    }

    //display menu
    public  void displayMenu() {
        System.out.println("*************************");
        System.out.println("* Shopping Manager Menu *");
        System.out.println("*************************");
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save to file");
        System.out.println("5. Open the GUI");
        System.out.println("6. Exit");
    }

    // add a product to the list
    public  void addProduct(Scanner scanner) {
        if (productList.size() < MAX_PRODUCTS) {
            System.out.println("Select what type of product you want to add? ");
            System.out.println("1. Electronics");
            System.out.println("2. Clothing");
            int choice;
            while (true) {
                try {
                    choice = Integer.parseInt(scanner.next());
                    if (choice == 1 || choice == 2) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        
            System.out.println("Enter product ID:");
            String productId;
            while (true) {
                productId = scanner.next();
                if (isProductIdValid(productId)) {
                    break;
                } else {
                    System.out.println("The product ID already been taken. Please enter a different product ID.");
                }
            }
            System.out.println("Enter product name:");
            String productName = scanner.next();
            int availableItems;
            while (true) {
                try {
                    System.out.println("Enter available items:");
                    availableItems = Integer.parseInt(scanner.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number for available items.");
                }
            }
            double price;
            while (true) {
                try {
                    System.out.println("Enter product price:");
                    price = Double.parseDouble(scanner.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid price.");
                }
            }

            if (choice == 1) {
                System.out.println("Enter brand:");
                String brand = scanner.next();
                System.out.println("Enter the warranty period of product:");
                String warranty = scanner.next();
                productList.add(new Electronics(productId, productName, availableItems, price, brand, warranty));
            } else {
                System.out.println("Enter the size of the cloth:");
                String size = scanner.next();
                System.out.println("Enter the color of the cloth:");
                String color = scanner.next();
                productList.add(new Clothing(productId, productName, availableItems, price, size, color));
            }

            System.out.println("Product added successfully!");
        } else {
            System.out.println("Maximum number of products reached.");
        }
    }

    //check for duplicate product
    private static boolean isProductIdValid(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return false;
            }
        }
        return true;
    }

    // delete a product from the list
    public  void deleteProduct(Scanner scanner) {
        System.out.println("Enter the product ID of the product you want to delete:");
        String productIdToDelete = scanner.next();
    
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(productIdToDelete)) {
                iterator.remove();
    
                System.out.println("Deleted Product Information:");
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Available Items: " + product.getAvailableItems());
                System.out.println("Price: " + product.getPrice());
    
                if (product instanceof Electronics electronics) {
                    System.out.println("Type: Electronics");
                    System.out.println("Brand: " + electronics.getBrand());
                    System.out.println("Warranty Period: " + electronics.getwarranty());
                } else if (product instanceof Clothing clothing) {
                    System.out.println("Type: Clothing");
                    System.out.println("Size: " + clothing.getSize());
                    System.out.println("Color: " + clothing.getColor());
                }
    
                System.out.println("Product has been deleted.");
                System.out.println("Total products left: " + productList.size());
                return;
            }
        }
    
        System.out.println("Product with ID " + productIdToDelete + " not found.");
    }
        
    //print products in the list
    public  void printProducts() {
        if (productList.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("List of products:");
            for (Product product : productList) {
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Available Items: " + product.getAvailableItems());
                System.out.println("Price: " + product.getPrice());
                if (product instanceof Electronics electronics) {
                    System.out.println("Brand: " + electronics.getBrand());
                    System.out.println("Warranty Period: " + electronics.getwarranty());
                } else if (product instanceof Clothing clothing) {
                    System.out.println("Size: " + clothing.getSize());
                    System.out.println("Color: " + clothing.getColor()); 
                }
                System.out.println("--------------------");
            }
        }
    }

    //save products to a file
    public  void saveToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
        for (Product product : productList) {
            if (product instanceof Electronics electronics) {
                writer.write("Electronics," + electronics.getProductId() + "," + electronics.getProductName() + "," + electronics.getAvailableItems() + "," + electronics.getPrice() + "," + electronics.getBrand() + "," + electronics.getwarranty());
            } else if (product instanceof Clothing clothing) {
                writer.write("Clothing," + clothing.getProductId() + "," + clothing.getProductName() + "," + clothing.getAvailableItems()+ "," + clothing.getPrice() + "," + clothing.getSize() + "," + clothing.getColor());
            }
            writer.newLine();
        }
        System.out.println("Products saved to file.");
    } catch (IOException e) {
        System.out.println("An error occurred while saving products: " + e.getMessage());
    }
    }

    //read products from the file
    public  void readFromFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals("Electronics")) {
                Electronics electronics = new Electronics(parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]), parts[5], parts[6]);
                productList.add(electronics);
            } else if (parts[0].equals("Clothing")) {
                Clothing clothing = new Clothing(parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]), parts[5], parts[6]);
                productList.add(clothing);
            }
        }
        System.out.println("Products loaded from file.");
    } catch (IOException e) {
        System.out.println("An error occurred while loading products: " + e.getMessage());
    }
    }

    //opens shopping manager gui
    private static void openWMShoppingManagerGUI() {
        WMShoppingManagerGUI gui = new WMShoppingManagerGUI(productList);
        gui.showGUI();  
    }

}

