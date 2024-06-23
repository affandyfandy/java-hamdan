# Q12: 12. What happen multiple threads to access and modify a shared collection concurrently

## Problem

When multiple threads access and modify a shared collection concurrently, it can lead to serious issues, such as `ConcurrentModificationException`.

### Explanation

### What Happens?

1. **Inconsistent State**: Concurrent modifications can leave the collection in an inconsistent state, leading to unpredictable behavior and data corruption.
2. **Race Conditions**: Multiple threads may try to read and write data simultaneously, causing race conditions where the outcome depends on the timing of thread execution.
3. **ConcurrentModificationException**: This exception is thrown when a thread detects that the collection has been modified concurrently while it is iterating through it.

### Why It Happens?

- **Iteration and Modification**: When a collection is being iterated over and another thread modifies it (e.g., by adding or removing elements), the structure of the collection changes, leading to a `ConcurrentModificationException`.
- **Lack of Synchronization**: Without proper synchronization, there is no coordination between threads accessing and modifying the collection, resulting in inconsistent and unpredictable states.

## Solutions

To avoid concurrency issues and ensure data consistency, consider the following solutions:

### Synchronized Collections

Java provides synchronized collections that can be accessed by only one thread at a time, ensuring thread safety. Examples include using `Collections.synchronizedList` for lists and `Collections.synchronizedMap` for maps.

### Concurrent Collections

Java provides concurrent collections that are designed for concurrent access and modification by multiple threads. Examples include `ConcurrentHashMap`, `ConcurrentSkipListMap`, and `CopyOnWriteArrayList`. These collections handle synchronization internally, providing better performance and scalability in multithreaded environments.

### Explicit Synchronization

Use explicit synchronization mechanisms like `synchronized` blocks or `ReentrantLock` to control access to the shared collection, ensuring that only one thread can modify the collection at a time.
