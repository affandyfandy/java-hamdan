# Assignment 5 - Thread-Safe Singleton

<img src="Assignment5\images\singleton.png" width="1000">

## What is thread-safe singleton?

In Java, a Singleton is a design pattern that ensures a class has only one instance and provides a global point of access to that instance. 

**BUT** A singleton class itself is not thread safe. Multiple threads can access the singleton same time and create multiple objects, violating the singleton concept.

A thread-safe singleton ensures that a singleton instance is created in a way that multiple threads can use it without causing issues such as creating multiple instances.

## Double-Checked Locking
Double-Checked Locking is one of the most common method to implement a thread-safe singleton in Java. This approach reduces the overhead of synchronized by first checking the instance without synchronization, then synchronizing only if the instance is ```null```. 

### Code example

> Singleton class

 ```
 class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Hello, I am a Singleton!");
    }
}
```
> Lets set up a main class in a multi-thread environment
```
public class DoubleCheckedLockingExample {
    public static void main(String[] args) {
        // Create multiple threads to test the Singleton instance
        Thread thread1 = new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            singleton.showMessage();
        });

        Thread thread2 = new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            singleton.showMessage();
        });

        Thread thread3 = new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            singleton.showMessage();
        });

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```
> Output
```
Hello, I am a Singleton!
Hello, I am a Singleton!
Hello, I am a Singleton!
```

## How it works

There are 3 key components of the Double-Checked Locking method:

- Private Static Volatile Instance : It is declared as ```volatile``` to ensure that changes to the instance variable are visible to all threads.
- Private constructor: The constructor is private to prevent instantiation of the class from outside
- Public Static Method: To provide global access to the instance
    - The first ```if (instance == null)``` check is done without synchronization for performance reasons.
    - The ```synchronized (Singleton.class)``` block ensures that only one thread can execute the instance creation code at a time.
    - The second ```if (instance == null)``` check inside the synchronized block ensures that the instance is still null before creating it.