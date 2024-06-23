# Q11: Issue with the code

## Problem

The code attempts to modify a list (`data`) while iterating over it using a for-each loop. This causes a `ConcurrentModificationException`.

When using a for-each loop, the iterator is implicitly used to traverse the collection. Modifying the collection (e.g., removing an element) while iterating will cause a `ConcurrentModificationException` because the iterator detects the structural modification.

## Fix

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Demo {
    public static void demo() {
        List<String> data = new ArrayList<>();
        data.add("Joe");
        data.add("Helen");
        data.add("Test");
        data.add("Rien");
        data.add("Ruby");

        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String d = iterator.next();
            if (d.equals("Test")) {
                iterator.remove();
            }
        }
    }
}
```

## Explanation
- Use of Iterator: Instead of a for-each loop, an explicit iterator is used to traverse the list.
- Iterator's remove() Method: The iterator.remove() method is called to remove the element safely. This method ensures that the underlying collection is modified correctly without causing a ConcurrentModificationException.