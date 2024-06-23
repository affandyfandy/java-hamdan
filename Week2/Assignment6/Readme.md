# Q1: Parallel stream in java

## When to Use Parallel Streams

Parallel streams in Java are used when you want to perform operations on a large dataset concurrently to improve performance. They are especially useful when:

1. **Large Data Sets**: When you are working with large collections of data, parallel streams can help by dividing the work across multiple threads.
2. **CPU-Intensive Operations**: When the tasks you need to perform are CPU-bound and can benefit from multiple cores.
3. **Non-Interdependent Tasks**: When the operations do not depend on each other and can be executed independently.
4. **Stateless Operations**: When the operations do not maintain any state and can be performed in isolation.

### Notices

- **Thread Overhead**: Parallel streams can introduce thread overhead, which may not be beneficial for small datasets.
- **Thread Safety**: Ensure that the operations performed are thread-safe to avoid concurrency issues.
- **Order of Execution**: The order of processing might not be predictable, which can be an issue if order matters.

## Demo: Sum of a List of Numbers Using Parallel Stream

Let's demonstrate how to sum a list of numbers using parallel streams in Java.

### Step-by-Step Explanation

1. **Create a List of Numbers**: We will create a list of integers.
2. **Use Parallel Stream**: Convert the list to a parallel stream.
3. **Sum the Numbers**: Use the `reduce` method to sum the numbers.

### Code Example

```java
import java.util.Arrays;
import java.util.List;

public class ParallelStreamDemo {
    public static void main(String[] args) {
        // Step 1: Create a list of numbers
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Step 2: Use parallel stream to sum the numbers
        int sum = numbers.parallelStream()
                         .reduce(0, Integer::sum);

        // Step 3: Print the result
        System.out.println(sum); // output : 55
    }
}
```

### Code Explanation

First We create a list of integers from 1 to 10, then We convert the list to a parallel stream using `parallelStream()`. The reduce method is then used to sum the elements. `reduce(0, Integer::sum)` starts with an initial value of `0` and applies the sum operation and will output `55`.

# Q2: Remove all duplicate elements from a list of string using streams

## Code

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<String> listWithDuplicates = Arrays.asList("apple", "banana", "apple", "orange", "banana", "grape");

        // Using streams to remove duplicates
        List<String> listWithoutDuplicates = listWithDuplicates.stream()
                .distinct() // This method returns a stream with distinct elements (removes duplicates)
                .collect(Collectors.toList()); // Collect the results into a list

        System.out.println("List without duplicates: " + listWithoutDuplicates);
    }
}
```

## Code Explanation
A list named `listWithDuplicates` is created using Arrays. The stream is created from the original list using the `stream()` method. The `distinct()` method is called on the stream, which filters out the duplicate elements and returns a stream with only unique elements. The c`ollect(Collectors.toList())` method collects the elements of the stream back into a new list.

# Q3: Remove lines which is duplicated data by key field

In this questions let's say we have an employees dataset but the data entry team make a mistakes an they input one employee data twice

## CSV Example
```
id,name,age
1,John Doe,30
2,Jane Smith,25
3,John Doe,30
4,Emily Davis,22
2,Jane Smith,25
5,Michael Brown,35
```

Let's solve this problem in java using Stream with IO

## Code
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveDuplicateLines {

    public static void main(String[] args) {
        String inputFilePath = "input.csv"; // Change this to your input file path
        String outputFilePath = "output.csv"; // Change this to your output file path
        int keyFieldIndex = 0; // Change this to the index of your key field (0-based index for CSV)

        removeDuplicates(inputFilePath, outputFilePath, keyFieldIndex);
    }

    public static void removeDuplicates(String inputFilePath, String outputFilePath, int keyFieldIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String header = reader.readLine();
            writer.write(header);
            writer.newLine();

            Set<String> uniqueKeys = new HashSet<>();

            // Read and process lines using streams
            reader.lines()
                  .filter(line -> {
                      String[] fields = line.split(",");
                      return uniqueKeys.add(fields[keyFieldIndex]);
                  })
                  .forEach(line -> {
                      try {
                          writer.write(line);
                          writer.newLine();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Output

```
id,name,age
1,John Doe,30
2,Jane Smith,25
3,John Doe,30
4,Emily Davis,22
5,Michael Brown,35
```

## Explanation

In the example CSV file input.csv, the lines with id 2 appear more than once. The code will remove these duplicates, keeping only one of each id. In this implementation, Java Streams are used in conjunction with IO operations to efficiently process the file. The `lines()` method of `BufferedReader` converts the input file into a Stream, allowing us to perform Stream operations on the lines. We use the `filter()` method to remove duplicate lines based on a key field by maintaining a `HashSet` of unique keys.

# Q4: Count the number of strings in a list that start with a specific letter using streams

## Code

```java
import java.util.Arrays;
import java.util.List;

public class CountStrings {
    public static void main(String[] args) {
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
        String startingLetter = "G";

        long count = colors.stream()
                           .filter(color -> color.startsWith(startingLetter))
                           .count();

        System.out.println("Number of strings starting with " + startingLetter + ": " + count);
    }
}
```
## Explanation
The `stream()` method creates a stream from the list. The `filter()` method is used to include only those strings that start with the specified letter by using the `startsWith()` method. Finally,` the count()` method returns the number of elements that match the filter criteria.

# Q5: Sort name, Find employee, Check any employee names match

## Example input
```
1, John Doe, 30, 60000,
2, Jane Smith, 25, 75000,
3, Emily Davis, 35, 80000,
4, Michael Brown, 40, 90000
```

## Code
```java
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "John Doe", 30, 60000),
                new Employee(2, "Jane Smith", 25, 75000),
                new Employee(3, "Emily Davis", 35, 80000),
                new Employee(4, "Michael Brown", 40, 90000)
        );

        // Sort employees by name in alphabetical ascending order
        List<Employee> sortedEmployees = employees.stream()
                .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                .collect(Collectors.toList());

        System.out.println("Employees sorted by name:");
        sortedEmployees.forEach(System.out::println);

        // Find employee with maximum salary
        Employee maxSalaryEmployee = employees.stream()
                .max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()))
                .orElseThrow(NoSuchElementException::new);

        System.out.println("Employee with the highest salary:");
        System.out.println(maxSalaryEmployee);

        // Check if any employee names match with specific keywords
        List<String> keywords = Arrays.asList("John", "Jane");
        boolean anyMatch = employees.stream()
                .anyMatch(e -> keywords.stream().anyMatch(keyword -> e.getName().contains(keyword)));

        System.out.println("Any employee name matches with specific keywords: " + anyMatch);
    }
}

class Employee {
    private int id;
    private String name;
    private int age;
    private double salary;

    public Employee(int id, String name, int age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
```

## Output
**Employees sorted by name:**
```
Employee{id=3, name='Emily Davis', age=35, salary=80000.0}
Employee{id=2, name='Jane Smith', age=25, salary=75000.0}
Employee{id=1, name='John Doe', age=30, salary=60000.0}
Employee{id=4, name='Michael Brown', age=40, salary=90000.0}
```
**Employee with the highest salary:**
```
Employee{id=4, name='Michael Brown', age=40, salary=90000.0}
```
**Any employee name matches with specific keywords:**
```
true
```

## Explanation

- Sort by Name: The stream is sorted alphabetically by employee names using `sorted()` and the `compareTo()` method. The sorted list is then collected back into a list and printed. 
- Find Maximum Salary: The stream finds the employee with the maximum salary using `max()` and `Double.compare()`. The result is printed.
- Check for Name Keywords: The stream checks if any employee's name contains specific keywords using `anyMatch()`. The result is printed.

# Q6: Convert list employees to map with ID as key
## Example input
```
1, John Doe, 30, 60000,
2, Jane Smith, 25, 75000,
3, Emily Davis, 35, 80000,
4, Michael Brown, 40, 90000
```

## Code
```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "John Doe", 30, 60000),
                new Employee(2, "Jane Smith", 25, 75000),
                new Employee(3, "Emily Davis", 35, 80000),
                new Employee(4, "Michael Brown", 40, 90000)
        );

        // Convert list of employees to a map with ID as the key
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, employee -> employee));

        // Print the map
        employeeMap.forEach((id, employee) -> System.out.println("ID: " + id + ", Employee: " + employee));
    }
}

class Employee {
    private int id;
    private String name;
    private int age;
    private double salary;

    public Employee(int id, String name, int age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
```

## Output
```
ID: 1, Employee: Employee{id=1, name='John Doe', age=30, salary=60000.0}
ID: 2, Employee: Employee{id=2, name='Jane Smith', age=25, salary=75000.0}
ID: 3, Employee: Employee{id=3, name='Emily Davis', age=35, salary=80000.0}
ID: 4, Employee: Employee{id=4, name='Michael Brown', age=40, salary=90000.0}
```

## Explanation

The `stream()` method creates a stream from the list. The `collect()` method uses `Collectors.toMap` to convert the stream to a map, with the employee `ID` as the key and the `Employee` object as the value. The `Employee::getId` method reference is used to extract the key.
