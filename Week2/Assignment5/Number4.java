import java.util.HashMap;

public class Number3 {
    public static void main(String[] args) {
        // Creating a HashMap instance
        HashMap<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        // Getting a shallow copy using the clone method
        @SuppressWarnings("unchecked")
        HashMap<String, Integer> shallowCopy = (HashMap<String, Integer>) originalMap.clone();

        // Printing the shallow copy
        System.out.println("Shallow Copy: " + shallowCopy);
    }
}
