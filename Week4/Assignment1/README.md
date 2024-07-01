# Assignment 1: Dependency Injection (DI)

## 1. Advantages and Drawbacks of Dependency Injection :

### Advantage
1. **Decoupling**: DI helps in decoupling the classes from their dependencies, making the code more modular and easier to manage.
2. **Improved Testability**: It makes unit testing easier by allowing mock dependencies to be injected, facilitating isolated testing.
3. **Flexibility**: Dependencies can be changed without modifying the class, making it easier to switch implementations or configurations.
4. **Maintainability**: With DI, code becomes easier to maintain as changes in dependencies don’t require changes in the dependent classes.
5. **Reusability**: Code can be reused more easily since dependencies are injected rather than hardcoded, allowing for different implementations to be used as needed.

### Drawbacks:
1. **Complexity**: Implementing DI can add a layer of complexity to the application, especially for developers who are not familiar with the pattern.
2. **Overhead**: There can be performance overhead due to the additional abstraction layers and the process of resolving dependencies.
3. **Configuration Management**: Managing configurations for DI can become cumbersome, particularly in large applications with many dependencies.
4. **Learning Curve**: Developers need to learn and understand DI and IoC principles, which can be a hurdle for beginners.
5. **Debugging Difficulty**: Debugging can be more challenging as the actual creation of objects is abstracted away, making it harder to trace issues related to dependency resolution.

### Code Example Without Dependency Injection:
```java
public class Service {
    private Repository repository = new Repository();

    public void performOperation() {
        repository.save();
    }
}

public class Repository {
    public void save() {
        System.out.println("Saving data");
    }
}

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        service.performOperation();
    }
}
```

### Explanation:
- **Service Class**: This class depends directly on the Repository class. The repository instance is created within the Service class, making it tightly coupled to Repository.

- **Repository Class**: This class has a method save() which simulates saving data to a database.

- **Main Class**: This is the entry point of the application. It creates an instance of Service and calls the performOperation() method.

### Problems:

- **Tight Coupling**: The Service class is tightly coupled to the Repository class. If you want to change the implementation of Repository, you have to modify the Service class.
- **Hard to Test**: Testing the Service class in isolation is difficult because it creates its own instance of Repository. You can't easily replace Repository with a mock or stub for testing purposes.
### Code Example With Dependency Injection

```java
public class Service {
    private Repository repository;

    // Constructor injection
    public Service(Repository repository) {
        this.repository = repository;
    }

    public void performOperation() {
        repository.save();
    }
}
```

### Explanation:
- **Dependency Injection**: The Repository instance is now passed to the Service class through the constructor (constructor injection). This makes Service independent of how Repository is instantiated.

- **The Service** class has a performOperation() method that uses the injected Repository instance to save data.
Repository Class: This remains the same as before. It has a save() method to simulate saving data.

- **Main Class**: This is the entry point of the application. It creates instances of Repository and Service, passing the Repository instance to the Service constructor.

### Advantages:
- **Loose Coupling**: The Service class is now loosely coupled with the Repository class. The Service class only knows about the Repository interface (or class) and does not create its own instance.
- **Improved Testability**: You can easily replace the Repository with a mock or stub when testing the Service class. This makes unit testing simpler and more effective.
- **Flexibility**: Changing the Repository implementation (e.g., to MockRepository for testing) doesn't require changing the Service class.

# Number 2: Convert XML Bean Declaration to Java Configuration

Now, we will try an implementation to convert a XML Bean Declaration to Java configuration using @Bean annonation with Constructor Injection. We also will add more atttributes to the employee like ID and age.

## Original XML Configuration

```java
<beans>
    <bean id="employee" class="com.helen.demo.entity.Employee">
        <constructor-arg name="name" value="GL"/>
        <constructor-arg name="employeeWork">
            <bean class="com.helen.demo.EmployeeWork"/>
        </constructor-arg>
    </bean>
</beans>
```
In this configuration:

- An `Employee` bean is defined with an ID of "employee".
- The `Employee` bean has two constructor arguments: `name` (a string) with the value of `GL` and `employeeWork` (a bean of type EmployeeWork).

## Converting to Java Configuration

To convert this XML configuration to Java configuration using ```@Configuration``` and ``@Bean`` annotations, follow these steps:

- **Create a Configuration Class**: This class will be annotated with `@Configuration` to indicate that it contains Spring bean definitions.
- **Define Beans with Constructor Injection**: Use the `@Bean` annotation to define beans and use constructor arguments to inject dependencies.

now to implement it we need to change **Employee.java**, **AppConfig.java** and **SimpleDemoApplication.java**

## Employee.java

The main change in this class is only we need to add more attributes following the requirements of this assignment

```java
public class Employee {
    private int id;
    private String name;
    private int age;
    private EmployeeWork employeeWork;

    // Constructor
    public Employee(int id, String name, int age, EmployeeWork employeeWork) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.employeeWork = employeeWork;
    }

    public void working() {
        System.out.println("My ID is: " + id);
        System.out.println("My Name is: " + name);
        System.out.println("My Age is: " + age);
        employeeWork.work();
    }
}
```

## AppConfig.java

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeWork employeeWork() {
        return new EmployeeWork();
    }

    @Bean
    public Employee employee() {
        return new Employee(1, "GL", 30, employeeWork());
    }
}
```

**Explanation:**

- **@Configuration**: Marks the class as a source of bean definitions.
- **@Bean**: Indicates that a method produces a bean to be managed by the Spring container.
- `employeeWork()`: Defines an `EmployeeWork` bean.
- `employee()`: Defines an `Employee` bean with constructor injection. It takes four parameters ID, Name, Age and emplyeeWork.

## SimpleDemoApplication.java

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SimpleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleDemoApplication.class, args);

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Employee employee = context.getBean(Employee.class);
        employee.working();
    }
}
```

**Explanation:**

- **SpringApplication.run(SimpleDemoApplication.class, args)**: Bootstraps the application.
- **AnnotationConfigApplicationContext(AppConfig.class)**: Loads the Spring context from the Java configuration class `AppConfig`.
- **context.getBean(Employee.class)**: Retrieves the `Employee` bean from the Spring context.
- **employee.working()**: Calls the `working()` method on the `Employee` bean to demonstrate that the bean has been correctly instantiated and injected.

# Number 3: Using Setter-Based Dependency Injection and Field Dependency for #2

Now that we have the java configuration we will modify the configuration to use setter-based and field-based dependency injection.

## Setter-based dependency injection

**Setter-based dependency injection** involves providing dependencies to a class through setter methods. This allows for optional dependencies and the ability to change dependencies after object creation.

Too use **Setter-based dependency injection** we need to do 2 steps:

- **Employee.java**: We need to update this class so this class has setter methods to inject dependencies
- **AppConfig.java**: We need to set each employee's attribute with the setter method.

### Employee.java

```java
public class Employee {
    private int id;
    private String name;
    private int age;
    private EmployeeWork employeeWork;

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmployeeWork(EmployeeWork employeeWork) {
        this.employeeWork = employeeWork;
    }

    public void working() {
        System.out.println("My ID is: " + id);
        System.out.println("My Name is: " + name);
        System.out.println("My Age is: " + age);
        employeeWork.work();
    }
}
```
### AppConfig.java
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeWork employeeWork() {
        return new EmployeeWork();
    }

    @Bean
    public Employee employee() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("GL");
        employee.setAge(30);
        employee.setEmployeeWork(employeeWork());
        return employee;
    }
}
```
## Field-Based Dependency Injection
**Field-based dependency injection** involves directly injecting dependencies into fields using the `@Autowired` annotation. This approach is simple and direct.

Too use **Field-based dependency injection** we need to do 2 steps:
- **Employee.java**: Each attributes need to be annonated with `@Autowired`
- **AppConfig.java**: Define beans for the dependencies you want to inject

### Employee.java
```java
import org.springframework.beans.factory.annotation.Autowired;

public class Employee {
    @Autowired
    private int id;

    @Autowired
    private String name;

    @Autowired
    private int age;

    @Autowired
    private EmployeeWork employeeWork;

    public void working() {
        System.out.println("My ID is: " + id);
        System.out.println("My Name is: " + name);
        System.out.println("My Age is: " + age);
        employeeWork.work();
    }
}
```

### AppConfig.java

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeWork employeeWork() {
        return new EmployeeWork();
    }

    @Bean
    @Primary
    public int id() {
        return 1;
    }

    @Bean
    @Primary
    public String name() {
        return "GL";
    }

    @Bean
    @Primary
    public int age() {
        return 30;
    }

    @Bean
    public Employee employee() {
        return new Employee();
    }
}
```

## Summary of Dependency Injection Methods

| **Injection Method**     | **Description**                                                                 | **Advantages**                                                                                   | **Disadvantages**                                                                                    |
|--------------------------|---------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| Constructor Injection    | Dependencies are provided through the constructor.                              | Ensures all required dependencies are provided at object creation.                              | Becomes cumbersome with many dependencies.                                                          |
| Setter Injection         | Dependencies are provided through setter methods.                               | - Allows optional dependencies. <br> - Can change dependencies after object creation.             | Dependencies are mutable, which can lead to inconsistent state.                                      |
| Field Injection          | Dependencies are injected directly into fields using the `@Autowired` annotation.| Simple and direct.                                                                              | - Makes dependencies less visible and harder to detect. <br> - Less flexible, as dependencies are injected after object creation. |

