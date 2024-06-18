import java.util.Scanner;

// Custom exception class
class Lab2Exception extends Exception {
    public Lab2Exception(String message) {
        super(message);
    }
}

public class Lab2 {
    public static void main(String[] args) {
        // Array with 5 menu elements
        String[] menu = {"Menu 1: Chicken", "Menu 2: Beef", "Menu 3: Vegetarian", "Menu 4: Vegan", "Menu 5: Dessert"};
        System.out.println("Menu 1: Chicken");
        System.out.println("Menu 2: Beef");
        System.out.println("Menu 3: Vegetarian");
        System.out.println("Menu 4: Vegan");
        System.out.println("Menu 5: Dessert");

        Scanner scanner = new Scanner(System.in);
        
        try {
            // Prompt user to enter a number
            System.out.println("Enter a menu number (1-5):");
            int choice = scanner.nextInt();

            // Try to access the selected menu item
            try {
                System.out.println(menu[choice - 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                // Handle the unchecked exception for index out of bounds
                System.out.println("Error: Menu number out of range. Please enter a number between 1 and 5.");
            }
        } catch (Exception e) {
            System.out.println("Unhandled exception: " + e);
        } finally {
            scanner.close();
        }
        System.out.println("If this shows up, the system does not crash");
    }
}
