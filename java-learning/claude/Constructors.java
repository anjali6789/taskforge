public class Constructors {

    static class Task {
        String title;
        String status;
        int priority;

        // static field + block — runs ONCE when class is first loaded
        static int totalCreated = 0;
        static {
            System.out.println("Task class loaded — static block runs once");
            totalCreated = 0;
        }

        // instance block — runs before EVERY constructor, every time you do new Task(...)
        {
            totalCreated++;
            System.out.println("Instance block — task #" + totalCreated);
        }

        // constructor chaining — each adds a default for missing parameters
        // calling new Task("Fix bug") → goes to Task(title, "TODO") → goes to Task(title, "TODO", 1)
        // the 3rd constructor always gets 3 real values — either from caller or from your defaults
        // status and priority are NEVER null — you control the defaults here
        Task(String title) {
            this(title, "TODO");       // fill in default status
        }

        Task(String title, String status) {
            this(title, status, 1);    // fill in default priority
        }

        // all roads lead here — actual assignment done once, no repetition
        Task(String title, String status, int priority) {
            this.title = title;
            this.status = status;
            this.priority = priority;
        }

        // title + priority only — skips status, fills in default
        // Java picks this constructor when you pass (String, int)
        Task(String title, int priority) {
            this(title, "TODO", priority);
        }

        // static factory methods — name tells you what you're creating
        // A factory method is a static method that creates and returns an object — an alternative to using new directly.
        // cleaner than: new Task("Fix bug", "TODO", 5) — what does 5 mean?
        // better:        Task.createBug("Fix bug")     — obvious
        static Task createBug(String title) {
            return new Task(title, "TODO", 5);
        }

        static Task createFeature(String title) {
            return new Task(title, "TODO", 3);
        }

        // ┌─────────────────────────────────────────────────────────────────┐
        // │  CONSTRUCTOR vs FACTORY METHOD                                  │
        // ├──────────────────────┬──────────────────────────────────────────┤
        // │  Constructor         │  Factory Method                          │
        // ├──────────────────────┼──────────────────────────────────────────┤
        // │  new Task("x", 5)    │  Task.createBug("x")                     │
        // │  No name (just new)  │  Has descriptive name                    │
        // │  Always creates new  │  Can cache/reuse objects                 │
        // │  Returns exact type  │  Can return subtypes                     │
        // │  Signature collision │  No collision (different names)          │
        // └──────────────────────┴──────────────────────────────────────────┘
        //
        // EXAMPLES
        //
        // 1. Cache/reuse
        //    new Integer(100) == new Integer(100)       → false (new objects)
        //    Integer.valueOf(100) == Integer.valueOf(100) → true (cached!)
        //
        // 2. Return subtypes
        //    static Animal create(String type) {
        //        if (type.equals("dog")) return new Dog();
        //        if (type.equals("cat")) return new Cat();
        //        return new Animal();
        //    }
        //    Animal pet = Animal.create("dog"); // Actually returns Dog!
        //
        // 3. No signature collision
        //    Task(String title, int priority)  ← (String, int)
        //    Task(String status, int priority) ← (String, int) ERROR! Same!
        //    BUT
        //    static Task withPriority(String title, int p) { ... }
        //    static Task withStatus(String status, int p)  { ... }  // OK!
        //
        // Use factory when name adds clarity, encoding business rules,
        //                   or need flexibility constructors can't provide

        // copy constructor — creates a new object with same values
        // changing the copy does NOT affect the original
        Task(Task other) {
            this(other.title, other.status, other.priority);
        }

        @Override
        public String toString() {
            return "Task{title=" + title + ", status=" + status + ", priority=" + priority + "}";
        }
    }

    public static void main(String[] args) {

        // 1. Constructor chaining
        System.out.println("--- Constructor chaining ---");
        Task t1 = new Task("Fix bug");                    // title only → defaults fill the rest
        Task t2 = new Task("Write tests", "IN_PROGRESS"); // title + status → priority defaults to 1
        Task t3 = new Task("Deploy", "DONE", 2);          // all three — no defaults needed
        Task t4 = new Task("Add feature", 4);             // title + priority → status defaults to TODO
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);

        // 2. Static factory
        System.out.println("--- Static factory ---");
        Task bug = Task.createBug("Null pointer in login");
        Task feature = Task.createFeature("Add dark mode");
        System.out.println(bug);
        System.out.println(feature);

        // 3. Copy constructor — original stays unchanged
        System.out.println("--- Copy constructor ---");
        Task copy = new Task(t1);
        copy.title = "Fixed bug";
        System.out.println("Original: " + t1);   // still "Fix bug"
        System.out.println("Copy: " + copy);      // "Fixed bug"

        // 4. Total created
        System.out.println("--- Total tasks created: " + Task.totalCreated + " ---");
    }
}
