import java.util.*;

/**
 * CLASSES AND OBJECTS - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Object lifecycle and memory model
 * - Object vs Class methods/fields
 * - Equality contracts (equals/hashCode)
 * - Immutability patterns
 * - Records (Java 16+)
 */
public class ClassesAndObjects {

    // ═══════════════════════════════════════════════════════════════════
    // OBJECT LIFECYCLE - Creation to Garbage Collection
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * 1. Class Loading: JVM loads .class file into memory
     * 2. Memory Allocation: JVM allocates memory on heap for new object
     * 3. Initialization: 
     *    a) Instance variables get default values
     *    b) Instance initializer blocks run (in order)
     *    c) Constructor runs
     * 4. Usage: Object is used by program
     * 5. Garbage Collection: When no references exist, GC can reclaim memory
     */
    
    static class LifecycleDemo {
        // Step 3a: Default values assigned first
        private int count;              // 0
        private String name;            // null
        private boolean active;         // false
        private double[] scores;        // null
        
        // Step 3b: Instance initializer block runs
        {
            System.out.println("Instance initializer block");
            count = 10;
        }
        
        // Step 3c: Constructor runs
        public LifecycleDemo(String name) {
            System.out.println("Constructor");
            this.name = name;
        }
        
        // Avoid finalize() - deprecated and unreliable!
        // Use try-with-resources or explicit cleanup instead
    }

    // ═══════════════════════════════════════════════════════════════════
    // OBJECT MEMORY MODEL - Stack vs Heap
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * STACK (per thread):
     * - Method call frames
     * - Local variables (primitives)
     * - References to objects (not the objects themselves!)
     * - Fast allocation/deallocation (LIFO)
     * 
     * HEAP (shared):
     * - All object instances
     * - Instance variables
     * - Managed by Garbage Collector
     * - Slower allocation
     */
    
    static void memoryDemo() {
        // 'x' is on stack (primitive)
        int x = 10;
        
        // 'person' reference is on stack
        // Person object is on heap
        Person person = new Person("John", 30);
        
        // 'list' reference is on stack
        // ArrayList object is on heap
        // Integer objects inside are also on heap
        List<Integer> list = new ArrayList<>();
        list.add(1);  // Integer object created on heap
    }

    // ═══════════════════════════════════════════════════════════════════
    // COMPLETE CLASS DESIGN EXAMPLE
    // ═══════════════════════════════════════════════════════════════════
    
    static class Person implements Comparable<Person> {
        // Constants - uppercase with underscores
        private static final int MIN_AGE = 0;
        private static final int MAX_AGE = 150;
        
        // Instance fields - private for encapsulation
        private final String name;  // final = cannot change after construction
        private int age;
        private List<String> hobbies;
        
        // Static field - shared across all instances
        private static int instanceCount = 0;
        
        // Constructor
        public Person(String name, int age) {
            // Validate inputs
            this.name = Objects.requireNonNull(name, "name cannot be null");
            setAge(age);  // Use setter for validation
            this.hobbies = new ArrayList<>();
            instanceCount++;
        }
        
        // Copy constructor - for creating defensive copies
        public Person(Person other) {
            this.name = other.name;
            this.age = other.age;
            this.hobbies = new ArrayList<>(other.hobbies);  // Defensive copy!
        }
        
        // Getters
        public String getName() { return name; }
        public int getAge() { return age; }
        
        // Defensive copy for mutable fields!
        public List<String> getHobbies() {
            return new ArrayList<>(hobbies);  // Return copy, not original
            // Or: return Collections.unmodifiableList(hobbies);
        }
        
        // Setters with validation
        public void setAge(int age) {
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new IllegalArgumentException("Age must be between " + MIN_AGE + " and " + MAX_AGE);
            }
            this.age = age;
        }
        
        public void addHobby(String hobby) {
            Objects.requireNonNull(hobby, "hobby cannot be null");
            hobbies.add(hobby);
        }
        
        // Static method - operates on class, not instance
        public static int getInstanceCount() {
            return instanceCount;
        }
        
        // ─────────────────────────────────────────────────────────────────
        // EQUALS CONTRACT - CRITICAL TO GET RIGHT
        // ─────────────────────────────────────────────────────────────────
        
        /*
         * equals() must be:
         * 1. Reflexive:  x.equals(x) == true
         * 2. Symmetric:  x.equals(y) == y.equals(x)
         * 3. Transitive: x.equals(y) && y.equals(z) => x.equals(z)
         * 4. Consistent: Multiple calls return same result (if objects unchanged)
         * 5. Null-safe:  x.equals(null) == false
         */
        
        @Override
        public boolean equals(Object obj) {
            // Same reference
            if (this == obj) return true;
            
            // Null or different class
            if (obj == null || getClass() != obj.getClass()) return false;
            
            // Cast and compare fields
            Person person = (Person) obj;
            return age == person.age && 
                   Objects.equals(name, person.name);
            // Note: hobbies intentionally excluded (maybe two people with same name/age are "equal"?)
        }
        
        // ─────────────────────────────────────────────────────────────────
        // HASHCODE CONTRACT - MUST OVERRIDE WITH EQUALS
        // ─────────────────────────────────────────────────────────────────
        
        /*
         * If x.equals(y), then x.hashCode() == y.hashCode()
         * (Reverse is NOT required - collisions are OK)
         * 
         * MUST include same fields used in equals()!
         */
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
        
        // ─────────────────────────────────────────────────────────────────
        // TOSTRING - For debugging
        // ─────────────────────────────────────────────────────────────────
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + ", hobbies=" + hobbies + "}";
        }
        
        // ─────────────────────────────────────────────────────────────────
        // COMPARABLE - For natural ordering
        // ─────────────────────────────────────────────────────────────────
        
        @Override
        public int compareTo(Person other) {
            // Compare by name first, then age
            int nameComparison = this.name.compareTo(other.name);
            if (nameComparison != 0) return nameComparison;
            return Integer.compare(this.age, other.age);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // IMMUTABLE CLASSES - Thread-safe, predictable
    // ═══════════════════════════════════════════════════════════════════
    
    // Classic immutable class
    static final class ImmutablePerson {
        private final String name;
        private final int age;
        private final List<String> hobbies;
        
        public ImmutablePerson(String name, int age, List<String> hobbies) {
            this.name = name;
            this.age = age;
            // Defensive copy on construction!
            this.hobbies = new ArrayList<>(hobbies);
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        
        // Defensive copy on access!
        public List<String> getHobbies() {
            return Collections.unmodifiableList(hobbies);
        }
        
        // "Setter" returns new instance
        public ImmutablePerson withAge(int newAge) {
            return new ImmutablePerson(this.name, newAge, this.hobbies);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // RECORDS (Java 16+) - Concise immutable data carriers
    // ═══════════════════════════════════════════════════════════════════
    
    // This one line gives you:
    // - private final fields
    // - constructor
    // - getters (name(), age())
    // - equals(), hashCode(), toString()
    record PersonRecord(String name, int age) {
        
        // Compact constructor for validation
        public PersonRecord {
            Objects.requireNonNull(name, "name cannot be null");
            if (age < 0) throw new IllegalArgumentException("age cannot be negative");
        }
        
        // Can add methods
        public String greeting() {
            return "Hello, I'm " + name;
        }
        
        // Can add static fields and methods
        public static PersonRecord unknown() {
            return new PersonRecord("Unknown", 0);
        }
    }
    
    // Records can implement interfaces
    record Point(int x, int y) implements Comparable<Point> {
        @Override
        public int compareTo(Point other) {
            int xComp = Integer.compare(this.x, other.x);
            return xComp != 0 ? xComp : Integer.compare(this.y, other.y);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // NESTED CLASSES - When to use which
    // ═══════════════════════════════════════════════════════════════════
    
    // 1. Static nested class - doesn't need outer instance
    static class StaticNested {
        void method() {
            // Cannot access non-static members of ClassesAndObjects
        }
    }
    
    // 2. Inner class (non-static) - has reference to outer instance
    class Inner {
        void method() {
            // Can access all members of ClassesAndObjects (including private)
        }
    }
    
    // 3. Local class - defined inside a method
    void methodWithLocalClass() {
        class Local {
            // Rarely used; limited scope
        }
    }
    
    // 4. Anonymous class - one-time use, no name
    void anonymousExample() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous class");
            }
        };
        // Better with lambda: Runnable r = () -> System.out.println("Lambda");
    }

    // ═══════════════════════════════════════════════════════════════════
    // MAIN - Demonstrate concepts
    // ═══════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        // Object creation and lifecycle
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Bob", 25);
        System.out.println("Instance count: " + Person.getInstanceCount());
        
        // equals and hashCode
        Person p3 = new Person("Alice", 30);
        System.out.println("p1.equals(p3): " + p1.equals(p3)); // true
        System.out.println("p1 == p3: " + (p1 == p3));         // false (different objects)
        
        // In HashSet/HashMap
        Set<Person> set = new HashSet<>();
        set.add(p1);
        set.add(p3);
        System.out.println("Set size: " + set.size()); // 1 (they're equal!)
        
        // Records
        PersonRecord rec1 = new PersonRecord("Charlie", 35);
        PersonRecord rec2 = new PersonRecord("Charlie", 35);
        System.out.println("Records equal: " + rec1.equals(rec2)); // true
        System.out.println("Record: " + rec1); // Nice toString() for free
        
        // Sorting with Comparable
        List<Person> people = Arrays.asList(p1, p2);
        Collections.sort(people);
        System.out.println("Sorted: " + people);
    }
}
