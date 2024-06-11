
# Value types and reference types in Java
<img src="https://github.com/Zaniiiii/Fpt-Assignment1/blob/main/Assignment1/images/valueVSreferences.png?raw=true" width="1000">

In Java, there are two main types of data: value types and reference types. Here is an explanation of each:

## Value types
Java does not have built-in support for value types like some other programming languages (e.g., C# with its struct type). In Java, all primitive data types are value types. Value types store the actual data value directly in the variable, and they are passed by value. When you assign a value to another variable, a copy of the value is made.

### Examples

- int
- float
- double
- char
- boolean
- byte
- short
- long

### Characteristics

- **Memory Allocation**: Stored on the stack.
- **Copying**: When a value type is assigned to another variable, the value is copied. Both variables hold the same value, but they are independent of each other.
- **Performance**: Faster because they are stored on the stack and do not require complex memory management.

### Code Examples
```
public class Main {
    public static void main(String[] args){
        int myNum = 5;               // Integer (whole number)
        System.out.println("int: " + myNum);
        float myFloatNum = 5.99f;    // Floating point number
        System.out.println("float: " + myFloatNum);
        char myLetter = 'h';         // Character
        System.out.println("char: " + myLetter);
        boolean myBool = true;       // Boolean
        System.out.println("bool: " + myBool);
    }
}
```

Output:
```
int: 5
float: 5.99
char: h
bool: true
```

## References types
Reference types are data types that store a reference (memory address) to the actual object in the heap. When you assign a reference to another variable, only the reference is copied, not the object itself.

### Examples

- string
- array
- class
- objects created from custom classes

### Characteristics

- **Memory Allocation**: The reference is stored on the stack, but the actual object is stored in the heap.
- **Copying**: When a reference type is assigned to another variable, only the reference is copied. Both variables point to the same object in memory.
- **Performance**: More complex due to heap memory management and garbage collection.

### Code Examples
```
class MyClass {
    int value;

}

public class App {
    public static void main(String[] args) {
        MyClass obj1 = new MyClass();
        obj1.value = 5;
        obj1.value2 = 3.45;
        obj1.bool = true;
        System.out.println(obj1.value);
        System.out.println(obj1.value2);
        System.out.println(obj1.bool);
        MyClass obj2 = obj1;    // obj2 gets the same reference as obj1
        obj2.value2 = 5.5;      // Changing the value2 through obj2 also changes obj1
        obj2.value = 10;        // Changing the value through obj2 also changes obj1
        System.out.println(obj1.value);
        System.out.println(obj1.value2);
        System.out.println(obj1.bool);
    }
}
```

Output:
```
5
3.45
true
10
5.5
true
```
