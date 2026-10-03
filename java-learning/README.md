# Java Core Concepts - Deep Dive

> **For developers who want to understand Java at a professional level.**

Each file contains production-level insights, common pitfalls, best practices, and interview-relevant gotchas that a 3-year experienced developer should know.

## Learning Path

The current beginner-first entry point is:

| Day | Topic | Files |
|---|---|---|
| 1 | How Java works and Operators | [Day1.md](Day1.md), [Day1.java](Day1.java) |

`Day1.java` combines the theory and runnable experiments for the first day. The older topic-named files remain as reference material while the course is being reorganized into day-based lessons.

| # | Topic | File | Key Concepts |
|---|-------|------|--------------|
| 1 | **Variables & Data Types** | [01_VariablesAndDataTypes.java](01_VariablesAndDataTypes.java) | Primitives vs wrappers, Integer caching, autoboxing pitfalls, BigDecimal for money, var keyword |
| 2 | **Operators** | [02_Operators.java](02_Operators.java) | Short-circuit evaluation, bitwise for flags, compound assignment quirks, instanceof pattern matching |
| 3 | **Conditions** | [03_Conditions.java](03_Conditions.java) | Switch expressions (Java 14+), pattern matching (Java 21+), guard clauses, exhaustiveness |
| 4 | **Loops** | [04_Loops.java](04_Loops.java) | Iterator vs for-each vs streams, ConcurrentModification, when to use which |
| 5 | **Methods** | [05_Methods.java](05_Methods.java) | Pass-by-value semantics, overloading resolution, varargs, functional interfaces, method references |
| 6 | **Classes & Objects** | [06_ClassesAndObjects.java](06_ClassesAndObjects.java) | Object lifecycle, equals/hashCode contract, immutability, Records (Java 16+) |
| 7 | **Constructors** | [07_Constructors.java](07_Constructors.java) | Chaining (this/super), initialization order, copy constructors, Builder pattern |
| 8 | **Access Modifiers** | [08_AccessModifiers.java](08_AccessModifiers.java) | Package-private uses, protected gotchas, encapsulation patterns, module system |
| 9 | **Static** | [09_StaticKeyword.java](09_StaticKeyword.java) | Static vs instance, class loading, static blocks, singletons, why static hinders testing |
| 10 | **this Keyword** | [10_ThisKeyword.java](10_ThisKeyword.java) | Disambiguation, constructor chaining, fluent APIs, this in lambdas vs inner classes |

## How to Study

### 1. Read & Run
```bash
# Compile and run any file
javac 01_VariablesAndDataTypes.java
# Java is a compiled language that targets a virtual machine (the JVM), not your physical CPU directly.
VariablesAndDataTypes.java  →  javac  →  VariablesAndDataTypes.class  →  java  →  runs
     (source code)            (compile)      (bytecode)                  (run)

# JVM = Java Virtual Machine
# It's a program that runs on your computer and executes Java bytecode. Think of it as a translator between your .class files and your actual hardware.
# Why "Virtual" Machine?
# It simulates a real CPU in software. Your .class file contains instructions for this imaginary CPU (the JVM), not for your actual Intel/ARM chip. The JVM then translates those to real CPU instructions at runtime.
# The JDK you installed (/opt/homebrew/Cellar/openjdk@21/...) includes the JVM — when you run java VariablesAndDataTypes, you're launching the JVM.
java VariablesAndDataTypes
```

### 2. Experiment
- Uncomment the "GOTCHA" examples to see errors
- Modify values and observe behavior
- Try the "BAD" patterns to understand why they're bad

### 3. Interview Prep
Each file contains common interview questions and edge cases marked with:
- `GOTCHA:` - Surprising behavior
- `CRITICAL:` - Must-know concepts
- `PRO TIP:` - Professional best practices
- `INTERVIEW:` - Commonly asked in interviews

## Key Takeaways

### Memory Model
- **Stack**: Primitives, references (fast, per-thread)
- **Heap**: Objects (GC managed, shared)
- **Metaspace**: Static fields, class metadata

### Equality
- `==` compares references (or primitive values)
- `.equals()` compares content (override for your classes!)
- Always override `hashCode()` when you override `equals()`

### Immutability
- Make fields `final`
- No setters
- Return defensive copies
- Use Records (Java 16+) for data classes

### Modern Java
- `var` for local type inference (Java 10+)
- Switch expressions (Java 14+)
- Pattern matching (Java 16+ for instanceof, 21+ for switch)
- Records (Java 16+)
- Sealed classes (Java 17+)

## Next Steps

After mastering these fundamentals, explore:
1. **Inheritance & Polymorphism** - extends, abstract, interfaces
2. **Exception Handling** - try-catch-finally, checked vs unchecked
3. **Generics** - Type parameters, bounds, wildcards
4. **Collections Framework** - List, Set, Map implementations
5. **Concurrency** - Threads, ExecutorService, CompletableFuture
6. **Streams API** - Functional data processing
7. **Modules** - Java Platform Module System (JPMS)
