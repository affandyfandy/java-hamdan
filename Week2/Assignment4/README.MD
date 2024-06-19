# Q1 : Deadlock

**Deadlock** is a situation in concurrent programming where two or more threads are unable to proceed because each is waiting for one of the others to release a resource. This typically occurs when multiple threads need the same set of resources and obtain them in different orders.

## Conditions for Deadlock
A deadlock situation arises when the following four conditions occur simultaneously:
- **Mutual Exclusion**: At least one resource must be held in a non-shareable mode.
- **Hold and Wait**: A thread holding at least one resource is waiting to acquire additional resources held by other threads.
- **No Preemption**: Resources cannot be forcibly removed from threads that are holding them.
- **Circular Wait**: A set of threads are waiting for each other in a circular chain.

## Example of Deadlock

Here is an example in Java that demonstrates a deadlock situation:

```java
public class DeadlockExample {
    public static void main(String[] args) {
        final Object resource1 = "Resource 1";
        final Object resource2 = "Resource 2";

        // Thread 1 tries to lock resource1 then resource2
        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: locked resource 1");
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                synchronized (resource2) {
                    System.out.println("Thread 1: locked resource 2");
                }
            }
        });

        // Thread 2 tries to lock resource2 then resource1
        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: locked resource 2");
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                synchronized (resource1) {
                    System.out.println("Thread 2: locked resource 1");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
```

In this example, `thread1` locks `resource1` and waits to lock `resource2`, while `thread2` locks `resource2` and waits to lock `resource1`. This creates a deadlock situation.

## How to Prevent Deadlock

There are several strategies to prevent deadlock:

- **Avoid Nested Locks**: Avoid locking multiple resources at once.
- **Lock Ordering**: Always acquire locks in a predefined order. If every thread follows the same order when acquiring locks, circular wait conditions are avoided.
- **Lock Timeout**: Use tryLock with a timeout. If a thread cannot acquire the lock within the specified time, it releases the locks it holds and retries.
- **Deadlock Detection**: Implement a deadlock detection algorithm that can monitor and detect deadlock conditions. If detected, corrective measures can be taken, such as preempting resources or killing one of the deadlocked threads.

## Example of Lock Ordering to Prevent Deadlock

Here is an example that demonstrates how lock ordering can prevent deadlock:

```java
public class DeadlockPreventionExample {
    public static void main(String[] args) {
        final Object resource1 = "Resource 1";
        final Object resource2 = "Resource 2";

        // Thread 1 tries to lock resource1 then resource2
        Thread thread1 = new Thread(() -> {
            lockResources(resource1, resource2);
        });

        // Thread 2 also locks resources in the same order
        Thread thread2 = new Thread(() -> {
            lockResources(resource1, resource2);
        });

        thread1.start();
        thread2.start();
    }

    private static void lockResources(Object res1, Object res2) {
        synchronized (res1) {
            System.out.println(Thread.currentThread().getName() + " locked " + res1);
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            synchronized (res2) {
                System.out.println(Thread.currentThread().getName() + " locked " + res2);
            }
        }
    }
}
```

In this example, both threads lock resources in the same order (first `resource1`, then `resource2`), which prevents deadlock by eliminating circular wait conditions.


# Q4 : What are noticeable things when using multiple thread?

## Benefits

1. **Increased Performance**:
    - **Concurrency**: Multiple threads can execute concurrently, potentially leading to better utilization of CPU resources, especially on multi-core processors.
    - **Responsiveness**: In GUI applications, using threads can keep the user interface responsive while performing background tasks.

2. **Simplified Design**:
    - **Separation of Concerns**: Threads can help separate different parts of a program that perform distinct tasks, such as handling user input, performing calculations, or communicating over a network.

## Challenges

1. **Synchronization Issues**:
    - **Race Conditions**: Multiple threads accessing shared resources without proper synchronization can lead to unpredictable results or corrupted data.
    - **Deadlocks**: Threads waiting on each other to release resources can lead to a situation where none of the threads can proceed, effectively freezing the program.
    - **Starvation**: Some threads may never get access to the required resources, leading to indefinite waiting.

2. **Complex Debugging**:
    - **Non-deterministic Bugs**: Bugs that depend on the timing and order of thread execution can be hard to reproduce and debug.
    - **Increased Complexity**: Understanding and maintaining a multi-threaded program is generally more complex than a single-threaded one.

3. **Resource Management**:
    - **Overhead**: Creating and managing multiple threads incurs overhead. Excessive threading can lead to increased memory usage and context-switching overhead, potentially degrading performance.
    - **Resource Contention**: Threads competing for shared resources (like CPU, memory, or I/O) can lead to contention, reducing overall performance benefits.

# Q5 ReadWriteLock interface

A ReadWriteLock maintains a pair of associated locks, one for read-only operations and one for writing. The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.

All ReadWriteLock implementations must guarantee that the memory synchronization effects of writeLock operations (as specified in the Lock interface) also hold with respect to the associated readLock. That is, a thread successfully acquiring the read lock will see all updates made upon previous release of the write lock.

We'll create a shared resource, a simple counter, and demonstrate how multiple threads can read from it concurrently while only one thread can write to it at a time.

## Example

### 1. Create the Shared Resource Class

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedCounter {
    private int count = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void increment() {
        lock.writeLock().lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented count to " + count);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getCount() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read count as " + count);
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

### 2. Create Threads to Perform Read and Write Operations

```java
public class ReadWriteLockExample {
    public static void main(String[] args) {
        final SharedCounter counter = new SharedCounter();

        // Create writer threads
        Thread writer1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Writer-1");

        Thread writer2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Writer-2");

        // Create reader threads
        Thread reader1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.getCount();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Reader-1");

        Thread reader2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.getCount();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Reader-2");

        // Start threads
        writer1.start();
        writer2.start();
        reader1.start();
        reader2.start();

        // Wait for threads to finish
        try {
            writer1.join();
            writer2.join();
            reader1.join();
            reader2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## Explanation

### SharedCounter Class
- The `SharedCounter` class has a `count` variable and a `ReentrantReadWriteLock`.
- The `increment` method increments the count. It acquires a write lock to ensure exclusive access.
- The `getCount` method reads the count. It acquires a read lock to allow concurrent reads.

### ReadWriteLockExample Class
- Creates instances of reader and writer threads.
- Writer threads increment the counter five times, sleeping briefly between increments.
- Reader threads read the counter five times, also sleeping briefly between reads.
- All threads are started and then joined to ensure the main thread waits for their completion.

This setup demonstrates how `ReadWriteLock` allows multiple readers to access the shared resource concurrently, but only one writer at a time can modify it, ensuring thread safety and improving performance for read-heavy workloads.