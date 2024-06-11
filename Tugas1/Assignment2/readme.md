# OOP

Object-Oriented Programming (OOP) principles are fundamental concepts that guide the design and development of software using objects and classes. Java, being an object-oriented language, strongly supports these principles. The four main OOP principles are encapsulation, inheritance, polymorphism, and abstraction. Here is how these principles are implemented and utilized in Java:

<p align="center">
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment2/images/oop.png?raw=true" width="500">
</p>

## Encapsulation

Encapsulation is when data and the methods that work with the data are bundled together into a single unit called a class. It restricts access to some of the object’s components and prevents the accidental modification of data.

<p align="center">
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment2/images/encapsulation.png?raw=true" width="500">
</p>


In Java, you can use access modifiers (private, protected, public) to control the visibility of variables and methods. For example:
```
public class Person {
    private String name;
    private int age;

    // Getter method for name
    public String getName() {
        return name;
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for age
    public int getAge() {
        return age;
    }

    // Setter method for age
    public void setAge(int age) {
        this.age = age;
    }
}
```

## Inheritance
Inheritance is a mechanism that allows a class to inherit properties and behaviors from another class, establishing a relationship between them. The class that is inherited from is called the superclass (or base class), and the class that inherits is called the subclass (or derived class).

<p align="center">
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment2/images/inheritance.png?raw=true" width="500">
</p>


To implement inheritance in java, use the extends keyword to define a subclass, for example:
```
// Superclass
public class Animal {
    public void eat() {
        System.out.println("This animal eats food.");
    }
}

// Subclass
public class Dog extends Animal {
    public void bark() {
        System.out.println("The dog barks.");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();  // Inherited method
        dog.bark(); // Subclass method
    }
}
```
output
```
This animal eats food.
The dog barks.
```

## Polymorphism
Polymorphism allows objects of different classes to be treated as objects of a common superclass. It provides the ability to call the same method on different objects and have each of them respond in their own way.

<p align="center">
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment2/images/polymorphism.png?raw=true" width="500">
</p>

To implement polymorphism in java, you can use Overriding, for example:
```
// Superclass
public class Animal {
    public void makeSound() {
        System.out.println("Animal makes a sound.");
    }
}

// Subclass
public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Dog barks.");
    }
}

// Subclass
public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Cat meows.");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Animal myDog = new Dog();
        Animal myCat = new Cat();
        
        myDog.makeSound();
        myCat.makeSound();
    }
}
```
Output:
```
Dog barks.
Cat meows.
```

## Abstraction
Abstraction is the principle of hiding the complex implementation details and showing only the essential features of the object.

<p align="center">
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment2/images/abstraction.png?raw=true" width="500">
</p>

To implement abstraction in java use abstract classes and interfaces, for example:
```
// Abstract class
abstract class Animal {
    // Abstract method (does not have a body)
    public abstract void makeSound();

    // Regular method
    public void sleep() {
        System.out.println("This animal sleeps.");
    }
}

// Subclass (inheriting from Animal)
public class Dog extends Animal {
    public void makeSound() {
        System.out.println("Dog barks.");
    }
}

// Interface
interface AnimalInterface {
    void makeSound();
    void sleep();
}

// Class implementing the interface
public class Cat implements AnimalInterface {
    @Override
    public void makeSound() {
        System.out.println("Cat meows.");
    }

    @Override
    public void sleep() {
        System.out.println("Cat sleeps.");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Animal myDog = new Dog();
        myDog.makeSound(); // Outputs: Dog barks.
        myDog.sleep();     // Outputs: This animal sleeps.

        AnimalInterface myCat = new Cat();
        myCat.makeSound(); // Outputs: Cat meows.
        myCat.sleep();     // Outputs: Cat sleeps.
    }
}
```
Output:
```
Dog Barks
This animal sleeps
Cat meows
Cat sleeps
```
