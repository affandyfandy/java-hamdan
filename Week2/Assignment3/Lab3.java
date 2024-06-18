import java.util.Scanner;

// Custom exception class
class NoVowelException extends Exception {
    public NoVowelException(String message) {
        super(message);
    }
}

public class Lab3 {

    // Method to check for vowels in a string
    public static void checkForVowels(String input) throws NoVowelException {
        if (!input.matches(".*[AEIOUaeiou].*")) {
            throw new NoVowelException("The string does not contain any vowels.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Prompt user to enter a string
            System.out.println("Enter a string:");
            String userInput = scanner.nextLine();

            // Check for vowels
            checkForVowels(userInput);

            // If no exception is thrown, print a success message
            System.out.println("The string contains vowels.");
        } catch (NoVowelException e) {
            // Handle the custom exception
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions
            System.out.println("Unhandled exception: " + e);
        } finally {
            scanner.close();
        }
    }
}
