import java.util.*;
import java.util.stream.Collectors;

class Employee {
    private String employeeID;
    private String name;

    public Employee(String employeeID, String name) {
        this.employeeID = employeeID;
        this.name = name;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

public class Number5 {
    public static void main(String[] args) {
        // Creating a list of employees
        List<Employee> employees = Arrays.asList(
                new Employee("3", "Alice"),
                new Employee("1", "Bob"),
                new Employee("2", "Charlie")
        );

        // Converting the list to a map and ordering by employeeID (key) in ascending order
        Map<String, Employee> employeeMap = employees.stream()
                .sorted(Comparator.comparing(Employee::getEmployeeID))
                .collect(Collectors.toMap(
                        Employee::getEmployeeID,
                        employee -> employee,
                        (oldValue, newValue) -> oldValue, // Handling duplicate keys
                        LinkedHashMap::new // Maintaining insertion order
                ));

        // Printing the map
        employeeMap.forEach((key, value) -> System.out.println(key + " : " + value));
    }
}
