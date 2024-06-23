# Q1: Comparison of ArrayList vs LinkedList & HashSet vs TreeSet vs LinkedHashSet

## ArrayList vs LinkedList

| Feature                     | ArrayList                                   | LinkedList                                 |
|-----------------------------|---------------------------------------------|--------------------------------------------|
| **Implementation**          | Uses a dynamic array to store elements      | Uses a doubly-linked list to store elements|
| **Access Time (get)**       | O(1) - Direct access via index              | O(n) - Requires traversal from the head or tail |
| **Memory Usage**            | Less memory overhead                        | More memory overhead due to node pointers  |
| **Iteration Performance**   | Faster due to better cache locality         | Slower due to pointer dereferencing        |
| **Random Access**           | Efficient and fast                          | Inefficient, requires traversal            |
| **Data Structure**          | Backed by a dynamic array                   | Backed by a doubly-linked list             |
| **Use Case**                | Preferred for read-heavy operations         | Preferred for write-heavy operations       |
"""

## HashSet vs TreeSet vs LinkedHashSet

| Feature                       | HashSet                                    | TreeSet                                   | LinkedHashSet                              |
|-------------------------------|--------------------------------------------|-------------------------------------------|--------------------------------------------|
| **Implementation**            | Uses a hash table                          | Uses a Red-Black tree (balanced binary search tree) | Uses a hash table with a linked list running through it |
| **Ordering**                  | No guaranteed order                        | Sorted in natural order or by a comparator | Maintains insertion order                  |
| **Null Elements**             | Allows a single null element               | Does not allow null elements              | Allows a single null element               |
| **Performance (add, remove, contains)** | O(1) average time complexity           | O(log n) time complexity                  | O(1) average time complexity               |
| **Iteration Performance**     | Fast, but no specific order                | Slower due to sorting                     | Fast, maintains insertion order            |
| **Memory Usage**              | Less memory overhead                       | More memory overhead due to tree structure| More memory overhead due to linked list    |
| **Use Case**                  | Best for search operations                 | Best for sorted data                      | Best for maintaining insertion order       |

