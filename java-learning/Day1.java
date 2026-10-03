/**
 * Day 1: How Java works and operators.
 *
 * Compile: javac Day1.java
 * Run:     java Day1
 */
public class Day1 {

    public static void main(String[] args) {
        explainExecution();
        demonstrateReferences();
        demonstrateOperators();
        challenge();
    }

    private static void explainExecution() {
        System.out.println("=== How Java works ===");
        System.out.println("Day1.java --javac--> Day1.class --java--> JVM");
        System.out.println("JDK provides development tools, including javac and java.");
        System.out.println("The JVM loads, verifies, and executes bytecode.");
        System.out.println("The JIT compiler can optimize frequently executed code.");
        System.out.println();
    }

    private static void demonstrateReferences() {
        System.out.println("=== References and memory ===");
        Task first = new Task("Learn Java");
        Task second = first;

        System.out.println("first == second: " + (first == second));
        second.title = "Learn Java operators";
        System.out.println("first.title after changing second: " + first.title);

        first = null;
        second = null;
        System.out.println("The Task object is now eligible for garbage collection.");
        System.out.println("Eligible does not mean collected immediately.");
        System.out.println();
    }

    private static void demonstrateOperators() {
        System.out.println("=== Operators ===");

        int a = 10;
        int b = 3;
        System.out.println("10 / 3 = " + (a / b));
        System.out.println("10.0 / 3 = " + (10.0 / b));
        System.out.println("10 % 3 = " + (a % b));

        String first = new String("task");
        String second = new String("task");
        System.out.println("first == second: " + (first == second));
        System.out.println("first.equals(second): " + first.equals(second));

        String title = null;
        System.out.println("Safe null check: " + (title != null && !title.isBlank()));

        final int READ = 0b0001;
        final int WRITE = 0b0010;
        int permissions = READ | WRITE;
        System.out.println("Can read: " + ((permissions & READ) != 0));
        System.out.println("Can write: " + ((permissions & WRITE) != 0));

        byte value = 127;
        value += 1;
        System.out.println("byte 127 += 1: " + value);

        Object valueObject = "Java";
        if (valueObject instanceof String text) {
            System.out.println("Pattern matching: " + text.toUpperCase());
        }
        System.out.println();
    }

    private static void challenge() {
        System.out.println("=== Challenge ===");
        System.out.println("5 / 2 = " + (5 / 2));
        System.out.println("(double) 5 / 2 = " + ((double) 5 / 2));
    }

    private static final class Task {
        private String title;

        private Task(String title) {
            this.title = title;
        }
    }
}
