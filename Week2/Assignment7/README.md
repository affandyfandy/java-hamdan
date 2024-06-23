# Q1: Remove duplicated items for any object and any duplicated fields

## Example input
```
"John", "Doe", 30
"Jane", "Doe", 25
"John", "Doe", 30
```

## Code

```java
```java
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DuplicateRemover {
    public static <T> List<T> removeDuplicates(List<T> list, String... fields) {
        List<T> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (T item : list) {
            StringBuilder keyBuilder = new StringBuilder();

            for (String field : fields) {
                try {
                    Field f = item.getClass().getDeclaredField(field);
                    f.setAccessible(true);
                    Object value = f.get(item);
                    keyBuilder.append(value != null ? value.toString() : "null").append("-");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            String key = keyBuilder.toString();
            if (!seen.contains(key)) {
                seen.add(key);
                result.add(item);
            }
        }
        return result;
    }
}

class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", Age: " + age;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("John", "Doe", 30),
            new Person("Jane", "Doe", 25),
            new Person("John", "Doe", 30)
        );

        List<Person> uniquePeople = DuplicateRemover.removeDuplicates(people, "firstName", "lastName", "age");
        uniquePeople.forEach(System.out::println);
    }
}
```

## Output
```
John Doe, Age: 30
Jane Doe, Age: 25
```

## Explanation
generic method `removeDuplicates` in the `DuplicateRemover` class that eliminates duplicate objects from a list based on specified fields by using Java Reflection to dynamically access and compare the values of these fields; the method constructs a unique key for each object and uses a set to track and ensure only unique objects are added to the result list

# Q2: Demo: Using Wildcards With Generics

## Example Input
- List of strings: ["Hello", "World"]
- List of integers: [1, 2, 3, 4, 5]
- List of doubles: [1.1, 2.2, 3.3]
- Empty list of numbers: []

## Code
```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildcardDemo {

    // Method using an unbounded wildcard
    public static void printList(List<?> list) {
        for (Object elem : list) {
            System.out.println(elem);
        }
    }

    // Method using a bounded wildcard with an upper bound
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Method using a bounded wildcard with a lower bound
    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) {
        // Unbounded wildcard example
        List<String> stringList = Arrays.asList("Hello", "World");
        printList(stringList);

        // Bounded wildcard with upper bound example
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Sum: " + sumOfList(intList));

        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
        System.out.println("Sum: " + sumOfList(doubleList));

        // Bounded wildcard with lower bound example
        List<Number> numberList = new ArrayList<>();
        addNumbers(numberList);
        printList(numberList);
    }
}
```

## Output
```
Hello
World
Sum: 15.0
Sum: 6.6
1
2
3
4
5
6
7
8
9
10
```

The provided code demonstrates the use of wildcards with generics in three different contexts: unbounded wildcards, bounded wildcards with an upper bound, and bounded wildcards with a lower bound. The `printList` method uses an unbounded wildcard to accept a list of any type, `sumOfList` uses a bounded wildcard with an upper bound to accept a list of any type that extends `Number`, and `addNumbers` uses a bounded wildcard with a lower bound to accept a list of any type that is a superclass of Integer.

# Q3: Sort List, Find max

## Input
"John", "Doe", 30
"Jane", "Doe", 25
"Alice", "Smith", 35

## Code
```java
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class ObjectUtils {

    // Method to sort a list of objects by a specified field
    public static <T> void sortByField(List<T> list, String fieldName) {
        list.sort((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Comparable value1 = (Comparable) field.get(o1);
                Comparable value2 = (Comparable) field.get(o2);
                return value1.compareTo(value2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    // Method to find the item with the maximum value for a specified field
    public static <T> T findMaxByField(List<T> list, String fieldName) {
        return list.stream().max((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Comparable value1 = (Comparable) field.get(o1);
                Comparable value2 = (Comparable) field.get(o2);
                return value1.compareTo(value2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        }).orElse(null);
    }
}

class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", Age: " + age;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("John", "Doe", 30));
        people.add(new Person("Jane", "Doe", 25));
        people.add(new Person("Alice", "Smith", 35));

        System.out.println("Original list:");
        people.forEach(System.out::println);

        // Sort by age
        ObjectUtils.sortByField(people, "age");
        System.out.println("\nSorted by age:");
        people.forEach(System.out::println);

        // Find person with maximum age
        Person oldest = ObjectUtils.findMaxByField(people, "age");
        System.out.println("\nPerson with maximum age:");
        System.out.println(oldest);
    }
}
```

## Output
```
Original list:
John Doe, Age: 30
Jane Doe, Age: 25
Alice Smith, Age: 35

Sorted by age:
Jane Doe, Age: 25
John Doe, Age: 30
Alice Smith, Age: 35

Person with maximum age:
Alice Smith, Age: 35
```

## Explanation
utility class ObjectUtils with two generic methods: sortByField and findMaxByField. The sortByField method sorts a list of objects by a specified field using Java Reflection to access the field values dynamically and Comparator to compare them. The findMaxByField method finds the object with the maximum value for a specified field using Java Reflection and streams.

# Q4: Convert list any object to map with any key field

## Input
```
"John", "Doe", 30
"Jane", "Doe", 25
"Alice", "Smith", 35
```

## Code
```java
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ObjectConverter {

    // Method to convert a list of objects to a map using a specified key field
    public static <T, K> Map<K, T> listToMap(List<T> list, String keyFieldName) {
        Map<K, T> map = new HashMap<>();
        for (T item : list) {
            try {
                Field keyField = item.getClass().getDeclaredField(keyFieldName);
                keyField.setAccessible(true);
                K key = (K) keyField.get(item);
                map.put(key, item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}

class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", Age: " + age;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Person> people = List.of(
            new Person("John", "Doe", 30),
            new Person("Jane", "Doe", 25),
            new Person("Alice", "Smith", 35)
        );

        // Convert list to map using 'firstName' as the key field
        Map<String, Person> personMap = ObjectConverter.listToMap(people, "firstName");
        personMap.forEach((key, value) -> System.out.println(key + " -> " + value));
    }
}
```

## Output
John -> John Doe, Age: 30
Jane -> Jane Doe, Age: 25
Alice -> Alice Smith, Age: 35



