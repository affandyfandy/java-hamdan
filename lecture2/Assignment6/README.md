# Assignment 6 - Stack and Heap

In Java, memory management is a vital process. It is managed by Java automatically. The JVM divides the memory into two parts: stack memory and heap memory. From the perspective of Java, both are important memory areas but both are used for different purposes.

img

## Stack Memory in Java
The stack in Java is a memory area used to store local variables and method call information during the execution of a program. Each time a method is called, a new frame is created on top of the stack, which holds the method's local variables and information.

## Heap Memory in Java 
Heap memory in Java is a part of memory allocated to Java applications for dynamic memory allocation. Objects and class instances are created in the heap memory at runtime. Unlike the stack, which follows a LIFO (Last In, First Out) order, the heap does not follow any specific order for allocation and deallocation of memory.  

## Code example

### Example 1
```
public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.value = 5;
        obj.name = "John";
        System.out.println("obj.value after modifyObject: " + obj.value);
        System.out.println("obj.value after modifyObject: " + obj.name);
        modifyObject(obj);
        System.out.println("obj.value after modifyObject: " + obj.value);
        // Outputs: obj.value after modifyObject: 10
        System.out.println("obj.value after modifyObject: " + obj.name);
        // Outputs: obj.value after modifyObject: doe
    }

    public static void modifyObject(MyClass x) {
        x.value = 10;
        x.name = "Doe";
    }
}

class MyClass {
    int value;
    String name;
}
```
<<<<<<< HEAD:lecture2/Assignment6/README.md
<img src="Assignment6\images\singleton.png" width="1000">

### Explanation
The ```Main``` method creates an instance of ```MyClass``` and assigns it to the reference variable ```obj```, which is stored on the stack. This ```MyClass``` instance, containing an integer value ```10``` and a string reference to ```"Doe"```, resides in the heap. The ```modifyObject``` method is called with ```obj``` as an argument, creating another stack reference ```x``` that points to the same heap object. Consequently, any modifications made to ```x``` inside ```modifyObject``` will directly affect the same ```MyClass``` instance referenced by ```obj```. 
=======
<img src="https://github.com/affandyfandy/java-hamdan/blob/main/secondLecture/Assignment6/images/fpt61 (1).png" width="1000">
>>>>>>> f57214c8944db3758622e08985338a7d82f61e7d:secondLecture/Assignment6/README.md


### Example 2

```
public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.value = 5;
        obj.name = "John"
        changeReference(obj);
        System.out.println("obj.value after changeReference: " + obj.value);
        // Outputs: obj.value after changeReference: 5
    }

    public static void changeReference(MyClass x) {
        x = new MyClass(); // This changes the local reference, not the original reference
        x.value = 10;
        x.name = "Doe"
    }
}

class MyClass {
    int value;
}
```

<img src="Assignment5\images\singleton.png" width="1000">
<<<<<<< HEAD:lecture2/Assignment6/README.md

### Explanation

The ```Main``` method creates a ```MyClass``` instance on the heap, referenced by the variable ```obj``` on the stack. When the ```modifyObject``` method is called with ```obj``` as an argument, it creates another reference ```x``` on the stack pointing to the same ```MyClass``` instance in the heap. Modifications made through ```x```, such as changing the integer value from ```10``` to ```5``` and the String name from ```"Doe"``` to ```"John"```, directly affect the same heap object referenced by ```obj```.

# Conclusion
The Java stack and heap both are used to store information but each have a different uses. The stack is a specific part of a computer's memory that is used to store information about local variables and function calls. It is quick to access because it is easy to reach, but it has a limited amount of space. In contrast, the heap is a section of memory that can store larger amounts of data, though it may take slightly longer to access than the stack.

=======
>>>>>>> f57214c8944db3758622e08985338a7d82f61e7d:secondLecture/Assignment6/README.md
