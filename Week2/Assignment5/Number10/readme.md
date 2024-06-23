# Q10: Creating Map of Employee with Composite Key (Department, EmployeeID)

## What we need to do:

1. **Define a Composite Key Class:**
   - Create a class to represent the composite key, consisting of `department` and `employeeID`.

2. **Implement `equals` and `hashCode` Methods in Composite Key Class:**
   - Override the `equals` method to compare both `department` and `employeeID`.
   - Override the `hashCode` method to generate a hash code based on both fields.

3. **Converting List to Map with Composite Key:**
   - Use Java Streams to convert the list to a map where the key is an instance of the composite key class.
   - Use `Collectors.toMap` to perform the conversion.
   - Specify a `TreeMap` to keep the keys sorted.

Code:

```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;

class Employee {
    int employeeID;
    String name;
    String department;
    // other fields, constructor, getters and setters
}

class CompositeKey {
    String department;
    int employeeID;

    public CompositeKey(String department, int employeeID) {
        this.department = department;
        this.employeeID = employeeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return employeeID == that.employeeID && Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, employeeID);
    }
}

public class EmployeeMapWithCompositeKeyExample {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "John", "HR"),
            new Employee(2, "Jane", "Finance"),
            new Employee(3, "Doe", "HR")
        );

        Map<CompositeKey, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(
                employee -> new CompositeKey(employee.getDepartment(), employee.getEmployeeID),
                employee -> employee,
                (existing, replacement) -> existing, // handle duplicate keys
                TreeMap::new // ensure map is sorted by keys
            ));

        employeeMap.forEach((key, employee) -> {
            System.out.println("Department: " + key.department + ", EmployeeID: " + key.employeeID + ", Name: " + employee.getName());
        });
    }
}
```