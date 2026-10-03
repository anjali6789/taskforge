import java.util.*;

/**
 * CONSTRUCTORS - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Constructor chaining (this() and super())
 * - Copy constructors vs clone()
 * - Static factory methods (alternative to constructors)
 * - Builder pattern for complex construction
 * - Initialization order (tricky interview questions!)
 */
public class Constructors {

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTOR BASICS AND CHAINING
    // ═══════════════════════════════════════════════════════════════════
    
    static class Employee {
        private final String name;
        private final String department;
        private final double salary;
        private final String email;
        
        // Primary constructor - all fields
        public Employee(String name, String department, double salary, String email) {
            // Validate in primary constructor
            this.name = Objects.requireNonNull(name, "name required");
            this.department = Objects.requireNonNull(department, "department required");
            if (salary < 0) throw new IllegalArgumentException("salary cannot be negative");
            this.salary = salary;
            this.email = email;  // Can be null
        }
        
        // Telescoping constructors - chain to primary using this()
        public Employee(String name, String department, double salary) {
            this(name, department, salary, null);  // MUST be first statement!
        }
        
        public Employee(String name, String department) {
            this(name, department, 50000.0);  // Default salary
        }
        
        public Employee(String name) {
            this(name, "General");  // Default department
        }
        
        // RULES for this():
        // 1. Must be FIRST statement
        // 2. Cannot use both this() and super() - mutual exclusion
        // 3. Cannot call recursively (this() calling itself)
    }

    // ═══════════════════════════════════════════════════════════════════
    // SUPER() - Calling parent constructor
    // ═══════════════════════════════════════════════════════════════════
    
    static class Animal {
        protected final String name;
        
        public Animal(String name) {
            this.name = Objects.requireNonNull(name);
            System.out.println("Animal constructor: " + name);
        }
        
        // No default constructor! Subclasses MUST call super(name)
    }
    
    static class Dog extends Animal {
        private final String breed;
        
        public Dog(String name, String breed) {
            super(name);  // MUST be first statement!
            this.breed = breed;
            System.out.println("Dog constructor: " + breed);
        }
        
        // This won't compile - Animal has no default constructor
        // public Dog() { this.breed = "Unknown"; } // Error!
    }

    // ═══════════════════════════════════════════════════════════════════
    // INITIALIZATION ORDER - Interview question favorite!
    // ═══════════════════════════════════════════════════════════════════
    
    static class Parent {
        // Order within Parent:
        // 1. Static fields and static blocks (once, on class load)
        // 2. Instance fields and instance blocks (per instance)
        // 3. Constructor
        
        static String staticField = initStatic("Parent static field");
        static { System.out.println("1. Parent static block"); }
        
        String instanceField = initInstance("2. Parent instance field");
        { System.out.println("3. Parent instance block"); }
        
        public Parent() {
            System.out.println("4. Parent constructor");
        }
        
        private static String initStatic(String msg) { System.out.println(msg); return msg; }
        private String initInstance(String msg) { System.out.println(msg); return msg; }
    }
    
    static class Child extends Parent {
        static String childStatic = initStaticChild("Child static field");
        static { System.out.println("5. Child static block"); }
        
        String childInstance = initInstanceChild("6. Child instance field");
        { System.out.println("7. Child instance block"); }
        
        public Child() {
            // super() implicitly called here first!
            System.out.println("8. Child constructor");
        }
        
        private static String initStaticChild(String msg) { System.out.println(msg); return msg; }
        private String initInstanceChild(String msg) { System.out.println(msg); return msg; }
    }
    
    /*
     * Running `new Child()` prints:
     * 
     * Parent static field       (static init happens once, top-down)
     * 1. Parent static block
     * Child static field
     * 5. Child static block
     * 2. Parent instance field  (now instance init for Parent)
     * 3. Parent instance block
     * 4. Parent constructor
     * 6. Child instance field   (now instance init for Child)
     * 7. Child instance block
     * 8. Child constructor
     */

    // ═══════════════════════════════════════════════════════════════════
    // COPY CONSTRUCTORS - Better than clone()
    // ═══════════════════════════════════════════════════════════════════
    
    static class User {
        private String name;
        private List<String> roles;
        private Map<String, String> preferences;
        
        public User(String name, List<String> roles, Map<String, String> preferences) {
            this.name = name;
            this.roles = new ArrayList<>(roles);  // Defensive copy
            this.preferences = new HashMap<>(preferences);
        }
        
        // Copy constructor - creates independent copy
        public User(User other) {
            this.name = other.name;  // String is immutable, safe to share
            this.roles = new ArrayList<>(other.roles);  // Deep copy
            this.preferences = new HashMap<>(other.preferences);  // Deep copy
        }
        
        // Why copy constructor over clone()?
        // 1. No CloneNotSupportedException
        // 2. No type casting needed
        // 3. Works with final fields
        // 4. Clear, explicit behavior
        // 5. Can handle inheritance properly
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC FACTORY METHODS - Often better than constructors
    // ═══════════════════════════════════════════════════════════════════
    
    static class Connection {
        private final String host;
        private final int port;
        private final boolean secure;
        
        // Private constructor - force use of factory methods
        private Connection(String host, int port, boolean secure) {
            this.host = host;
            this.port = port;
            this.secure = secure;
        }
        
        // Advantage 1: Descriptive names
        public static Connection http(String host) {
            return new Connection(host, 80, false);
        }
        
        public static Connection https(String host) {
            return new Connection(host, 443, true);
        }
        
        // Advantage 2: Can return cached instance (flyweight pattern)
        private static final Connection LOCALHOST = new Connection("localhost", 8080, false);
        
        public static Connection localhost() {
            return LOCALHOST;  // Same instance every time
        }
        
        // Advantage 3: Can return subtype
        public static Connection create(String protocol, String host) {
            // Could return different implementations based on protocol
            return switch (protocol.toLowerCase()) {
                case "http" -> http(host);
                case "https" -> https(host);
                default -> throw new IllegalArgumentException("Unknown protocol: " + protocol);
            };
        }
        
        // Advantage 4: Can return Optional
        public static Optional<Connection> tryCreate(String url) {
            try {
                // Parse and create...
                return Optional.of(http("parsed.host.com"));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        
        // Common naming conventions:
        // from()     - type conversion (Date.from(instant))
        // of()       - aggregation (List.of(1,2,3))
        // valueOf()  - similar to from() (Integer.valueOf(5))
        // getInstance(), create(), newInstance()
    }

    // ═══════════════════════════════════════════════════════════════════
    // BUILDER PATTERN - For complex object construction
    // ═══════════════════════════════════════════════════════════════════
    
    static class HttpRequest {
        private final String method;
        private final String url;
        private final Map<String, String> headers;
        private final String body;
        private final int timeout;
        private final boolean followRedirects;
        
        // Private constructor - only Builder can create
        private HttpRequest(Builder builder) {
            this.method = builder.method;
            this.url = builder.url;
            this.headers = Map.copyOf(builder.headers);  // Immutable copy
            this.body = builder.body;
            this.timeout = builder.timeout;
            this.followRedirects = builder.followRedirects;
        }
        
        // Getters...
        public String getMethod() { return method; }
        public String getUrl() { return url; }
        
        // Static factory to get builder
        public static Builder builder(String method, String url) {
            return new Builder(method, url);
        }
        
        // Builder class
        public static class Builder {
            // Required parameters
            private final String method;
            private final String url;
            
            // Optional parameters with defaults
            private Map<String, String> headers = new HashMap<>();
            private String body = "";
            private int timeout = 30000;
            private boolean followRedirects = true;
            
            private Builder(String method, String url) {
                this.method = Objects.requireNonNull(method);
                this.url = Objects.requireNonNull(url);
            }
            
            // Fluent setters return this
            public Builder header(String key, String value) {
                this.headers.put(key, value);
                return this;
            }
            
            public Builder body(String body) {
                this.body = body;
                return this;
            }
            
            public Builder timeout(int millis) {
                if (millis < 0) throw new IllegalArgumentException("timeout cannot be negative");
                this.timeout = millis;
                return this;
            }
            
            public Builder followRedirects(boolean follow) {
                this.followRedirects = follow;
                return this;
            }
            
            // Build method creates the immutable object
            public HttpRequest build() {
                // Can add validation here
                return new HttpRequest(this);
            }
        }
    }
    
    // Usage:
    // HttpRequest request = HttpRequest.builder("POST", "https://api.example.com")
    //     .header("Content-Type", "application/json")
    //     .body("{\"key\": \"value\"}")
    //     .timeout(5000)
    //     .build();

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTOR BEST PRACTICES
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * 1. Don't do heavy work in constructors
     *    - No IO, no network calls
     *    - Makes testing difficult
     *    - Use lazy initialization or factories
     * 
     * 2. Don't call overridable methods from constructors
     *    - Subclass constructor hasn't run yet!
     *    - Can cause NullPointerException or wrong behavior
     * 
     * 3. Make fields final when possible
     *    - Ensures initialization
     *    - Thread-safe publication
     *    - Compiler catches uninitialized fields
     * 
     * 4. Validate all inputs
     *    - Fail fast with clear error messages
     *    - Use Objects.requireNonNull()
     * 
     * 5. Consider making defensive copies
     *    - For mutable parameters
     *    - Prevents external modification
     */
    
    // BAD: Calling overridable method
    static class Bad {
        public Bad() {
            init(); // Dangerous!
        }
        protected void init() {
            System.out.println("Bad.init()");
        }
    }
    
    static class ChildBad extends Bad {
        private String name;
        
        public ChildBad(String name) {
            super();  // Calls init() before name is set!
            this.name = name;
        }
        
        @Override
        protected void init() {
            System.out.println("Name is: " + name);  // null! NPE risk!
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        System.out.println("=== Constructor chaining ===");
        Employee e1 = new Employee("John");
        Employee e2 = new Employee("Jane", "Engineering", 75000);
        
        System.out.println("\n=== Initialization order ===");
        Child child = new Child();
        
        System.out.println("\n=== Copy constructor ===");
        User original = new User("Alice", Arrays.asList("admin", "user"), 
                                 Map.of("theme", "dark"));
        User copy = new User(original);
        System.out.println("Original == Copy: " + (original == copy)); // false
        
        System.out.println("\n=== Static factory ===");
        Connection conn1 = Connection.https("api.example.com");
        Connection conn2 = Connection.localhost();
        Connection conn3 = Connection.localhost();
        System.out.println("localhost same instance: " + (conn2 == conn3)); // true
        
        System.out.println("\n=== Builder pattern ===");
        HttpRequest request = HttpRequest.builder("GET", "https://api.example.com")
            .header("Authorization", "Bearer token")
            .timeout(5000)
            .followRedirects(false)
            .build();
        System.out.println("Request: " + request.getMethod() + " " + request.getUrl());
    }
}
