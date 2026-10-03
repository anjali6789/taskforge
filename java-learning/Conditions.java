/**
 * CONDITIONS (if, switch) - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Switch expressions (Java 14+) vs switch statements
 * - Pattern matching in switch (Java 21+)
 * - Guard patterns and when to use them
 * - Common pitfalls and best practices
 */
public class Conditions {

    public static void main(String[] args) {
        
        // ═══════════════════════════════════════════════════════════════
        // IF-ELSE - Best Practices
        // ═══════════════════════════════════════════════════════════════
        
        int score = 85;
        
        // PATTERN: Early return (Guard clauses) - cleaner than nested ifs
        // BAD:
        /*
        if (score >= 0) {
            if (score <= 100) {
                // do something
            }
        }
        */
        
        // GOOD: Fail fast
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Invalid score");
        }
        // Now proceed with valid score...
        
        // PATTERN: Avoid else after return
        // BAD:
        /*
        if (condition) {
            return a;
        } else {
            return b;
        }
        */
        
        // GOOD:
        /*
        if (condition) {
            return a;
        }
        return b;
        */

        // ═══════════════════════════════════════════════════════════════
        // SWITCH STATEMENT (Traditional) - Know the pitfalls
        // ═══════════════════════════════════════════════════════════════
        
        int dayNum = 3;
        String dayName;
        
        // Traditional switch - MUST remember break!
        switch (dayNum) {
            case 1:
                dayName = "Monday";
                break;  // Without break, falls through to next case!
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            // Fall-through can be intentional:
            case 6:
            case 7:
                dayName = "Weekend";
                break;
            default:
                dayName = "Unknown";
        }
        System.out.println("Day: " + dayName);
        
        // ═══════════════════════════════════════════════════════════════
        // SWITCH EXPRESSION (Java 14+) - Modern, safer, more concise
        // ═══════════════════════════════════════════════════════════════
        
        // Arrow syntax - no fall-through, no break needed!
        String dayNameModern = switch (dayNum) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6, 7 -> "Weekend";  // Multiple labels
            default -> "Unknown";
        };
        System.out.println("Modern day: " + dayNameModern);
        
        // With code blocks - use 'yield' to return value
        String description = switch (dayNum) {
            case 1 -> {
                System.out.println("Starting the week...");
                yield "Monday Blues";
            }
            case 5 -> {
                System.out.println("Almost weekend!");
                yield "TGIF";
            }
            default -> "Regular day";
        };
        System.out.println(description);
        
        // Exhaustiveness check - compiler ensures all cases covered
        enum Status { PENDING, APPROVED, REJECTED }
        Status status = Status.APPROVED;
        
        // Compiler error if you miss a case (no default needed for enums)!
        String message = switch (status) {
            case PENDING -> "Waiting for review";
            case APPROVED -> "Request approved";
            case REJECTED -> "Request denied";
            // No default needed - compiler knows all cases covered
        };
        
        // ═══════════════════════════════════════════════════════════════
        // PATTERN MATCHING IN SWITCH (Java 21+)
        // ═══════════════════════════════════════════════════════════════
        
        Object obj = "Hello";
        
        // Type pattern matching
        String result = switch (obj) {
            case Integer i -> "Integer: " + i;
            case Long l -> "Long: " + l;
            case String s -> "String of length " + s.length();
            case null -> "It's null!";  // Can handle null explicitly!
            default -> "Unknown type";
        };
        System.out.println(result);
        
        // With guard conditions (when clause)
        Object value = 42;
        String category = switch (value) {
            case Integer i when i < 0 -> "Negative integer";
            case Integer i when i == 0 -> "Zero";
            case Integer i when i > 0 && i <= 100 -> "Small positive";
            case Integer i -> "Large positive: " + i;
            case String s when s.isEmpty() -> "Empty string";
            case String s -> "String: " + s;
            case null -> "Null value";
            default -> "Other type";
        };
        System.out.println("Category: " + category);
        
        // Record patterns (Java 21+)
        record Point(int x, int y) {}
        // A record is a shortcut for a class that just holds data.
        Object point = new Point(3, 4);
        
        String pointDesc = switch (point) {
            case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
            // Think of it like: "if it's a Point AND x and y are both 0 → Origin"
            case Point(int x, int y) when x == y -> "On diagonal";
            case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
            default -> "Not a point";
        };
        System.out.println(pointDesc);

        // ═══════════════════════════════════════════════════════════════
        // BEST PRACTICES FOR CONDITIONS
        // ═══════════════════════════════════════════════════════════════
        
        // 1. Prefer positive conditions
        // BAD:  if (!isNotValid())
        // GOOD: if (isValid())
        
        // 2. Use enums instead of magic numbers/strings
        // BAD:  if (status == 1)
        // GOOD: if (status == Status.APPROVED)
        
        // 3. Extract complex conditions into methods
        // BAD:  if (user.getAge() >= 18 && user.hasId() && !user.isBanned())
        // GOOD: if (user.canPurchaseAlcohol())
        
        // 4. Use Optional to avoid null checks
        String userName = null;
        // BAD:  if (userName != null) { ... }
        // GOOD:
        java.util.Optional.ofNullable(userName)
            .ifPresent(name -> System.out.println("User: " + name));
//         Optional.ofNullable(userName) — Wrap userName in an Optional container.

        // If userName is null → creates an empty Optional
        // If userName has a value → creates Optional containing that value
        // .ifPresent(...) — "If there's a value inside, do this"

        // If Optional is empty (was null) → does nothing
        // If Optional has value → runs the code with that value

        // 5. Prefer switch expression over if-else chains for multiple values
        // BAD: if (x == 1) ... else if (x == 2) ... else if (x == 3) ...
        // GOOD: Use switch expression
        
        // 6. Use Objects.requireNonNull for parameter validation
        // public void process(String data) {
        //     this.data = Objects.requireNonNull(data, "data cannot be null");
        // }
        // Throws NullPointerException immediately with a clear message — better than a random crash 10 lines later.
    }
    
    // Example: Refactored complex condition
    static boolean canVote(Person person) {
        return person != null 
            && person.age() >= 18 
            && person.isCitizen() 
            && !person.isFelon();
    }
    
    record Person(int age, boolean isCitizen, boolean isFelon) {}
}
