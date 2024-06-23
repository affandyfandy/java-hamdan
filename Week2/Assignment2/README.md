# Question 1: what happen if implement 2 interface have same default method? How to solve? Demo in code.

In Java, if you implement two interfaces that have the same default method, you will encounter a `compile-time error`. This happens because the compiler cannot determine which default method to use in the implementing class. To resolve this issue, you need to override the conflicting default method in your class and explicitly specify which interface's default method should be used if needed. Let me give you an example:

## Example
```
public interface FirstInterface {
    void firstMethod(String string);

    default void log(String string) {
        System.out.println("FirstInterface log: " + string);
    }
}

public interface SecondInterface {
    void secondMethod();

    default void log(String str) {
        System.out.println("SecondInterface log: " + str);
    }
}

public class MyClass implements FirstInterface, SecondInterface{}
```
In this example there are 2 interface that contain the same default method, if a class implement these 2 interface there will be an `compile-time error`, so let's solve it

## Solve

```
public interface FirstInterface {
    void firstMethod(String string);

    default void log(String string) {
        System.out.println("FirstInterface log: " + string);
    }
}

public interface SecondInterface {
    void secondMethod();

    default void log(String str) {
        System.out.println("SecondInterface log: " + str);
    }
}

public class MyClass implements FirstInterface, SecondInterface {
    // Resolve the conflict by overriding the log method
    @Override
    public void log(String str) {
        System.out.println("MyClass log: " + str);
        // Optionally, call the default log methods from the interfaces
        FirstInterface.super.log(str);
        SecondInterface.super.log(str);
    }

    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.log("Logging from MyClass");
    }
}
```
## Output
```
MyClass log: Logging from MyClass
FirstInterface log: Logging from MyClass
SecondInterface log: Logging from MyClass
```


## Explanation

To avoid `compile-time error` because of two interfaces that have the same default method, we need to Override the conflicting default method in our class and optionally if you want to show the default method from the interfaces you can call both methods from both interfaces using `[YOURINTERFACE].super.log(str)`

# Question 2: Explain the difference between abstract class and interface

## Theory

| Aspect  | Abstract | Interface|
| ------------- | ------------- |-------------|
|Definition	  | 	A class that cannot be instantiated and is meant to be subclassed  |A contract that defines a set of methods but does not implement them            |
| Method  | Can have both abstract (without body) and non-abstract methods (with body)  |Can only have abstract methods (Java 7 and below); can have default and static methods (Java 8 and above)            |
| Fields  | Can have fields (instance variables)  |Cannot have fields, only constants (final static)            |
| Inheritance  | 	A class can extend only one abstract class  |A class can implement multiple interfaces|
| Constructors	  | Can have constructors  |Cannot have constructors       |
| Access Modifiers  |Can have access modifiers for methods and fields (private, protected, etc.)  |Methods in an interface are implicitly public and abstract (Java 7 and below)           |
| When to Use	  | Use when you want to share code among several closely related classes  |Use when you want to specify a contract that multiple classes should adhere to|
| Implementation	  | Can provide some implementation	  |Cannot provide any implementation in methods (except default and static methods in Java 8 and above)            |
| Usage	  | Suitable for sharing common methods and behavior among derived classes  |Suitable for defining capabilities or behaviors that can be implemented by any class, regardless of its place in the class hierarchy            |

## Syntax

### Abstract
```
abstract class Animal {
    // Abstract method (does not have a body)
    abstract void makeSound();

    // Regular method
    void eat() {
        System.out.println("This animal is eating.");
    }
}

class Dog extends Animal {
    // Providing implementation for the abstract method
    void makeSound() {
        System.out.println("Woof");
    }
}
```

### Interface
```
interface Animal {
    // Abstract method (implicitly public and abstract)
    void makeSound();

    // Default method (introduced in Java 8)
    default void eat() {
        System.out.println("This animal is eating.");
    }

    // Static method (introduced in Java 8)
    static void sleep() {
        System.out.println("This animal is sleeping.");
    }
}

class Dog implements Animal {
    // Providing implementation for the abstract method
    public void makeSound() {
        System.out.println("Woof");
    }
}
```

### Summary
| Aspect  | Abstract | Interface|
| ------------- | ------------- |-------------|
| Declaration  | `abstract class ClassName { ... }` | `interface InterfaceName { ... }`|
| Method Declaration  | Can have both abstract and non-abstract methods | All methods are abstract by default (Java 7 and below); can have default and static methods (Java 8 and above)|
| Inheritance Implementation  | Use `extends` keyword to inherit | Use `implements` keyword to implement|
| Access Modifiers	  | Methods and fields can have any access modifier | Methods are implicitly public (Java 7 and below); can specify public for default and static methods (Java 8 and above)|
| Fields  | 	Can have fields (with any access modifier) | Cannot have fields; can have constants (final static)
|

# Question 3: FunctionalInterface
In Java, the `@FunctionalInterface` annotation is used to indicate that an interface is intended to be a functional interface. A functional interface is an interface with exactly one abstract method, and it can be used as the assignment target for a lambda expression or method reference.

## Example: Using @FunctionalInterface Annotation

### 1. Define the Functional Interface

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    void execute(String message);

    // It's allowed to have default methods in a functional interface
    default void defaultMethod() {
        System.out.println("Default method");
    }

    // It's allowed to have static methods in a functional interface
    static void staticMethod() {
        System.out.println("Static method");
    }
}
```

### 2. Use the Functional Interface with a Lambda Expression

```java
public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        // Using a lambda expression to provide the implementation of the execute method
        MyFunctionalInterface myFunc = (message) -> {
            System.out.println("Executing with message: " + message);
        };

        // Call the method with a message
        myFunc.execute("Hello, Functional Interface!");

        // Call the default method
        myFunc.defaultMethod();

        // Call the static method
        MyFunctionalInterface.staticMethod();
    }
}
```

## Explanation

### Defining the Functional Interface
- The `MyFunctionalInterface` interface is annotated with `@FunctionalInterface`.
- It has one abstract method `execute(String message)`, making it a functional interface.
- It also contains a default method `defaultMethod()` and a static method `staticMethod()`. Default and static methods are allowed in functional interfaces.

### Using the Functional Interface
- In the `FunctionalInterfaceExample` class, a lambda expression is used to provide an implementation of the `execute` method of `MyFunctionalInterface`.
- The lambda expression `(message) -> { System.out.println("Executing with message: " + message); }` is assigned to an instance of `MyFunctionalInterface`.
- The `execute` method is called with a message, demonstrating how the lambda expression works.
- The default method `defaultMethod` and the static method `staticMethod` are also called to show their usage.

This example demonstrates how to define a functional interface with `@FunctionalInterface` and how to use it with lambda expressions, default methods, and static methods.