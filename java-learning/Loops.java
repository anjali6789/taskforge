import java.util.*;
import java.util.stream.*;

/**
 * LOOPS (for, while) - Deep Dive
 * 
 * As a 3-year experienced developer, you need to understand:
 * - Traditional loops vs enhanced for vs Streams
 * - When to use which (performance and readability)
 * - Iterator and ConcurrentModification
 * - Loop optimization and common pitfalls
 */
public class Loops {

    public static void main(String[] args) {
        
        // ═══════════════════════════════════════════════════════════════
        // TRADITIONAL FOR LOOP - Use when you need the index
        // ═══════════════════════════════════════════════════════════════
        
        int[] numbers = {10, 20, 30, 40, 50};
        
        // Classic indexed loop
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Index " + i + ": " + numbers[i]);
        }
        
        // Reverse iteration
        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.println("Reverse: " + numbers[i]);
        }
        
        // Step by 2
        for (int i = 0; i < numbers.length; i += 2) {
            System.out.println("Every other: " + numbers[i]);
        }
        
        // PITFALL: Off-by-one errors
        // < length (correct) vs <= length (ArrayIndexOutOfBoundsException!)
        
        // PITFALL: Modifying loop variable inside loop
        // for (int i = 0; i < 10; i++) { i = 5; } // Infinite loop!

        // ═══════════════════════════════════════════════════════════════
        // ENHANCED FOR LOOP (for-each) - Cleaner when index not needed
        // ═══════════════════════════════════════════════════════════════
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Simple and readable
        for (String name : names) {
            System.out.println("Hello, " + name);
        }
        
        // Works with any Iterable or array
        int[] nums = {1, 2, 3};
        for (int n : nums) {
            System.out.println(n);
        }
        
        // LIMITATION: Can't modify the collection
        // for (String name : names) {
        //     names.remove(name); // ConcurrentModificationException!
        // }
        
        // LIMITATION: Can't access index
        // Need index? Use traditional for or IntStream
        
        // ═══════════════════════════════════════════════════════════════
        // WHILE AND DO-WHILE
        // ═══════════════════════════════════════════════════════════════
        
        // While - condition checked BEFORE first iteration
        int count = 0;
        while (count < 3) {
            System.out.println("While: " + count);
            count++;
        }
        
        // Do-While - executes AT LEAST ONCE
        int num = 10;
        do {
            System.out.println("Do-while: " + num); // Prints even though condition false
        } while (num < 5);
        
        // Use case: Input validation
        // Scanner scanner = new Scanner(System.in);
        // int input;
        // do {
        //     System.out.print("Enter positive number: ");
        //     input = scanner.nextInt();
        // } while (input <= 0);

        // ═══════════════════════════════════════════════════════════════
        // ITERATOR - Safe modification during iteration
        // ═══════════════════════════════════════════════════════════════
        
        List<String> mutableNames = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie", "Bob"));
        
        // Safe removal using Iterator
        Iterator<String> iterator = mutableNames.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name.equals("Bob")) {
                iterator.remove(); // Safe! Uses iterator's remove
            }
        }
        // The iterator tracks position automatically. Every time you call iterator.next(), it:
        // Returns the current element
        // Moves forward to the next one internally

        // Rule: one next() → one remove(). Always. If you call next() twice without remove(), you can only remove the second element. If you call remove() without next(), it throws IllegalStateException.
        // next() doesn't move TO the element — it moves PAST it and returns what it passed:
        // ["Fix bug", "Write tests", "Deploy",  end]
        // ↑
        // iterator.hasNext() → true (Fix bug is ahead)
        // iterator.next()    → returns "Fix bug", cursor moves

            // ["Fix bug", "Write tests", "Deploy",  end]
            //                 ↑
            // iterator.hasNext() → true (Write tests is ahead)
            // iterator.next()    → returns "Write tests", cursor moves

            // ["Fix bug", "Write tests", "Deploy",  end]
            //                                 ↑
            // iterator.hasNext() → true (Deploy is ahead)
            // iterator.next()    → returns "Deploy", cursor moves

            // ["Fix bug", "Write tests", "Deploy",  end]
            //                                         ↑
            // iterator.hasNext() → false (nothing ahead)
            // loop ends
        System.out.println("After removal: " + mutableNames);
        
        // ALTERNATIVE (Java 8+): removeIf - even cleaner!
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "B"));
        list.removeIf(item -> item.equals("B"));
        System.out.println("After removeIf: " + list);

        // ═══════════════════════════════════════════════════════════════
        // STREAMS (Java 8+) - Declarative, functional style
        // ═══════════════════════════════════════════════════════════════
        
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Stream pipeline: source -> intermediate ops -> terminal op
        List<Integer> evenDoubled = numberList.stream()
            .filter(n -> n % 2 == 0)    // Keep evens
            .map(n -> n * 2)             // Double them
            .collect(Collectors.toList());
        System.out.println("Even doubled: " + evenDoubled);
        
        // Reduction operations
        int sum = numberList.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        
        // Finding elements
        Optional<Integer> firstEven = numberList.stream()
            .filter(n -> n % 2 == 0)
            .findFirst();
        firstEven.ifPresent(n -> System.out.println("First even: " + n));
        
        // Check conditions
        boolean allPositive = numberList.stream().allMatch(n -> n > 0);
        boolean anyNegative = numberList.stream().anyMatch(n -> n < 0);
        boolean noneNegative = numberList.stream().noneMatch(n -> n < 0);
        
        // With index using IntStream
        IntStream.range(0, names.size())
            .forEach(i -> System.out.println("Index " + i + ": " + names.get(i)));
        
        // Parallel streams - use for CPU-intensive operations on large data
        long parallelSum = numberList.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();
        
        // CAUTION: Don't use parallel streams for:
        // - Small collections (overhead > benefit)
        // - IO operations (thread pool not designed for blocking)
        // - Operations with shared mutable state

        // ═══════════════════════════════════════════════════════════════
        // LOOP OPTIMIZATION - Performance considerations
        // ═══════════════════════════════════════════════════════════════
        
        List<String> largeList = new ArrayList<>();
        // Assume largeList has 1 million items...
        
        // BAD: Calculates size() every iteration
        // for (int i = 0; i < largeList.size(); i++) { }
        
        // BETTER: Cache the size (unless list changes)
        int size = largeList.size();
        for (int i = 0; i < size; i++) { 
            // Process largeList.get(i)
        }
        
        // BAD: LinkedList + indexed access = O(n²)
        // LinkedList<Integer> linked = new LinkedList<>();
        // for (int i = 0; i < linked.size(); i++) {
        //     linked.get(i); // O(n) for each get!
        // }
        
        // GOOD: Use iterator or enhanced for
        // for (Integer item : linked) { } // O(n)

        // ═══════════════════════════════════════════════════════════════
        // LOOP CONTROL: break, continue, labels
        // ═══════════════════════════════════════════════════════════════
        
        // break - exits the loop entirely
        for (int i = 0; i < 10; i++) {
            if (i == 5) break;
            System.out.println("Break loop: " + i);
        }
        
        // continue - skips current iteration
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) continue; // Skip even numbers
            System.out.println("Continue loop: " + i);
        }
        
        // Labeled break/continue - for nested loops
        outer: for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    break outer; // Breaks out of OUTER loop
                }
                System.out.println("Labeled: " + i + "," + j);
            }
        }
        
        // ═══════════════════════════════════════════════════════════════
        // CHOOSING THE RIGHT LOOP
        // ═══════════════════════════════════════════════════════════════
        
        /*
         * Use TRADITIONAL FOR when:
         * - You need the index
         * - You need to modify the loop counter
         * - Iterating backwards or with custom step
         * 
         * Use ENHANCED FOR when:
         * - Simple iteration, no index needed
         * - Readability is priority
         * 
         * Use WHILE when:
         * - Number of iterations unknown
         * - Condition-based termination
         * 
         * Use STREAMS when:
         * - Transforming/filtering collections
         * - Chaining operations
         * - Parallel processing needed
         * - More declarative style preferred
         * 
         * DON'T use STREAMS when:
         * - Simple iteration (overhead not worth it)
         * - Need to modify local variables (effectively final requirement)
         * - Need to throw checked exceptions
         * - Performance-critical tight loops
         */
    }
}
