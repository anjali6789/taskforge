/**
 * DAY 1 - How Java Works + Operators
 *
 * Goal:
 * Understand the Java execution model before learning operators:
 * JDK, JRE, JVM, bytecode, stack, heap, references, and garbage collection.
 * Then connect those ideas to Java operators.
 *
 * Run from this directory:
 *   javac Day1.java
 *   java Day1
 */
public class Day1 {

    public static void main(String[] args) {
        printTitle("PART 1: HOW JAVA WORKS");
        explainJavaExecution();

        printTitle("PART 2: STACK, HEAP, REFERENCES, AND GARBAGE COLLECTION");
        demonstrateMemoryModel();

        printTitle("PART 3: OPERATORS");
        arithmeticOperators();
        comparisonOperators();
        logicalOperators();
        bitwisePermissionFlags();
        compoundAssignment();
        ternaryOperator();
        instanceofPatternMatching();

        printTitle("DAY 1 CHALLENGE");
        day1Challenge();
    }

    private static void explainJavaExecution() {
        System.out.println("1. You write source code in Day1.java.");
        System.out.println("2. javac compiles source code into JVM bytecode: Day1.class.");
        System.out.println("3. java starts the JVM and loads Day1.class.");
        System.out.println("4. The JVM verifies and executes the bytecode.");
        System.out.println("5. The JIT compiler may compile frequently used code into native machine code.");
        System.out.println();
        System.out.println("JDK = development tools + runtime, including javac and the JVM.");
        System.out.println("JRE = historical runtime package: JVM + standard runtime libraries.");
        System.out.println("JVM = the virtual machine that executes Java bytecode.");
    }

    private static void demonstrateMemoryModel() {
        int taskCount = 3;
        Task task = new Task("Learn Java");
        Task sameTask = task;

        System.out.println("Local primitive taskCount = " + taskCount);
        System.out.println("task and sameTask refer to the same heap object: " + (task == sameTask));
        System.out.println("Task title from the heap object: " + task.title);

        sameTask.title = "Learn Java and Spring Boot";
        System.out.println("Changing through sameTask changes task: " + task.title);

        task = null;
        sameTask = null;
        System.out.println("Both references are now null; the Task object is eligible for garbage collection.");
        System.out.println("Eligible does not mean collected immediately. The JVM decides when GC runs.");
        System.out.println();
        System.out.println("Stack: method frames, local variables, and references used by each thread.");
        System.out.println("Heap: objects and arrays shared by application threads.");
        System.out.println("Metaspace: metadata about loaded classes.");
    }

    private static void arithmeticOperators() {
        System.out.println("-- Arithmetic operators --");
        int a = 10;
        int b = 3;

        System.out.println("10 + 3 = " + (a + b));
        System.out.println("10 - 3 = " + (a - b));
        System.out.println("10 * 3 = " + (a * b));
        System.out.println("10 / 3 = " + (a / b) + " because both operands are int");
        System.out.println("10.0 / 3 = " + (10.0 / b) + " because one operand is double");
        System.out.println("10 % 3 = " + (a % b));

        int x = 5;
        System.out.println("x++ returns " + x++ + "; x is now " + x);
        System.out.println("++x returns " + (++x) + "; x is now " + x);
    }

    private static void comparisonOperators() {
        System.out.println("-- Comparison operators --");
        int firstNumber = 100;
        int secondNumber = 100;
        System.out.println("Primitive == compares values: " + (firstNumber == secondNumber));

        String first = new String("task");
        String second = new String("task");
        System.out.println("Object == compares references: " + (first == second));
        System.out.println("Object equals() compares values: " + first.equals(second));
        System.out.println("Objects.equals() safely handles null values: "
                + java.util.Objects.equals(null, second));
    }

    private static void logicalOperators() {
        System.out.println("-- Logical operators and short-circuit evaluation --");
        String title = System.getenv("TASK_TITLE");

        if (title != null && !title.isBlank()) {
            System.out.println("The title has content.");
        } else {
            System.out.println("Safe: the second condition was not evaluated because title is null.");
        }

        System.out.println("&& and || short-circuit; & and | evaluate both sides.");
        System.out.println("This is useful for null checks, authorization, and avoiding expensive work.");
    }

    private static void bitwisePermissionFlags() {
        System.out.println("-- Bitwise operators for permission flags --");
        final int READ = 0b0001;
        final int WRITE = 0b0010;
        final int EXECUTE = 0b0100;

        int permissions = READ | WRITE;
        System.out.println("READ + WRITE permissions = " + permissions);
        System.out.println("Can read: " + ((permissions & READ) != 0));
        System.out.println("Can execute: " + ((permissions & EXECUTE) != 0));

        permissions |= EXECUTE;
        System.out.println("After adding EXECUTE: " + permissions);

        permissions &= ~WRITE;
        System.out.println("After removing WRITE: " + permissions);
    }

    private static void compoundAssignment() {
        System.out.println("-- Compound assignment --");
        byte value = 127;
        value += 1;
        System.out.println("byte 127 += 1 becomes " + value + " because the byte overflows.");
        System.out.println("Compound assignment performs an implicit narrowing conversion.");
    }

    private static void ternaryOperator() {
        System.out.println("-- Ternary operator --");
        int completedTasks = 5;
        int totalTasks = 8;
        double completionPercentage = (double) completedTasks / totalTasks * 100;
        String status = completedTasks == totalTasks ? "Complete" : "In progress";

        System.out.println("Completion percentage: " + completionPercentage);
        System.out.println("Status: " + status);
        System.out.println("Use ternary for simple choices; use if/else for complex logic.");
    }

    private static void instanceofPatternMatching() {
        System.out.println("-- instanceof pattern matching --");
        Object value = "Java";

        if (value instanceof String text) {
            System.out.println("The value is a String with uppercase form: " + text.toUpperCase());
        }
    }

    private static void day1Challenge() {
        System.out.println("5 / 2 = " + (5 / 2));
        System.out.println("(double) 5 / 2 = " + ((double) 5 / 2));
        System.out.println("Expected first answer: 2");
        System.out.println("Expected second answer: 2.5");
        System.out.println("Next: change the values and predict the output before running again.");
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("========================================");
    }

    private static final class Task {
        private String title;

        private Task(String title) {
            this.title = title;
        }
    }
}
