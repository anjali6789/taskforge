public class Methods {
    //1. overloaded methods
    static void createTask(String title){
        System.out.println("Task created: " + title);
    }
    static void createTask(String title, String description){
        System.out.println("Task created: " + title + " - " + description);
    }
    static void createTask(String title, int priority){
        System.out.println("Task created: " + title + " with priority " + priority);
    }

    //2. Pass-by-value vs Pass-by-reference
    static void doubling(int num){
        num *= 2; // This modifies the local copy, not the original
    }
    static void rename(String[] arr){
        arr[0] = "Renamed"; // This modifies the original array
    }

    //3. var args
    // The ... messages means you can pass any number of strings to that method: could be 0, 1, or many. Inside the method, messages is treated as an array of Strings.
    static void log(String level, String... messages){
        //...messages must come last because if varargs wasn't last, Java wouldn't know where the varargs ends and level begins when you call it:
        for(String msg : messages){
            System.out.println("[" + level + "] " + msg);
        }
    }

    public static void main(String[] args) {
        // Demonstrating method overloading
        createTask("Finish report");
        createTask("Finish report", "Complete the annual report by Friday");
        createTask("Finish report", 1);

        // Demonstrating pass-by-value
        int number = 5;
        doubling(number);
        System.out.println("After doubling: " + number); // Output: 5

        // Demonstrating pass-by-reference
        String[] names = {"Original"};
        rename(names);
        System.out.println("After renaming: " + names[0]); // Output: Renamed

        // Demonstrating var args
        log("INFO", "Application started", "User logged in", "Data loaded");
    }
}
