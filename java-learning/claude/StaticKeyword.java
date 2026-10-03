public class StaticKeyword {

    // ═══════════════════════════════════════════════════════════════
    // HOW STATIC WORKS INTERNALLY
    // ═══════════════════════════════════════════════════════════════
    //
    // Java has two memory areas:
    //
    //   HEAP     — where objects live (new Task(), new String(), etc.)
    //   METASPACE — where class definitions and static fields live
    //
    // When JVM loads the Task class:
    //   METASPACE
    //   ┌─────────────────────────────┐
    //   │  Task class definition      │
    //   │  static int totalCount = 0  │  ← stored here, ONE copy for all objects
    //   │  static block runs          │
    //   └─────────────────────────────┘
    //
    // When you create objects:
    //   HEAP                          METASPACE
    //   ┌──────────────────┐         ┌──────────────────────────────┐
    //   │  Task object t1  │         │  static int totalCount = 2   │ ← shared
    //   │  title="Fix bug" │         └──────────────────────────────┘
    //   └──────────────────┘
    //   ┌──────────────────┐
    //   │  Task object t2  │
    //   │  title="Write"   │
    //   └──────────────────┘
    //
    // Each object has its own instance fields on the heap.
    // Static fields live in Metaspace — one copy, shared by everyone, entire program lifetime.
    //
    // Static methods: JVM goes directly to Metaspace, no object/heap involved.
    //   → slightly faster
    //   → cannot use 'this' — no object on heap to point to
    //   → call on class name, not instance

    // ═══════════════════════════════════════════════════════════════
    // STATIC vs POLYMORPHISM — THE KEY GOTCHA
    // ═══════════════════════════════════════════════════════════════
    //
    // Instance method → JVM checks actual object type at RUNTIME (heap lookup)
    //   Animal a = new Dog();
    //   a.sound() → goes to heap → finds Dog object → calls Dog.sound() → "Woof" ✅
    //
    // Static method → JVM uses reference type at COMPILE TIME (no heap lookup)
    //   Animal a = new Dog();
    //   a.describe() → reference type is Animal → calls Animal.describe() — Dog never checked ❌
    //
    // This is called HIDING (not overriding) for static methods.
    //
    //   Dog d = new Dog();
    //   d.describe()   → reference type is Dog → calls Dog.describe() ✅
    //   Animal a = new Dog();
    //   a.describe()   → reference type is Animal → calls Animal.describe() ❌ (hiding, not polymorphism)
    //
    // RULE: always call static methods on the class name to avoid confusion
    //   Animal.describe()  ✅ clear
    //   a.describe()       ❌ misleading — looks like polymorphism but isn't
    //
    // Summary:
    //   instance method → actual object type decides (runtime)
    //   static method   → reference type decides (compile time)

    static class Task {
        // static field — ONE copy in Metaspace, shared across ALL Task objects
        static int totalCount = 0;
        static final String DEFAULT_STATUS = "TODO";  // constant — never changes

        // instance fields — each object has its OWN copy on the heap
        String title;
        String status;

        // static block — runs ONCE when class first loads into Metaspace
        static {
            System.out.println("Task class loaded");
        }

        Task(String title) {
            this.title = title;
            this.status = DEFAULT_STATUS;
            totalCount++;  // increments the shared Metaspace counter
        }

        // static utility method — no object needed, called on class name
        // Task.isValidTitle("x") — JVM goes to Metaspace, no heap involved
        static boolean isValidTitle(String title) {
            return title != null && !title.isBlank();
        }

        // instance method — needs an object, called on instance
        String getSummary() {
            return title + " [" + status + "]";
        }

        @Override
        public String toString() {
            return "Task{title=" + title + ", status=" + status + "}";
        }
    }

    // Singleton pattern — only ONE instance ever created
    // uses static to hold the single instance in Metaspace
    static class TaskConfig {
        private static TaskConfig instance;  // stored in Metaspace
        private String dbUrl;

        private TaskConfig() {  // private — nobody outside can do new TaskConfig()
            this.dbUrl = "jdbc:postgresql://localhost:5432/taskforge";
            System.out.println("TaskConfig initialised");
        }

        public static TaskConfig getInstance() {
            if (instance == null) {
                instance = new TaskConfig();  // created only once, stored in Metaspace
            }
            return instance;  // same object returned every time
        }

        public String getDbUrl() { return dbUrl; }
    }

    // Demonstrating static hiding vs instance polymorphism
    static class Animal {
        static void describe() { System.out.println("I am Animal"); }
        void sound() { System.out.println("..."); }
    }

    static class Dog extends Animal {
        static void describe() { System.out.println("I am Dog"); }  // HIDES, not overrides
        @Override
        void sound() { System.out.println("Woof"); }  // OVERRIDES — real polymorphism
    }

    public static void main(String[] args) {

        // 1. Static field shared across instances
        System.out.println("--- Static field ---");
        Task t1 = new Task("Fix bug");
        Task t2 = new Task("Write tests");
        Task t3 = new Task("Deploy");
        System.out.println("Total tasks: " + Task.totalCount);  // 3 — shared counter

        // 2. Static method — called on class, not object
        System.out.println("--- Static method ---");
        System.out.println(Task.isValidTitle("Fix bug"));  // true
        System.out.println(Task.isValidTitle(""));         // false
        System.out.println(Task.isValidTitle(null));       // false

        // 3. Instance method — needs object
        System.out.println("--- Instance method ---");
        System.out.println(t1.getSummary());

        // 4. Singleton — same instance every time
        System.out.println("--- Singleton ---");
        TaskConfig config1 = TaskConfig.getInstance();
        TaskConfig config2 = TaskConfig.getInstance();
        System.out.println("Same instance: " + (config1 == config2));  // true
        System.out.println("DB URL: " + config1.getDbUrl());

        // 5. Static hiding vs instance polymorphism
        System.out.println("--- Static hiding vs polymorphism ---");
        Animal a = new Dog();
        a.describe();   // "I am Animal" — reference type is Animal, hiding, NOT polymorphism
        a.sound();      // "Woof"        — actual object is Dog, real polymorphism ✅

        Dog d = new Dog();
        d.describe();   // "I am Dog"    — reference type is Dog
        d.sound();      // "Woof"
    }
}