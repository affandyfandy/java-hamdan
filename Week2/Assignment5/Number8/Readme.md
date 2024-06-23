# Q8: equals() and hashcode()

In Java, the `equals()` and `hashCode()` methods are essential for comparing objects and maintaining the integrity of collections that rely on hashing, such as `HashMap` and `HashSet`.

## `equals()` Method

The `equals()` method is used to compare two objects for equality. It checks if the two objects are logically "equal" rather than being the same instance. By default, the `equals()` method in the `Object` class compares memory addresses, so it needs to be overridden to provide meaningful equality comparisons.

## `hashCode()` Method

The `hashCode()` method returns an integer hash code value for the object. It is used in hashing-based collections to efficiently locate objects. When `equals()` is overridden, `hashCode()` must also be overridden to ensure that equal objects have the same hash code.

## Contract Between `equals()` and `hashCode()`

1. If two objects are equal according to the `equals()` method, they must have the same hash code.
2. If two objects have the same hash code, they are not necessarily equal.

## Example

Here is an example of a class overriding `equals()` and `hashCode()` methods:

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        return name != null ? name.equals(person.name) : person.name == null;
    }

    // Overriding hashCode() method
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }

    // Getters and toString() method for convenience
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Alice", 30);
        Person person3 = new Person("Bob", 25);

        System.out.println("person1.equals(person2): " + person1.equals(person2)); // true
        System.out.println("person1.equals(person3): " + person1.equals(person3)); // false
        System.out.println("person1.hashCode() == person2.hashCode(): " + (person1.hashCode() == person2.hashCode())); // true
        System.out.println("person1.hashCode() == person3.hashCode(): " + (person1.hashCode() == person3.hashCode())); // false
    }
}
```

## Explanation

- equals() Method: This method compares the name and age fields to determine equality. It returns true if both fields are equal in both objects.
- hashCode() Method: This method generates a hash code based on the name and age fields. It ensures that objects considered equal by the equals() method will have the same hash code.