import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyClassesAndObjects {

    static class Task {
        int id;
        String title;
        String status;

        Task(int id, String title, String status) {
            this.id = id;
            this.title = title;
            this.status = status;
        }

        // toString() — every class inherits a default toString() from Object
        // but default returns "Task@6d06d69c" (class name + memory address) — useless
        // we override it to return something readable
        // Java automatically calls toString() when you do System.out.println(object)
        @Override
        public String toString() {
            return "Task{id=" + id + ", title=" + title + ", status=" + status + "}";
        }

        // equals() — default equals() inherited from Object behaves same as ==
        // it compares memory addresses, not values
        // we override it to define what "equal" means for Task — here: same id = same task
        //
        // Step 1: if (this == o) return true
        //   → same object in memory, obviously equal (like checking: is it literally you?)
        //
        // Step 2: if (!(o instanceof Task t)) return false
        //   → wrong type, can't be equal to a Task (like checking: are you even a person?)
        //   → pattern matching — casts o to Task t in one step (Java 16+)
        //
        // Step 3: return id == t.id
        //   → same type, now check if same Task by id (like checking: same ID card number?)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Task t)) return false;
            return id == t.id;
        }

        // hashCode() — converts object into a number
        // used by HashMap/HashSet to organise objects into buckets for fast lookup:
        //   hashCode() = 4 → bucket 4 → [Task1, Task5]
        //   hashCode() = 7 → bucket 7 → [Task2]
        // when searching: compute hash → find bucket → use equals() to find exact match
        //
        // Objects.hash(id) is a utility method — same id always produces same number
        //
        // RULE: use exact same fields in hashCode() as in equals()
        //   equals checks id → hashCode uses id ✅
        //   equals checks id → hashCode uses title ❌ broken
        //
        // WHY both must be overridden together:
        //   if you override equals() but NOT hashCode():
        //     t1.equals(t2) → true  (you defined this)
        //     t1.hashCode() → 12345 (random memory-based number)
        //     t2.hashCode() → 67890 (different random number)
        //     HashSet puts them in different buckets → never runs equals() → duplicate added ❌
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public static void main(String[] args) {

        // 1. toString
        System.out.println("--- toString ---");
        Task t1 = new Task(1, "Fix bug", "TODO");
        System.out.println(t1);  // calls t1.toString() automatically

        // 2. == vs equals
        // == asks: are these the same object in memory?
        // equals() asks: do these represent the same thing? (based on our override)
        System.out.println("--- == vs equals ---");
        Task t2 = new Task(1, "Fix bug", "TODO");  // different object in memory, same id
        Task t3 = new Task(2, "Write tests", "TODO");
        System.out.println("t1 == t2: " + (t1 == t2));         // false — different objects in memory
        System.out.println("t1.equals(t2): " + t1.equals(t2)); // true — same id
        System.out.println("t1.equals(t3): " + t1.equals(t3)); // false — different id

        // 3. HashSet — stores unique objects only
        // uses hashCode() to find the right bucket, then equals() to check for duplicates
        // adding t2: hashCode matches t1 → goes to same bucket → equals() returns true → rejected
        System.out.println("--- HashSet ---");
        Set<Task> tasks = new HashSet<>();
        tasks.add(t1);
        tasks.add(t2);  // same id as t1 — duplicate, gets rejected
        tasks.add(t3);
        System.out.println("Set size: " + tasks.size());  // 2, not 3
    }
}
