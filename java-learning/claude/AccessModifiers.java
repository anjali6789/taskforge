public class AccessModifiers {

    // private — only accessible inside Task
    // protected — accessible in Task + subclasses
    // public — accessible everywhere
    // no modifier — package-private, only same package
    static class BaseEntity {
        protected long id;           // subclasses can access
        private String createdAt;    // only BaseEntity can touch this

        BaseEntity(long id) {
            this.id = id;
            this.createdAt = "2026-06-01";
        }

        // package-private helper — internal use only
        String getCreatedAt() {
            return createdAt;
        }
    }

    static class Task extends BaseEntity {
        private String title;    // only Task can touch this
        private String status;   // only Task can touch this

        public Task(long id, String title) {
            super(id);           // calls BaseEntity constructor
            this.title = title;
            this.status = "TODO";
        }

        // public getter — controlled read access
        public String getTitle() { return title; }

        // public setter with validation — controlled write access
        public void setStatus(String status) {
            if (status == null || status.isBlank()) {
                throw new IllegalArgumentException("Status cannot be empty");
            }
            this.status = status;
        }

        public String getStatus() { return status; }

        // can access protected field from BaseEntity
        public long getId() { return id; }

        @Override
        public String toString() {
            return "Task{id=" + id + ", title=" + title + ", status=" + status + "}";
        }
    }

    public static void main(String[] args) {

        Task task = new Task(1L, "Fix bug");
        System.out.println(task);

        // public method — accessible everywhere
        System.out.println("Title: " + task.getTitle());

        // setter with validation
        task.setStatus("IN_PROGRESS");
        System.out.println("Status: " + task.getStatus());

        // try invalid status
        try {
            task.setStatus("");  // throws IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // package-private — works here since same package
        System.out.println("Created at: " + task.getCreatedAt());

        // task.title = "hacked";   // ❌ private — compile error
        // task.id = 99;            // ❌ protected — only subclasses
    }
}