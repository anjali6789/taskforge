package com.taskforge.core.exception;

/**
 * Thrown when a task is requested by ID but doesn't exist in the database.
 *
 * WHY A CUSTOM EXCEPTION?
 *   - RuntimeException is too generic — any part of your code can throw it for any reason
 *   - TaskNotFoundException is specific and meaningful: "this task ID doesn't exist"
 *   - The @ControllerAdvice handler can catch it precisely and return 404
 *   - Stack traces become much easier to read in logs
 *
 * WHY EXTEND RuntimeException (not Exception)?
 *   - RuntimeException = unchecked → callers don't need to declare "throws TaskNotFoundException"
 *   - Checked exceptions (extends Exception) force every method to declare them — messy for web apps
 *   - Spring's own exceptions (like EntityNotFoundException) are all unchecked for the same reason
 */
public class TaskNotFoundException extends RuntimeException {

    private final String taskId;

    public TaskNotFoundException(String taskId) {
        super("Task not found with id: " + taskId);
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
