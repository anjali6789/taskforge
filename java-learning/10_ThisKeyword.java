import java.util.*;

/**
 * THIS KEYWORD - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - What 'this' actually is (reference to current instance)
 * - All use cases: disambiguation, constructor chaining, self-reference
 * - Method chaining with 'this' (fluent APIs)
 * - 'this' in inner classes vs lambdas
 * - Common patterns and interview questions
 */
public class ThisKeyword {

    // ═══════════════════════════════════════════════════════════════════
    // WHAT IS 'this'?
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * 'this' is a reference to the CURRENT OBJECT instance.
     * 
     * It's like saying "myself" in English.
     * 
     * IMPORTANT:
     * - 'this' only exists in INSTANCE context
     * - Cannot use 'this' in static methods (no instance!)
     * - In lambdas, 'this' refers to enclosing class, not lambda
     */

    // ═══════════════════════════════════════════════════════════════════
    // USE CASE 1: Disambiguate instance variables from parameters
    // ═══════════════════════════════════════════════════════════════════
    
    private String name;
    private int age;
    
    // Most common use - parameter shadows field
    public ThisKeyword(String name, int age) {
        // Without 'this', 'name' refers to parameter (closest scope)
        // 'this.name' explicitly means the field
        this.name = name;
        this.age = age;
    }
    
    public void setName(String name) {
        this.name = name;  // 'this.name' is field, 'name' is parameter
    }
    
    // If no shadowing, 'this' is optional but some style guides require it
    public void setAge(int newAge) {
        // Both lines work the same:
        age = newAge;       // OK - no shadowing
        this.age = newAge;  // Also OK - explicit
    }
    
    // PRO TIP: IDE can generate setters with 'this' automatically
    // In IntelliJ: Alt+Insert -> Setter

    // ═══════════════════════════════════════════════════════════════════
    // USE CASE 2: Constructor chaining with this()
    // ═══════════════════════════════════════════════════════════════════
    
    static class Person {
        private final String firstName;
        private final String lastName;
        private final int age;
        private final String email;
        
        // Full constructor
        public Person(String firstName, String lastName, int age, String email) {
            this.firstName = Objects.requireNonNull(firstName);
            this.lastName = Objects.requireNonNull(lastName);
            this.age = age;
            this.email = email;
        }
        
        // Chain to full constructor with defaults
        public Person(String firstName, String lastName, int age) {
            this(firstName, lastName, age, null);  // MUST be first statement!
        }
        
        public Person(String firstName, String lastName) {
            this(firstName, lastName, 0);  // Default age
        }
        
        public Person(String fullName) {
            // Can do processing before this()
            // BUT this() must still be the first statement
            this(
                fullName.split(" ")[0],  // These expressions are evaluated
                fullName.split(" ").length > 1 ? fullName.split(" ")[1] : "",
                0
            );
        }
        
        // GOTCHA: Can't call both this() and super()
        // They're mutually exclusive - both must be first statement!
    }

    // ═══════════════════════════════════════════════════════════════════
    // USE CASE 3: Return 'this' for method chaining (Fluent API)
    // ═══════════════════════════════════════════════════════════════════
    
    static class QueryBuilder {
        private String table;
        private List<String> columns = new ArrayList<>();
        private String whereClause;
        private String orderBy;
        private Integer limit;
        
        // Each setter returns 'this' to enable chaining
        public QueryBuilder from(String table) {
            this.table = table;
            return this;  // Return current instance
        }
        
        public QueryBuilder select(String... cols) {
            this.columns.addAll(Arrays.asList(cols));
            return this;
        }
        
        public QueryBuilder where(String clause) {
            this.whereClause = clause;
            return this;
        }
        
        public QueryBuilder orderBy(String column) {
            this.orderBy = column;
            return this;
        }
        
        public QueryBuilder limit(int n) {
            this.limit = n;
            return this;
        }
        
        public String build() {
            StringBuilder sql = new StringBuilder("SELECT ");
            sql.append(columns.isEmpty() ? "*" : String.join(", ", columns));
            sql.append(" FROM ").append(table);
            if (whereClause != null) sql.append(" WHERE ").append(whereClause);
            if (orderBy != null) sql.append(" ORDER BY ").append(orderBy);
            if (limit != null) sql.append(" LIMIT ").append(limit);
            return sql.toString();
        }
    }
    
    // Usage: Fluent, readable chain
    // String query = new QueryBuilder()
    //     .from("users")
    //     .select("id", "name", "email")
    //     .where("active = true")
    //     .orderBy("name")
    //     .limit(10)
    //     .build();

    // ═══════════════════════════════════════════════════════════════════
    // USE CASE 4: Pass current object as argument
    // ═══════════════════════════════════════════════════════════════════
    
    interface EventListener {
        void onEvent(Object source, String event);
    }
    
    static class Button {
        private String label;
        private EventListener listener;
        
        public Button(String label) {
            this.label = label;
        }
        
        public void setListener(EventListener listener) {
            this.listener = listener;
        }
        
        public void click() {
            if (listener != null) {
                // Pass 'this' as the source of the event
                listener.onEvent(this, "click");
            }
        }
        
        @Override
        public String toString() {
            return "Button[" + label + "]";
        }
    }
    
    // Observer pattern example
    static class Subject {
        private List<Observer> observers = new ArrayList<>();
        
        public void addObserver(Observer o) {
            observers.add(o);
        }
        
        public void notifyObservers(String message) {
            for (Observer o : observers) {
                o.update(this, message);  // Pass 'this' as subject
            }
        }
    }
    
    interface Observer {
        void update(Subject subject, String message);
    }

    // ═══════════════════════════════════════════════════════════════════
    // USE CASE 5: Returning 'this' vs creating new instance
    // ═══════════════════════════════════════════════════════════════════
    
    // Mutable class - methods modify and return this
    static class MutablePoint {
        private int x, y;
        
        public MutablePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public MutablePoint move(int dx, int dy) {
            this.x += dx;
            this.y += dy;
            return this;  // Same instance, modified
        }
    }
    
    // Immutable class - methods return new instance, not 'this'
    static class ImmutablePoint {
        private final int x, y;
        
        public ImmutablePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public ImmutablePoint move(int dx, int dy) {
            return new ImmutablePoint(x + dx, y + dy);  // NEW instance!
            // 'this' remains unchanged
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // 'this' IN INNER CLASSES VS LAMBDAS - CRITICAL DIFFERENCE!
    // ═══════════════════════════════════════════════════════════════════
    
    private String outerField = "Outer field";
    
    public void demonstrateThisInInnerClasses() {
        
        // Anonymous inner class - 'this' refers to the anonymous class!
        Runnable anonymousClass = new Runnable() {
            private String innerField = "Inner field";
            
            @Override
            public void run() {
                // 'this' is the anonymous Runnable instance
                System.out.println("Anonymous this: " + this.getClass().getName());
                System.out.println("Inner field: " + this.innerField);
                
                // To access outer 'this':
                System.out.println("Outer field: " + ThisKeyword.this.outerField);
            }
        };
        
        // Lambda - 'this' refers to ENCLOSING class, not the lambda!
        Runnable lambda = () -> {
            // 'this' is the ThisKeyword instance (enclosing class)
            System.out.println("Lambda this: " + this.getClass().getName());
            System.out.println("Outer field: " + this.outerField);
            
            // Lambdas don't have their own 'this'!
        };
        
        System.out.println("\n=== Anonymous class 'this' ===");
        anonymousClass.run();
        
        System.out.println("\n=== Lambda 'this' ===");
        lambda.run();
    }
    
    // Qualified 'this' - accessing outer class from inner class
    class Inner {
        private String field = "Inner";
        
        void method() {
            System.out.println(field);              // Inner.field
            System.out.println(this.field);         // Inner.field
            System.out.println(ThisKeyword.this.outerField);  // Outer's field
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // INTERVIEW QUESTIONS - Common 'this' gotchas
    // ═══════════════════════════════════════════════════════════════════
    
    // Q1: Can you use 'this' in a static method?
    // A: NO! Static methods don't have an instance.
    
    public static void staticMethod() {
        // System.out.println(this);  // COMPILE ERROR!
    }
    
    // Q2: What does 'this' refer to in a constructor?
    // A: The object being constructed (even before construction completes!)
    
    // Q3: Can you assign to 'this'?
    // A: NO! 'this' is final/read-only
    
    public void cannotReassignThis() {
        // this = new ThisKeyword("New");  // COMPILE ERROR!
    }
    
    // Q4: When is 'this' null?
    // A: NEVER in normal Java code. 'this' is always a valid reference.
    //    (In some JNI/native scenarios, you might see null, but that's rare)
    
    // Q5: What happens if you call a method using 'this' in constructor?
    // A: It works, but be careful - object may be partially initialized!
    
    static class Dangerous {
        private String data;
        
        public Dangerous() {
            processThis(this);  // Passing incomplete object!
            this.data = "initialized";
        }
        
        private static void processThis(Dangerous obj) {
            System.out.println("Data: " + obj.data);  // null! Not yet set!
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // BEST PRACTICES
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * 1. Always use 'this' when parameter shadows field
     *    - Makes code clear and prevents bugs
     * 
     * 2. Consider using 'this' consistently for all field access
     *    - Some teams prefer this for consistency
     *    - IDE can auto-format
     * 
     * 3. Use 'this()' for constructor chaining
     *    - DRY - Don't Repeat Yourself
     *    - Centralize validation in one constructor
     * 
     * 4. Return 'this' for fluent APIs
     *    - Makes code readable
     *    - Builder pattern
     * 
     * 5. Be careful with 'this' in lambdas
     *    - Know that it refers to enclosing class
     *    - Use OuterClass.this for clarity in nested classes
     * 
     * 6. Don't pass 'this' in constructors
     *    - Object may be incomplete
     *    - Can cause "this escape" issues
     */

    // ═══════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        System.out.println("=== Disambiguation ===");
        ThisKeyword tk = new ThisKeyword("John", 30);
        System.out.println("Name: " + tk.name + ", Age: " + tk.age);
        
        System.out.println("\n=== Constructor chaining ===");
        Person p1 = new Person("John", "Doe", 30, "john@example.com");
        Person p2 = new Person("Jane", "Doe", 25);
        Person p3 = new Person("Bob Smith");
        
        System.out.println("\n=== Method chaining (Fluent API) ===");
        String query = new QueryBuilder()
            .from("users")
            .select("id", "name", "email")
            .where("active = true")
            .orderBy("name")
            .limit(10)
            .build();
        System.out.println("Query: " + query);
        
        System.out.println("\n=== Pass 'this' as argument ===");
        Button btn = new Button("Submit");
        btn.setListener((source, event) -> 
            System.out.println("Received " + event + " from " + source));
        btn.click();
        
        System.out.println("\n=== Mutable vs Immutable ===");
        MutablePoint mp = new MutablePoint(0, 0);
        MutablePoint mp2 = mp.move(5, 5);
        System.out.println("Same instance? " + (mp == mp2));  // true
        
        ImmutablePoint ip = new ImmutablePoint(0, 0);
        ImmutablePoint ip2 = ip.move(5, 5);
        System.out.println("Same instance? " + (ip == ip2));  // false
        
        System.out.println("\n=== 'this' in inner classes vs lambdas ===");
        tk.demonstrateThisInInnerClasses();
        
        System.out.println("\n=== Dangerous constructor escape ===");
        new Dangerous();
    }
}
