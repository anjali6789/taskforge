import java.util.List;

/**
 * OPERATORS - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Operator precedence (avoid relying on it - use parentheses!)
 * - Short-circuit evaluation and its practical uses
 * - Bitwise operators for flags and optimizations
 * - Compound assignment quirks
 */
public class Operators {

    public static void main(String[] args) {
        
        // ═══════════════════════════════════════════════════════════════
        // ARITHMETIC OPERATORS
        // ═══════════════════════════════════════════════════════════════
        
        int a = 10, b = 3;
        System.out.println("10 / 3 = " + (a / b));    // 3 (integer division!)
        System.out.println("10.0 / 3 = " + (10.0 / b)); // 3.333... (at least one double)
        System.out.println("10 % 3 = " + (a % b));    // 1 (modulo/remainder)
        
        // GOTCHA: Modulo with negative numbers
        System.out.println("-10 % 3 = " + (-10 % 3)); // -1 (sign follows dividend!)
        System.out.println("10 % -3 = " + (10 % -3)); // 1
        
        // Use Math.floorMod for always-positive result
        System.out.println("Math.floorMod(-10, 3) = " + Math.floorMod(-10, 3)); // 2

        // Increment/Decrement - understand pre vs post
        int x = 5;
        System.out.println("x++ = " + x++); // 5 (returns, THEN increments)
        System.out.println("x is now: " + x); // 6
        System.out.println("++x = " + (++x)); // 7 (increments, THEN returns)
        int xy = 10;
        xy += xy -= 5;
        System.out.println(xy); // 15 (evaluates right to left: xy -= 5 -> 5, then xy += 5 -> 10)
        // AVOID in complex expressions - confusing and error-prone
        // int confusing = x++ + ++x; // Don't do this!

        // ═══════════════════════════════════════════════════════════════
        // COMPARISON OPERATORS - == vs equals() is CRITICAL
        // ═══════════════════════════════════════════════════════════════
        
        // Primitives: == compares values
        int p1 = 100, p2 = 100;
        System.out.println("primitives: 100 == 100: " + (p1 == p2)); // true
        
        // Objects: == compares REFERENCES (memory addresses)
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("new String == new String: " + (s1 == s2)); // false!
        System.out.println("equals(): " + s1.equals(s2)); // true
        
        // PRO TIP: Always use Objects.equals() to avoid NPE
        String nullable = null;
        // nullable.equals(s1); // NPE!
        System.out.println("Objects.equals(null, s1): " + 
            java.util.Objects.equals(nullable, s1)); // false, no NPE

        // ═══════════════════════════════════════════════════════════════
        // LOGICAL OPERATORS - Short-circuit evaluation
        // ═══════════════════════════════════════════════════════════════
        
        // && and || are SHORT-CIRCUIT (stop early if result is determined)
        // & and | are NOT short-circuit (always evaluate both sides)
        
        String str = null;
        
        // SAFE: Short-circuit prevents NPE
        if (str != null && str.length() > 0) {
            System.out.println("Has content");
        }
        //this is a deadcode example - the compiler will warn that str != null is always false, so the code inside the if block can never execute.
        
        // WOULD CRASH: & evaluates both sides
        // if (str != null & str.length() > 0) { } // NPE!
        
        // Practical use: Expensive operation avoidance
        // if (cheapCheck() && expensiveCheck()) { }
        // expensiveCheck() only called if cheapCheck() is true
        
        // Practical use: Null-safe method chaining
        // if (user != null && user.getAddress() != null && user.getAddress().getCity() != null)
        
        // BETTER (Java 8+): Optional
        // Optional.ofNullable(user)      // Wrap user (might be null)
            // .map(User::getAddress)     // If user exists, get address
            // .map(Address::getCity)     // If address exists, get city
            // .orElse("Unknown");        // If any step was null, return "Unknown"

        // ═══════════════════════════════════════════════════════════════
        // BITWISE OPERATORS - Flags, masks, and optimizations
        // ═══════════════════════════════════════════════════════════════
        
        // Common use case: Feature flags / permissions
        final int READ    = 0b0001;  // 1
        final int WRITE   = 0b0010;  // 2
        final int EXECUTE = 0b0100;  // 4
        final int DELETE  = 0b1000;  // 8
        
        int userPermissions = READ | WRITE; // 0b0011 = 3
        
        // Check if user has permission
        boolean canRead = (userPermissions & READ) != 0;      // true
        boolean canDelete = (userPermissions & DELETE) != 0;  // false
        System.out.println("Can read: " + canRead + ", Can delete: " + canDelete);
        
        // Add permission
        userPermissions |= EXECUTE; // Now 0b0111 = 7
        
        // Remove permission
        userPermissions &= ~WRITE; // Now 0b0101 = 5
        
        // Toggle permission
        userPermissions ^= READ; // If had, remove; if not, add
        
        // Bit shifting
        int num = 8;
        System.out.println("8 << 2 = " + (num << 2));  // 32 (multiply by 4)
        System.out.println("8 >> 2 = " + (num >> 2));  // 2 (divide by 4)
        
        // >>> is unsigned right shift (fills with 0, not sign bit)
        int negative = -8;
        System.out.println("-8 >> 2 = " + (negative >> 2));   // -2 (keeps negative)
        System.out.println("-8 >>> 2 = " + (negative >>> 2)); // Large positive number

        // ═══════════════════════════════════════════════════════════════
        // COMPOUND ASSIGNMENT QUIRKS
        // ═══════════════════════════════════════════════════════════════
        
        // Compound assignment includes implicit cast!
        byte byt = 10;
        // byt = byt + 1; // Compile error! (byt + 1) is int
        byt += 1;         // OK! Implicit cast (equivalent to byt = (byte)(byt + 1))
        
        // Can lead to unexpected truncation
        byte byteVal = 127;
        byteVal += 1;     // Overflows silently to -128!
        System.out.println("127 + 1 as byte = " + byteVal); // -128

        // ═══════════════════════════════════════════════════════════════
        // TERNARY OPERATOR - Use wisely
        // ═══════════════════════════════════════════════════════════════
        
        int age = 20;
        String status = age >= 18 ? "adult" : "minor"; // Simple, readable
        
        // AVOID: Nested ternary - hard to read
        // String bad = a > b ? "a" : b > c ? "b" : "c"; // Don't!
        
        // BETTER: Use if-else or switch for complex conditions

        // ═══════════════════════════════════════════════════════════════
        // INSTANCEOF with Pattern Matching (Java 16+)
        // ═══════════════════════════════════════════════════════════════
        
        Object obj = "Hello World";
        Object obj2 = "Hello World";
        // OLD way
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println(s.toUpperCase());
        }
        
        // NEW way (Java 16+) - pattern matching
        if (obj2 instanceof String s) {
            System.out.println(s.toUpperCase()); // s already cast!
        }
        
        // Works with negation too
        if (!(obj instanceof String s)) {
            return; // Exit method immediately if obj is NOT a String
        }
        
        // Practical use: Type checking in collections
        // List<Object> clutter = List.of("text", 42, 3.14, true, "more text");
        // When you have a "clutter" of mixed types, instanceof helps you sort them out:
        // for (Object item : clutter) {
        //     if (item instanceof String s)       → handle strings
        //     else if (item instanceof Integer n) → handle integers
        //     else if (item instanceof Double d)  → handle doubles
        //     else if (item instanceof Boolean b) → handle booleans
        // }
    }
}
