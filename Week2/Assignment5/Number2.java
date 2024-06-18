import java.util.ArrayList;
import java.util.Scanner;

public class Number2 {
    public static void main(String[] args) {
        // Create an ArrayList and add some elements
        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        list.add("Peach");
        list.add("Blueberry");

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Ask the user to enter the index
        System.out.println("Enter the index of the element you want to retrieve:");
        int index = scanner.nextInt();

        // Check if the index is within the valid range
        if (index >= 0 && index < list.size()) {
            // Retrieve and print the element at the specified index
            String element = list.get(index);
            System.out.println("Element at index " + index + ": " + element);
        } else {
            // Print an error message if the index is out of range
            System.out.println("Invalid index. Please enter an index between 0 and " + (list.size() - 1) + ".");
        }

        // Close the scanner
        scanner.close();
    }
}
