import java.util.*;
import java.util.function.*;

/**
 * METHODS - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Method overloading resolution rules
 * - Pass-by-value semantics (Java is ALWAYS pass-by-value!)
 * - Varargs pitfalls
 * - Method references and functional interfaces
 * - Default and static interface methods
 */
public class Methods {

    // ═══════════════════════════════════════════════════════════════════
    // METHOD SIGNATURE AND OVERLOADING
    // ═══════════════════════════════════════════════════════════════════
    
    // Method signature = name + parameter types (NOT return type!)
    
    // These are valid overloads
    public void process(int x) { }
    public void process(double x) { }
    public void process(String x) { }
    public void process(int x, String y) { }
    
    // This is NOT valid - same signature, different return type
    // public int process(int x) { return x; } // COMPILE ERROR!
    
    // OVERLOADING RESOLUTION - JVM picks MOST SPECIFIC match
    public void print(Object o) { System.out.println("Object: " + o); }
    public void print(String s) { System.out.println("String: " + s); }
    public void print(Integer i) { System.out.println("Integer: " + i); }
    
    public void testOverloading() {
        print("Hello");     // Calls print(String) - most specific
        print(42);          // Calls print(Integer) - most specific
        print(42L);         // Calls print(Object) - Long not Integer
        print(null);        // AMBIGUOUS! String or Integer? Compile error!
        // Fix: print((String) null);
    }
    
    // GOTCHA: Autoboxing + overloading can surprise you
    public void count(int x) { System.out.println("primitive int"); }
    public void count(Integer x) { System.out.println("wrapper Integer"); }
    
    public void testAutoboxing() {
        int primitive = 5;
        Integer wrapper = 5;
        
        count(primitive);  // "primitive int" - no boxing needed
        count(wrapper);    // "wrapper Integer" - no unboxing needed
        count(5);          // "primitive int" - literals are primitives
        count(Integer.valueOf(5)); // "wrapper Integer"
    }

    // ═══════════════════════════════════════════════════════════════════
    // PASS-BY-VALUE - THE MOST MISUNDERSTOOD CONCEPT
    // ═══════════════════════════════════════════════════════════════════
    
    // Java is ALWAYS pass-by-value. Period.
    // But for objects, the VALUE passed is the REFERENCE (memory address).
    
    public void modifyPrimitive(int x) {
        x = 100; // Modifies local copy, not original
    }
    
    public void modifyReference(StringBuilder sb) {
        sb.append(" World"); // Modifies the object the reference points to
    }
    
    public void reassignReference(StringBuilder sb) {
        sb = new StringBuilder("New"); // Reassigns local reference copy
        // Original reference unchanged!
    }
    
    public void demonstratePassByValue() {
        int num = 5;
        modifyPrimitive(num);
        System.out.println(num); // Still 5!
        
        StringBuilder sb = new StringBuilder("Hello");
        modifyReference(sb);
        System.out.println(sb); // "Hello World" - object was modified!
        
        sb = new StringBuilder("Hello");
        reassignReference(sb);
        System.out.println(sb); // Still "Hello" - original reference unchanged!
    }
    
    // IMPLICATION: Return values matter for immutable objects
    public String uppercase(String s) {
        return s.toUpperCase(); // String is immutable, must return new value
    }

    // ═══════════════════════════════════════════════════════════════════
    // VARARGS (Variable Arguments)
    // ═══════════════════════════════════════════════════════════════════
    
    // Varargs must be the LAST parameter
    public void printAll(String prefix, String... messages) {
        for (String msg : messages) {
            System.out.println(prefix + msg);
        }
    }
    
    public void testVarargs() {
        printAll("-> ");                          // Empty varargs OK
        printAll("-> ", "Hello");                 // Single element
        printAll("-> ", "Hello", "World");        // Multiple elements
        printAll("-> ", new String[]{"A", "B"});  // Array also works
    }
    
    // GOTCHA: Varargs + overloading = confusion
    public void ambiguous(int... nums) { System.out.println("varargs"); }
    public void ambiguous(int x, int y) { System.out.println("two params"); }
    
    // ambiguous(1, 2); // Which one? JVM prefers specific over varargs
    // Result: "two params"
    
    // GOTCHA: Varargs with generics creates warnings
    @SafeVarargs // Suppresses warning when you know it's safe
    public final <T> void safeVarargs(T... elements) {
        // Using @SafeVarargs tells compiler this method doesn't do
        // anything dangerous with the varargs array (like storing it)
    }

    // ═══════════════════════════════════════════════════════════════════
    // FUNCTIONAL INTERFACES AND METHOD REFERENCES (Java 8+)
    // ═══════════════════════════════════════════════════════════════════
    
    // Functional interface = exactly ONE abstract method
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
        // Can have default/static methods
        default int addOne(int x) { return x + 1; }
    }
    
    public void demonstrateFunctionalInterfaces() {
        // Lambda expression
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        
        System.out.println(add.calculate(5, 3));      // 8
        System.out.println(multiply.calculate(5, 3)); // 15
        
        // Built-in functional interfaces (java.util.function)
        Predicate<String> isEmpty = String::isEmpty;  // Method reference
        Function<String, Integer> length = String::length;
        Consumer<String> printer = System.out::println;
        Supplier<String> supplier = () -> "Hello";
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;
        
        // Types of method references:
        // 1. Static method:  ClassName::staticMethod
        Function<String, Integer> parser = Integer::parseInt;
        
        // 2. Instance method of specific object: object::instanceMethod
        String prefix = "Hello ";
        Function<String, String> greeter = prefix::concat;
        
        // 3. Instance method of arbitrary object: ClassName::instanceMethod
        Function<String, String> upper = String::toUpperCase;
        
        // 4. Constructor reference: ClassName::new
        Supplier<ArrayList<String>> listFactory = ArrayList::new;
        Function<String, StringBuilder> sbFactory = StringBuilder::new;
    }

    // ═══════════════════════════════════════════════════════════════════
    // DEFAULT AND STATIC METHODS IN INTERFACES (Java 8+)
    // ═══════════════════════════════════════════════════════════════════
    
    interface Vehicle {
        void drive(); // Abstract - must implement
        
        // Default method - provides implementation, can be overridden
        default void honk() {
            System.out.println("Beep!");
        }
        
        // Static method - belongs to interface, not inherited
        static int getWheelCount() {
            return 4;
        }
        
        // Private method (Java 9+) - helper for default methods
        private void log(String msg) {
            System.out.println("[Vehicle] " + msg);
        }
    }
    
    // Multiple interfaces with same default method = must override!
    interface A { default void foo() { System.out.println("A"); } }
    interface B { default void foo() { System.out.println("B"); } }
    
    class C implements A, B {
        @Override
        public void foo() {
            A.super.foo(); // Call specific interface's implementation
            B.super.foo();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // BEST PRACTICES
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * 1. Method names should be verbs: getName(), calculateTotal(), isValid()
     * 
     * 2. Keep methods small - Single Responsibility Principle
     *    - Ideally under 20 lines
     *    - One level of abstraction
     * 
     * 3. Limit parameters to 3-4 max
     *    - Use objects/builders for more
     *    - Consider Parameter Object pattern
     * 
     * 4. Return early to avoid deep nesting
     *    if (!isValid()) return;
     *    // rest of logic...
     * 
     * 5. Use Optional for methods that might not return a value
     *    public Optional<User> findById(Long id) { }
     * 
     * 6. Document public methods with Javadoc
     *    - What it does
     *    - @param descriptions
     *    - @return description
     *    - @throws conditions
     * 
     * 7. Avoid side effects in methods that return values
     *    - Either mutate state OR return value, not both
     *    - Exception: Builder pattern
     */
    
    /**
     * Finds a user by their unique identifier.
     * 
     * @param id the unique identifier of the user, must not be null
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if id is null
     */
    public Optional<String> findUserById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        // Implementation...
        return Optional.of("User " + id);
    }
    
    public static void main(String[] args) {
        Methods demo = new Methods();
        demo.demonstratePassByValue();
        demo.testVarargs();
        demo.demonstrateFunctionalInterfaces();
    }
}
