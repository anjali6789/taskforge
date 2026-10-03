import java.util.*;

/**
 * ACCESS MODIFIERS - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - The four access levels and their scope
 * - Package-private (default) and its uses
 * - Protected inheritance quirks
 * - Module system (Java 9+) impact
 * - Encapsulation best practices
 */
public class AccessModifiers {

    // ═══════════════════════════════════════════════════════════════════
    // THE FOUR ACCESS LEVELS (Most to Least Restrictive)
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * ┌─────────────────────────────────────────────────────────────────────┐
     * │  Modifier      │ Class │ Package │ Subclass │ World (Other pkgs)   │
     * ├─────────────────────────────────────────────────────────────────────┤
     * │  private       │  ✓    │    ✗    │    ✗     │    ✗                 │
     * │  (default)     │  ✓    │    ✓    │    ✗*    │    ✗                 │
     * │  protected     │  ✓    │    ✓    │    ✓     │    ✗                 │
     * │  public        │  ✓    │    ✓    │    ✓     │    ✓                 │
     * └─────────────────────────────────────────────────────────────────────┘
     * 
     * *package-private IS accessible in subclass if subclass is in same package
     */
    
    // ═══════════════════════════════════════════════════════════════════
    // PRIVATE - Most restrictive
    // ═══════════════════════════════════════════════════════════════════
    
    private String privateField = "Only accessible within this class";
    
    private void privateMethod() {
        // Can only be called within this class
    }
    
    // GOTCHA: Private is per-class, not per-instance!
    static class PrivateDemo {
        private int secret;
        
        public boolean hasSameSecret(PrivateDemo other) {
            // Can access other's private field - same class!
            return this.secret == other.secret;
        }
    }
    
    // GOTCHA: Nested classes can access private members of outer class
    private int outerSecret = 42;
    
    class Inner {
        void accessOuter() {
            System.out.println(outerSecret); // Works! Nested class privilege
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // PACKAGE-PRIVATE (DEFAULT) - No modifier keyword
    // ═══════════════════════════════════════════════════════════════════
    
    String packagePrivateField = "Accessible within same package";
    
    void packagePrivateMethod() {
        // Accessible by any class in the same package
        // NOT accessible outside package, even by subclasses!
    }
    
    /*
     * WHEN TO USE PACKAGE-PRIVATE:
     * 
     * 1. Implementation details shared between classes in a package
     * 
     * 2. Testing - put tests in same package to access package-private methods
     *    (common pattern: src/main/java/com/app/Service.java
     *                     src/test/java/com/app/ServiceTest.java)
     * 
     * 3. Encapsulation within a module - hide from other packages
     * 
     * 4. When you want to prevent subclassing outside your package
     */
    
    // Package-private class - cannot be accessed outside package
    class PackagePrivateClass {
        // Often used for implementation helper classes
    }

    // ═══════════════════════════════════════════════════════════════════
    // PROTECTED - Package + Subclasses
    // ═══════════════════════════════════════════════════════════════════
    
    protected String protectedField = "Package + Subclasses anywhere";
    
    protected void protectedMethod() {
        // Accessible by:
        // 1. Any class in same package
        // 2. Subclasses in ANY package (through inheritance)
    }
    
    // CRITICAL GOTCHA: Protected access from subclass is limited!
    static class Parent {
        protected int value = 100;
        
        protected void doSomething() {
            System.out.println("Parent.doSomething()");
        }
    }
    
    // In another package:
    // class Child extends Parent {
    //     void test(Parent p, Child c) {
    //         System.out.println(this.value);   // OK - inherited
    //         System.out.println(c.value);      // OK - same class type
    //         System.out.println(p.value);      // ERROR! p might not be a Child!
    //     }
    // }
    // 
    // Why? Protected inheritance access only works through "this" or same type
    // You can't access protected members on arbitrary Parent references

    // ═══════════════════════════════════════════════════════════════════
    // PUBLIC - Accessible everywhere
    // ═══════════════════════════════════════════════════════════════════
    
    public String publicField = "Accessible from anywhere";
    
    public void publicMethod() {
        // Part of the public API - be careful changing these!
    }
    
    /*
     * PUBLIC API DESIGN CONSIDERATIONS:
     * 
     * 1. Once public, always public (breaking change to remove)
     * 2. Document the contract with Javadoc
     * 3. Use defensive copies for mutable objects
     * 4. Consider return types carefully (prefer interfaces)
     * 5. Validate all inputs
     */

    // ═══════════════════════════════════════════════════════════════════
    // CLASS-LEVEL ACCESS
    // ═══════════════════════════════════════════════════════════════════
    
    // Top-level classes can only be: public or package-private
    // public class PublicClass { }    // Accessible everywhere
    // class PackageClass { }          // Package-private
    
    // Nested classes can have any access level
    public static class PublicNested { }
    protected static class ProtectedNested { }
    static class PackageNested { }
    private static class PrivateNested { }

    // ═══════════════════════════════════════════════════════════════════
    // ACCESS MODIFIERS WITH INHERITANCE
    // ═══════════════════════════════════════════════════════════════════
    
    static class BaseClass {
        private void privateMethod() { }     // Not inherited
        void packageMethod() { }             // Inherited if same package
        protected void protectedMethod() { } // Always inherited
        public void publicMethod() { }       // Always inherited
    }
    
    static class Derived extends BaseClass {
        // Can override with SAME or LESS restrictive access
        
        // @Override void privateMethod() { }     // Can't override - not inherited!
        
        @Override
        protected void packageMethod() { }  // OK - protected >= package-private
        
        @Override
        public void protectedMethod() { }   // OK - public >= protected
        
        @Override
        public void publicMethod() { }      // OK - same level
        
        // @Override
        // private void publicMethod() { }  // ERROR! Cannot reduce visibility
    }

    // ═══════════════════════════════════════════════════════════════════
    // MODULE SYSTEM (Java 9+) - Additional layer
    // ═══════════════════════════════════════════════════════════════════
    
    /*
     * module-info.java adds another access layer:
     * 
     * module com.example.app {
     *     exports com.example.app.api;           // Public API
     *     exports com.example.app.spi to         // Limited export
     *         com.example.provider;
     *     opens com.example.app.model to         // Allow reflection
     *         com.fasterxml.jackson.databind;
     * }
     * 
     * Even public classes are not accessible unless their package is exported!
     * 
     * EFFECTIVE ACCESS:
     * - Public + Exported package = Truly public
     * - Public + Non-exported package = Module-internal (like package-private)
     */

    // ═══════════════════════════════════════════════════════════════════
    // ENCAPSULATION BEST PRACTICES
    // ═══════════════════════════════════════════════════════════════════
    
    static class WellEncapsulated {
        // Rule 1: Make everything as private as possible
        private String name;
        private List<String> items;
        
        // Rule 2: Expose through accessor methods (with validation)
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            // Rule 3: Validate in setters
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            this.name = name.trim();  // Can also normalize
        }
        
        // Rule 4: Return defensive copies of mutable objects
        public List<String> getItems() {
            return Collections.unmodifiableList(items);  // Read-only view
            // Or: return new ArrayList<>(items);        // Copy
        }
        
        // Rule 5: Accept copies of mutable parameters
        public void setItems(List<String> items) {
            this.items = new ArrayList<>(items);  // Defensive copy
        }
        
        // Rule 6: Prefer immutable classes when possible
        // See Records (Java 16+)
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // COMMON PATTERNS
    // ═══════════════════════════════════════════════════════════════════
    
    // Pattern: Public interface, package-private implementation
    interface PaymentProcessor {  // Public
        void process(double amount);
    }
    
    class StripeProcessor implements PaymentProcessor {  // Package-private
        @Override
        public void process(double amount) {
            // Implementation hidden from users
        }
    }
    
    // Pattern: Factory that returns interface type
    public class PaymentFactory {
        public static PaymentProcessor create(String type) {
            return switch (type) {
                case "stripe" -> new StripeProcessor();  // Returns package-private impl
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        }
    }
    
    // Pattern: Test access (same package, different source folder)
    /*
     * Production: src/main/java/com/app/Calculator.java
     *   class Calculator {
     *       int add(int a, int b) { return a + b; }  // Package-private
     *   }
     * 
     * Test: src/test/java/com/app/CalculatorTest.java
     *   class CalculatorTest {
     *       @Test void testAdd() {
     *           Calculator calc = new Calculator();
     *           assertEquals(5, calc.add(2, 3));  // Can access - same package!
     *       }
     *   }
     */

    // ═══════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        System.out.println("=== Access Modifiers Demo ===");
        
        // Private - only within same class
        AccessModifiers am = new AccessModifiers();
        System.out.println(am.privateField);  // OK - we're in the same class
        
        // Private instance access
        PrivateDemo pd1 = new PrivateDemo();
        PrivateDemo pd2 = new PrivateDemo();
        pd1.secret = 42;
        pd2.secret = 42;
        System.out.println("Same secret: " + pd1.hasSameSecret(pd2));
        
        // Encapsulation
        WellEncapsulated we = new WellEncapsulated();
        we.setName("  John Doe  ");  // Will be trimmed
        System.out.println("Name: " + we.getName());
        
        we.setItems(Arrays.asList("A", "B", "C"));
        List<String> items = we.getItems();
        // items.add("D");  // UnsupportedOperationException - it's unmodifiable!
        System.out.println("Items: " + items);
    }
}
