import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * STATIC KEYWORD - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Static vs instance context
 * - Class loading and static initialization
 * - Static blocks and their order
 * - When to use static (and when NOT to)
 * - Static and inheritance
 * - Memory implications
 */
public class StaticKeyword {

    // ═══════════════════════════════════════════════════════════════════
    // STATIC FIELDS - Belong to the class, not instances
    // ═══════════════════════════════════════════════════════════════════
    
    // Shared across ALL instances - one copy in memory
    private static int instanceCount = 0;
    
    // Constants should be static final
    public static final double PI = 3.14159265359;
    public static final String APP_NAME = "MyApp";
    
    // Thread-safe counter using Atomic
    private static final AtomicInteger threadSafeCount = new AtomicInteger(0);
    
    // Instance field for comparison
    private String name;
    
    public StaticKeyword(String name) {
        this.name = name;
        instanceCount++;  // Increments shared counter
    }
    
    // Static getter - no instance needed
    public static int getInstanceCount() {
        return instanceCount;
    }
    
    // MEMORY MODEL:
    // Static fields are stored in METASPACE (Java 8+), not heap
    // They exist from class loading until class unloading
    // Be careful with static collections - can cause memory leaks!
    
    private static final List<Object> potentialLeak = new ArrayList<>();
    
    public static void addToLeak(Object obj) {
        potentialLeak.add(obj);  // Objects never GC'd if we never remove!
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC METHODS - Operate without instance
    // ═══════════════════════════════════════════════════════════════════
    
    public static double celsiusToFahrenheit(double celsius) {
        // Cannot access 'this' - no instance!
        // Cannot access instance fields/methods
        return celsius * 9 / 5 + 32;
    }
    
    // GOTCHA: Cannot access instance members from static context!
    public static void staticMethod() {
        // System.out.println(name);  // ERROR! 'name' is instance field
        // instanceMethod();           // ERROR! Instance method
        System.out.println(instanceCount);  // OK - static field
        staticHelper();                      // OK - static method
    }
    
    private static void staticHelper() { }
    
    private void instanceMethod() {
        // Instance can access both static and instance
        System.out.println(name);          // OK - instance field
        System.out.println(instanceCount); // OK - static field
        staticMethod();                    // OK - static method
    }
    
    // WHEN TO USE STATIC METHODS:
    // 1. Utility methods that don't need state: Math.sqrt(), Collections.sort()
    // 2. Factory methods: Optional.of(), List.of()
    // 3. Operations on static data: getInstance() in singleton
    // 
    // WHEN NOT TO USE:
    // 1. If method could be overridden (use instance method)
    // 2. If method needs to access instance state
    // 3. If you need dependency injection (harder to test)

    // ═══════════════════════════════════════════════════════════════════
    // STATIC BLOCKS - Class initialization
    // ═══════════════════════════════════════════════════════════════════
    
    // Static fields and blocks execute in ORDER, once on class loading
    private static final Map<String, Integer> LOOKUP;
    private static final Properties CONFIG;
    
    static {
        System.out.println("Static block 1 - runs once on class load");
        
        // Initialize complex static data
        LOOKUP = new HashMap<>();
        LOOKUP.put("one", 1);
        LOOKUP.put("two", 2);
        LOOKUP.put("three", 3);
    }
    
    static {
        System.out.println("Static block 2 - multiple blocks allowed");
        
        // Load configuration (example with error handling)
        Properties props = new Properties();
        try {
            // props.load(StaticKeyword.class.getResourceAsStream("/config.properties"));
            props.setProperty("app.name", "MyApp");  // Demo value
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
        CONFIG = props;
    }
    
    // GOTCHA: Exceptions in static blocks wrap in ExceptionInInitializerError
    // and cause the class to be unusable!
    
    // ALTERNATIVE: Lazy initialization (often better)
    private static class LazyHolder {
        // Only initialized when LazyHolder class is accessed
        static final ExpensiveObject INSTANCE = new ExpensiveObject();
    }
    
    public static ExpensiveObject getExpensiveObject() {
        return LazyHolder.INSTANCE;  // Lazy, thread-safe initialization
    }
    
    static class ExpensiveObject {
        ExpensiveObject() {
            System.out.println("ExpensiveObject created");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC AND INHERITANCE - They don't mix well!
    // ═══════════════════════════════════════════════════════════════════
    
    static class Parent {
        static void staticMethod() {
            System.out.println("Parent.staticMethod()");
        }
        
        void instanceMethod() {
            System.out.println("Parent.instanceMethod()");
        }
    }
    
    static class Child extends Parent {
        // This HIDES parent's static method, NOT override!
        static void staticMethod() {
            System.out.println("Child.staticMethod()");
        }
        
        @Override  // This truly overrides
        void instanceMethod() {
            System.out.println("Child.instanceMethod()");
        }
    }
    
    public static void demonstrateStaticInheritance() {
        Parent p = new Child();
        
        // Instance method - polymorphism works
        p.instanceMethod();  // "Child.instanceMethod()" - virtual dispatch
        
        // Static method - resolved at COMPILE TIME, not runtime!
        p.staticMethod();  // "Parent.staticMethod()" - no polymorphism!
        
        // This is why you should NEVER call static methods on instances!
        // Always use: Parent.staticMethod() or Child.staticMethod()
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC NESTED CLASSES
    // ═══════════════════════════════════════════════════════════════════
    
    // Static nested class - no reference to outer instance
    public static class StaticNested {
        private String data;
        
        public StaticNested(String data) {
            this.data = data;
        }
        
        public void process() {
            // Cannot access outer instance members
            // System.out.println(name);  // ERROR!
            
            // CAN access outer static members
            System.out.println(instanceCount);
        }
    }
    
    // Non-static nested class (inner class) - has outer reference
    public class Inner {
        public void process() {
            // Can access outer instance
            System.out.println(name);  // OK!
            System.out.println(instanceCount);  // Also OK
        }
    }
    
    // MEMORY: Inner class holds reference to outer instance
    // Can prevent outer instance from being GC'd!
    // Prefer static nested classes unless you NEED outer reference

    // ═══════════════════════════════════════════════════════════════════
    // SINGLETON PATTERN - Static use case
    // ═══════════════════════════════════════════════════════════════════
    
    // Thread-safe singleton using static holder idiom (preferred)
    static class Singleton {
        // Private constructor
        private Singleton() { }
        
        // Holder class not loaded until getInstance() called
        private static class Holder {
            static final Singleton INSTANCE = new Singleton();
        }
        
        public static Singleton getInstance() {
            return Holder.INSTANCE;
        }
    }
    
    // Alternative: Enum singleton (bulletproof)
    enum SingletonEnum {
        INSTANCE;
        
        private String data;
        
        public void doSomething() {
            System.out.println("SingletonEnum doing something");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC IMPORTS - Use sparingly
    // ═══════════════════════════════════════════════════════════════════
    
    // import static java.lang.Math.PI;
    // import static java.lang.Math.sqrt;
    // import static java.util.stream.Collectors.*;
    
    // Now can use: sqrt(PI) instead of Math.sqrt(Math.PI)
    
    // GOOD: Common utilities like Collectors, Assertions
    // BAD: Can make code harder to read if overused

    // ═══════════════════════════════════════════════════════════════════
    // STATIC VS DEPENDENCY INJECTION
    // ═══════════════════════════════════════════════════════════════════
    
    // BAD: Hard to test, tightly coupled
    static class BadService {
        public void process(String data) {
            // Direct static call - hard to mock
            Database.save(data);
            Logger.log("Processed: " + data);
        }
    }
    
    static class Database {
        static void save(String data) { }
    }
    
    static class Logger {
        static void log(String msg) { }
    }
    
    // GOOD: Dependency injection - easy to test
    static class GoodService {
        private final DatabaseInterface db;
        private final LoggerInterface logger;
        
        // Dependencies injected
        public GoodService(DatabaseInterface db, LoggerInterface logger) {
            this.db = db;
            this.logger = logger;
        }
        
        public void process(String data) {
            db.save(data);
            logger.log("Processed: " + data);
        }
    }
    
    interface DatabaseInterface { void save(String data); }
    interface LoggerInterface { void log(String msg); }

    // ═══════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        System.out.println("\n=== Static fields ===");
        System.out.println("Instance count before: " + StaticKeyword.getInstanceCount());
        
        new StaticKeyword("First");
        new StaticKeyword("Second");
        new StaticKeyword("Third");
        
        System.out.println("Instance count after: " + StaticKeyword.getInstanceCount());
        System.out.println("PI = " + StaticKeyword.PI);
        
        System.out.println("\n=== Static methods ===");
        System.out.println("100°C = " + celsiusToFahrenheit(100) + "°F");
        
        System.out.println("\n=== Static vs instance inheritance ===");
        demonstrateStaticInheritance();
        
        System.out.println("\n=== Lazy initialization ===");
        System.out.println("Before getExpensiveObject()");
        getExpensiveObject();
        System.out.println("After getExpensiveObject()");
        getExpensiveObject();  // Won't print "created" again
        
        System.out.println("\n=== Singleton ===");
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println("Same instance: " + (s1 == s2));
        
        SingletonEnum.INSTANCE.doSomething();
    }
}
