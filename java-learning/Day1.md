# Day 1 — How Java Works and Operators

## Learning goal

Before building Spring Boot applications, understand what happens when Java code is compiled and executed. Then connect that foundation to operators.

The practical lesson is [Day1.java](Day1.java).

## 1. The Java toolchain

### JDK — Java Development Kit

The JDK is used to develop Java applications. It contains:

- `javac` — the Java compiler
- `java` — the command used to start a JVM
- Java standard libraries
- The JVM
- Debugging and monitoring tools

### JRE — Java Runtime Environment

Historically, the JRE was the package required to run Java programs. It contained the JVM and runtime libraries.

Modern Java distributions generally provide a JDK rather than a separate downloadable JRE. A JDK still contains everything needed to run Java applications.

### JVM — Java Virtual Machine

The JVM executes Java bytecode. It provides:

- Platform independence
- Class loading
- Bytecode verification
- Memory management
- Garbage collection
- Thread management
- Just-In-Time compilation

## 2. From source code to execution

```text
Day1.java --javac--> Day1.class --java--> JVM --> operating system and CPU
```

1. You write Java source code in a `.java` file.
2. `javac` compiles the source into bytecode.
3. The bytecode is saved in a `.class` file.
4. `java Day1` starts the JVM.
5. The class loader loads `Day1.class`.
6. The JVM verifies the bytecode.
7. The JVM finds and calls `main()`.
8. The interpreter and JIT compiler execute the program.

The bytecode is portable. The JVM implementation handles the differences between macOS, Windows, Linux, Intel, and ARM processors.

## 3. JVM memory areas

### Stack

Every thread has its own stack. Each method call creates a stack frame containing execution information such as:

- Local variables
- Primitive values held locally
- References held locally
- Method parameters
- Return information

When a method returns, its stack frame is removed automatically.

Recursive calls can exhaust the stack and cause `StackOverflowError`.

### Heap

The heap is shared by application threads. Objects and arrays are generally allocated there:

```java
Task task = new Task("Learn Java");
```

`task` is a reference held by the executing method. The `Task` object is generally allocated on the heap.

If the JVM cannot allocate more heap memory, it can throw `OutOfMemoryError`.

### Metaspace

Metaspace stores metadata about loaded classes, such as their methods and fields. It is different from the heap objects created from those classes.

### Important clarification

The beginner explanation says “primitives are on the stack and objects are on the heap.” A more accurate explanation is:

- Local variables belong to method execution frames or registers.
- A local object variable normally stores a reference.
- The referenced object is generally on the heap.
- The JVM may optimize these details internally.

## 4. References

```java
Task first = new Task("Learn Java");
Task second = first;
```

There is one object and two references. Both references point to the same object.

When no reachable reference points to an object, the object becomes eligible for garbage collection.

## 5. Garbage collection

The garbage collector automatically reclaims heap objects that are no longer reachable by the application.

```java
Task task = new Task("Temporary task");
task = null;
```

The object may now be eligible for garbage collection. It is not necessarily collected immediately. The JVM chooses when collection is appropriate.

Garbage collection manages memory, not external resources. Files, sockets, database connections, and message consumers still need explicit lifecycle management.

## 6. Operators

### Arithmetic

```java
10 / 3       // 3: integer division
10.0 / 3     // 3.333...: floating-point division
10 % 3       // 1: remainder
```

At least one floating-point operand is required for a decimal result.

### Increment

```java
x++ // use the old value, then increment
++x // increment first, then use the new value
```

Avoid combining multiple increments in one complicated expression.

### Comparison

For primitives, `==` compares values. For objects, `==` compares references.

Use `.equals()` for object value comparison and `Objects.equals()` when a value may be `null`.

### Short-circuit logic

```java
user != null && user.isActive()
```

Java evaluates from left to right. If the first condition is false, it does not evaluate the second condition. This prevents a null dereference.

### Bitwise flags

Each permission can use one binary bit:

```java
int read = 0b0001;
int write = 0b0010;
int permissions = read | write;
boolean canRead = (permissions & read) != 0;
```

This pattern is useful for compact permission and feature-flag storage.

### Compound assignment

```java
byte value = 127;
value += 1; // -128 because byte arithmetic overflows
```

Compound assignment includes an implicit narrowing conversion. Overflow is not automatically rejected.

### Ternary operator

```java
String status = completed ? "Complete" : "In progress";
```

Use it for simple value selection. Use `if/else` when the decision becomes complex.

### `instanceof` pattern matching

```java
if (value instanceof String text) {
    System.out.println(text.toUpperCase());
}
```

The type check and cast are combined in modern Java.

## 7. Run Day 1

From the `java-learning` directory:

```text
javac Day1.java
java Day1
```

Change values in the file, predict the output, and run it again.

## Day 1 exercises

1. What does `5 / 2` produce?
2. How do you produce `2.5` from `5` and `2`?
3. Why does `new String("x") == new String("x")` return `false`?
4. Why is `user != null && user.isActive()` safe?
5. What happens when a `byte` containing `127` is incremented?
6. Why can an object remain alive after one reference is set to `null`?
7. What is the difference between an object being eligible for GC and actually being collected?
