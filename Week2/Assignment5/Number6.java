import java.util.concurrent.CopyOnWriteArrayList;

public class Number6 {
    public static void main(String[] args) {
        // Creating a CopyOnWriteArrayList instance
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");

        // Printing the original list
        System.out.println("Original List: " + list);

        // Modifying an item in the list
        list.set(1, "TWO");

        // Printing the modified list
        System.out.println("Modified List: " + list);
        
        // Demonstrating the thread-safe aspect with concurrent modification
        Runnable readTask = () -> {
            for (String item : list) {
                System.out.println("Reading Item: " + item);
            }
        };

        Runnable writeTask = () -> {
            list.add("four");
            System.out.println("Added Item: four");
        };

        Thread thread1 = new Thread(readTask);
        Thread thread2 = new Thread(writeTask);

        thread1.start();
        thread2.start();

        // Ensuring main thread waits for the other threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Printing the final list
        System.out.println("Final List: " + list);
    }
}
