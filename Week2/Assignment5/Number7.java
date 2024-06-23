import java.util.concurrent.ConcurrentHashMap;

public class Number7 {
    public static void main(String[] args) {
        // Creating a ConcurrentHashMap instance
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // Printing the original map
        System.out.println("Original Map: " + map);

        // Demonstrating the thread-safe aspect with concurrent modification
        Runnable readTask = () -> {
            for (String key : map.keySet()) {
                System.out.println("Reading Key: " + key + ", Value: " + map.get(key));
            }
        };

        Runnable writeTask = () -> {
            map.put("four", 4);
            System.out.println("Added Entry: {four=4}");
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

        // Printing the final map
        System.out.println("Final Map: " + map);
    }
}
