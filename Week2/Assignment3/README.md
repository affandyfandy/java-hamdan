# Q1: try-with-resources

The try-with-resources statement in Java is a try statement that declares one or more resources. A resource is an object that must be closed after the program is finished with it. The try-with-resources statement ensures that each resource is closed at the end of the statement. Any object that implements the java.lang.AutoCloseable or java.io.Closeable interface can be used as a resource.

## Syntax
```
try (ResourceType resource = new ResourceType()) {
    // Use the resource
} catch (ExceptionType e) {
    // Handle exceptions
}
```
Example:
```
class MyResource implements AutoCloseable {
    public void doSomething() {
        System.out.println("Doing something with the resource");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Closing the resource");
    }
}

public class CustomResourceExample {
    public static void main(String[] args) {
        try (MyResource resource = new MyResource()) {
            resource.doSomething();
        } catch (Exception e) {
            System.err.println("An exception was caught: " + e.getMessage());
        }
    }
}
```

## Benefit
- **Automatic Resource Management**: Resources are automatically closed at the end of the try block, reducing the risk of resource leaks.
- **Simpler Code**: Eliminates the need for explicit finally blocks to close resources, making the code cleaner and more readable.
- **Exception Handling**: Handles exceptions that occur when closing resources. If an exception occurs in both the try block and when closing the resource, the exception from the try block is suppressed, and the exception from closing the resource is thrown.

# Q2: Throw vs Throws

The throw and throws are the concepts of exception handling in Java where the throw keyword throws the exception explicitly from a method or a block of code, whereas the throws keyword is used in the signature of the method.

The `throw` keyword is used to explicitly throw an exception from a method or any block of code. You can throw either checked or unchecked exceptions. 

The `throws` keyword is used in a method signature to declare that the method might throw one or more exceptions. It is used to propagate the exceptions to the caller of the method.

## Example
### Throw
```
public class ThrowExample {
    public static void main(String[] args) {
        try {
            checkAge(15);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to check age
    public static void checkAge(int age) {
        if (age < 18) {
            // Throwing an unchecked exception
            throw new IllegalArgumentException("Age must be 18 or older.");
        } else {
            System.out.println("You are eligible to vote.");
        }
    }
}
```

### Throws
```
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ThrowsExample {
    public static void main(String[] args) {
        try {
            readFile("example.txt");
        } catch (IOException e) {
            System.out.println("An IOException was caught: " + e.getMessage());
        }
    }

    // Method that declares it can throw an IOException
    public static void readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
}
```

## Key Differences

| Aspect       | Throw | Throws |
| ------------- | ------------- | ------------- |
| Purpose  | Used to explicitly throw an exception	  | Used to declare that a method may throw exceptions|
| Placement  | Inside a method or block of code	  | In the method signature  |
| Type of Exception	  | Can be used to throw any exception (checked or unchecked)	  | Declares which exceptions a method can throw  |
| Propagation  | Does not propagate exceptions	  | Propagates exceptions to the caller  |
| Example Usage	  | `throw new IllegalArgumentException("Error");`	  | `public void myMethod() throws IOException`  |