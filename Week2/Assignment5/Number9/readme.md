# Q9: Add employee to HashSet

To add employees to a HashSet and recognize if two employees have duplicated employee IDs, you need to override the equals and hashCode methods in the Employee class. Here is how you can do it:

```java
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class Employee {
    int employeeID;
    String name;

    public Employee(int employeeID, String name) {
        this.employeeID = employeeID;
        this.name = name;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeID == employee.employeeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID);
    }

    public static void main(String[] args) {
        Set<Employee> employeeSet = new HashSet<>();
        Employee emp1 = new Employee(1, "John");
        Employee emp2 = new Employee(2, "Jane");
        Employee emp3 = new Employee(1, "Doe"); // duplicate ID

        employeeSet.add(emp1);
        employeeSet.add(emp2);
        employeeSet.add(emp3); // will not be added

        employeeSet.forEach(employee -> {
            System.out.println("EmployeeID: " + employee.getEmployeeID() + ", Name: " + employee.getName());
        });
    }
}
```

## Explanation

### Converting List to Map

1. **Use Java Streams to Convert a List to a Map:**
   - Java Streams provide a clean and efficient way to convert a list of objects into a map.

2. **Using `Collectors.toMap`:**
   - The `Collectors.toMap` method is used to collect the elements of a stream into a map.
   - The first argument to `toMap` is a function that extracts the key (`employeeID`).
   - The second argument is a function that extracts the value (the `Employee` object).

3. **Handling Duplicate Keys:**
   - The merge function `(existing, replacement) -> existing` ensures that if there are duplicate keys, the existing value is retained, and the new value is ignored.

4. **Using `TreeMap` for Sorted Map:**
   - By specifying `TreeMap::new`, the resulting map is a `TreeMap`, which keeps the keys sorted in ascending order.

### Adding Employee to HashSet

1. **Override `equals` and `hashCode` Methods:**
   - In the `Employee` class, override the `equals` method to compare employees based on their `employeeID`.
   - Override the `hashCode` method to generate a hash code based on the `employeeID`.

2. **Ensuring Uniqueness in `HashSet`:**
   - When employees are added to the `HashSet`, the overridden `equals` and `hashCode` methods ensure that no two employees with the same `employeeID` can exist in the set.

3. **Behavior of `HashSet`:**
   - The `HashSet` relies on the `hashCode` method to place objects in buckets for efficient retrieval.
   - The `equals` method is used to check for equality, ensuring that only unique employees (based on `employeeID`) are added to the set.
