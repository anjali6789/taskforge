import java.util.Stack;

/**
 * VARIABLES AND DATA TYPES - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Primitives vs Wrapper classes (and why it matters)
 * - Memory implications
 * - Autoboxing/Unboxing pitfalls
 * - Type inference with 'var' (Java 10+)
 */
public class VariablesAndDataTypes {

    public static void main(String[] args) {
        // ═══════════════════════════════════════════════════════════════
        // PRIMITIVE TYPES - Stored on STACK (fast, no GC overhead)
        // ═══════════════════════════════════════════════════════════════
        
        // Integer types - know your ranges!
        byte b = 127;           // 8-bit:  -128 to 127
        short s = 32_767;       // 16-bit: -32,768 to 32,767 (underscore for readability)
        int i = 2_147_483_647;  // 32-bit: ~2.1 billion (DEFAULT for integer literals)
        long l = 9_223_372_036_854_775_807L; // 64-bit: needs 'L' suffix!
        
        // GOTCHA: Integer overflow wraps silently!
        int overflow = Integer.MAX_VALUE + 1;
        System.out.println("Integer overflow: " + overflow); // Prints -2147483648!
        
        // PRO TIP: Use Math.addExact() to catch overflow
        try {
            int safe = Math.addExact(Integer.MAX_VALUE, 1);
        } catch (ArithmeticException e) {
            // ArithmeticException is a particular/specific exception class — it only represents arithmetic errors (like division by zero or overflow detection).
            System.out.println("Caught overflow: " + e.getMessage());
        }

        // Floating-point types
        float f = 3.14f;        // 32-bit, ~7 decimal digits precision, needs 'f' suffix
        double d = 3.14159265;  // 64-bit, ~15 decimal digits (DEFAULT for decimal literals)
        
        // CRITICAL: Never use float/double for money!
        double badMoney = 0.1 + 0.2;
        System.out.println("0.1 + 0.2 = " + badMoney); // 0.30000000000000004 !!!
        
        // Use BigDecimal for financial calculations
        java.math.BigDecimal goodMoney = new java.math.BigDecimal("0.1")
            .add(new java.math.BigDecimal("0.2"));
        System.out.println("BigDecimal 0.1 + 0.2 = " + goodMoney); // 0.3

        // Character and Boolean
        char c = 'A';           // 16-bit Unicode (UTF-16)
        char unicode = '\u0041'; // Same as 'A'
        boolean bool = true;    // JVM implementation-specific size

        // ═══════════════════════════════════════════════════════════════
        // WRAPPER CLASSES - Stored on HEAP (objects, can be null)
        // ═══════════════════════════════════════════════════════════════
        // to use it with collections, generics or when you need nullability we use objects instead of primitives

        // had to do it manually before Java 5 (tedious and error-prone)
        // Manual boxing (primitive → object)
        // Integer wrapped = Integer.valueOf(100);  // Tedious!

        // // Manual unboxing (object → primitive)
        // int primitive = wrapped.intValue();      // Tedious!

        Integer wrappedInt = 100;  // Autoboxing: primitive -> object
        int primitiveInt = wrappedInt; // Unboxing: object -> primitive
        
        // GOTCHA #1: NullPointerException during unboxing!
        Integer nullInt = null;
        // int crash = nullInt; // NPE at runtime! Not compile error!
        
        // GOTCHA #2: Integer caching (-128 to 127)
        Integer a = 100;
        Integer b1 = 100;
        // Integer caching means small integers are reused, so a and b1 point to the same object (== true)
        Integer c1 = 200;
        Integer d1 = 200;
        
        System.out.println("100 == 100 (Integer): " + (a == b1));  // true (cached!)
        System.out.println("200 == 200 (Integer): " + (c1 == d1)); // false (different objects!)
        // == compares references for objects, not values. Always use equals() for value comparison!
        System.out.println("200.equals(200): " + c1.equals(d1));   // true (always use equals for comparing object values!)
        
        // GOTCHA #3: Performance impact in loops
        // BAD - creates thousands of Integer objects
        Long badSum = 0L;
        for (int j = 0; j < 1000; j++) {
            badSum += j; // Unbox, add, rebox... every iteration!
            
            // Compiler actually does:
            // 1. Unbox: long temp = badSum.longValue();
            // 2. Add:   temp = temp + j;
            // 3. Rebox: badSum = Long.valueOf(temp);  ← NEW object created!
        }
        
        // GOOD - use primitives in tight loops
        long goodSum = 0L;
        for (int j = 0; j < 1000; j++) {
            goodSum += j;
        }
        //we can have primitive arrays instead of object arrays, which are more memory efficient and faster to access. For example, int[] is much more efficient than Integer[] because it stores the actual values rather than references to Integer objects.

        // Java Memory: Stack vs Heap

        // Stack	                        Heap
        // Primitives (int, double, etc.)	Objects (new anything)
        // Method call info	                Arrays
        // Local variable references	    Strings
        // Fast, auto-cleanup	            Slower, garbage collected

        // Characteristics of stack memory:

        // LIFO (Last In, First Out) - like a stack of plates
        // Per thread - each thread has its own stack
        // Auto-cleanup - when method returns, its variables vanish
        // Fixed size - StackOverflowError if too deep (infinite recursion)
        // Fast - just move a pointer

        // Characteristics of heap memory:

        // Shared across all threads
        // Garbage collected - objects removed when no references point to them
        // Dynamic size - grows as needed (OutOfMemoryError if exhausted)
        // Slower - needs allocation, GC overhead

        // ═══════════════════════════════════════════════════════════════
        // TYPE INFERENCE WITH 'var' (Java 10+)
        // ═══════════════════════════════════════════════════════════════
        
        var name = "John";              // Inferred as String
        var numbers = new int[]{1,2,3}; // Inferred as int[]
        var list = new java.util.ArrayList<String>(); // Inferred as ArrayList<String>
        
        // PRO TIPS for 'var':
        // ✓ Use when type is obvious from RHS: var users = new HashMap<String, User>();
        // ✗ Avoid when type is unclear: var result = service.process(); // What type?
        // ✗ Cannot use for: fields, parameters, return types
        // ✗ Cannot be null: var x = null; // Compile error!

        // ═══════════════════════════════════════════════════════════════
        // STRING - Immutable, special handling
        // ═══════════════════════════════════════════════════════════════
        
        String str1 = "Hello"; // String pool (interned)
        String str2 = "Hello"; // Same reference from pool
        String str3 = new String("Hello"); // New object on heap
        
        System.out.println("str1 == str2: " + (str1 == str2)); // true (same pool reference)
        System.out.println("str1 == str3: " + (str1 == str3)); // false (different objects)
        System.out.println("str1.equals(str3): " + str1.equals(str3)); // true
        
        // String concatenation performance
        // BAD for loops - creates many intermediate String objects
        String bad = "";
        for (int j = 0; j < 100; j++) {
            bad += j; // Creates new String each time!
        }
        // The String "" was never modified. A NEW String "0" was created, and bad now points to it.
        // so we can say strings are immutable

        // GOOD - use StringBuilder
        StringBuilder good = new StringBuilder();
        for (int j = 0; j < 100; j++) {
            good.append(j);
        }
        // StringBuilder solves the performance problem by using a mutable internal char[] buffer.
        // Instead of creating a new String object on each append, it simply adds characters to
        // the same buffer. If the buffer fills up, it doubles in size and copies once — far
        // better than creating hundreds of String objects. The result: one object instead of
        // ~100, and O(n) memory instead of O(n²). Use StringBuilder for loops, StringBuffer
        // for thread-safe scenarios, and regular String concatenation for simple one-liners.
        
        // Text Blocks (Java 15+) - for multi-line strings
        String json = """
            {
                "name": "John",
                "age": 30
            }
            """;
        System.out.println(json);
    }
}
// String Pool
// A special memory area in the heap where Java stores unique string literals to save memory.
// When you create a string literal (e.g., "Hello"), Java checks the pool:
// - If it exists, it returns the reference to the existing string.
// - If not, it creates a new string in the pool and returns its reference.

// why we need string pool?
// // Without pool: 1000 "ERROR" strings = 1000 objects
// for (int i = 0; i < 1000; i++) {
//     log("ERROR");  // Each "ERROR" would be separate object
// }

//intern method
// String s1 = new String("Hello");  // Heap (not pooled)
// String s2 = s1.intern();          // Returns pooled "Hello"
// String s3 = "Hello";              // Same pooled "Hello"


    //        Stack              Pool                 Heap
    //      ┌───────┐         ┌─────────┐         ┌─────────┐
    // s1 ──┼───────┼─────────┼─────────┼────────►│ "Hello" │ (lonely copy)
    //      ├───────┤         │         │         └─────────┘
    // s2 ──┼───────┼────────►│ "Hello" │
    //      ├───────┤         │         │
    // s3 ──┼───────┼────────►│         │
    //      └───────┘         └─────────┘


// With pool: 1000 "ERROR" references = 1 object
// All point to same pooled "ERROR"
// why is String immutable?
// Problem it solves: Thread safety, security, and caching.

// Reason	                Explanation
// Thread safety	        Multiple threads can share String without synchronization
// Security	                Strings used in class loading, network connections, file paths — can't be tampered
// Hashcode caching	        Hash computed once, reused (important for HashMap keys)
// String pool possible	    Only works because Strings can't change
