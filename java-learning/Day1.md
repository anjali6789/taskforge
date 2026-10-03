# Day 1 — How Java Works and Operators

## Goal

Understand the Java execution model and the operators used in everyday application logic.

## Topics

- JDK, JVM, bytecode, and JIT compilation
- References, objects, heap, stack, and garbage collection
- Integer versus floating-point division
- `==` versus `.equals()`
- Short-circuit evaluation with `&&`
- Bitwise permission flags
- Compound assignment and numeric overflow
- Ternary expressions
- `instanceof` pattern matching

## Java execution flow

```text
Day1.java --javac--> Day1.class --java--> JVM --> operating system
```

`javac` compiles source code into bytecode. The `java` command starts the JVM, which loads and executes that bytecode.

## Run the lesson

From this directory:

```text
javac Day1.java
java Day1
```

## Key ideas

### Integer division

```java
5 / 2          // 2
(double) 5 / 2 // 2.5
```

A floating-point operand is required for a decimal result.

### References and equality

For primitives, `==` compares values. For objects, `==` compares references. Use `.equals()` to compare object values.

### Short-circuit logic

```java
user != null && user.isActive()
```

If `user` is `null`, Java does not evaluate the second condition, preventing a null dereference.

### Garbage collection

An object becomes eligible for garbage collection when no reachable references point to it. Eligibility does not mean the object is collected immediately.

## Exercises

1. What does `5 / 2` produce? Explain why.
2. How do you produce `2.5` from `5` and `2`?
3. Why does `new String("x") == new String("x")` return `false`?
4. Why is `user != null && user.isActive()` safe?
5. What happens when a `byte` containing `127` is incremented?
6. Add an `EXECUTE` permission flag and test it with `&`.
7. Change the values in `Day1.java`, predict the output, then run it.

## Connection to TaskForge

Operators are used in validation, authorization, task status checks, feature flags, and service-layer business rules.
