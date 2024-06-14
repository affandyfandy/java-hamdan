# Assignment 6 - Stack and Heap

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
<img src="https://github.com/affandyfandy/java-hamdan/blob/main/secondLecture/Assignment6/images/fpt61 (1).png" width="1000">


### Example 2

```
public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.value = 5;
        changeReference(obj);
        System.out.println("obj.value after changeReference: " + obj.value);
        // Outputs: obj.value after changeReference: 5
    }

    public static void changeReference(MyClass x) {
        x = new MyClass(); // This changes the local reference, not the original reference
        x.value = 10;
    }
}

class MyClass {
    int value;
}
```

<img src="Assignment5\images\singleton.png" width="1000">
