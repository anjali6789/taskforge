package com.taskforge.api.exception;

import com.taskforge.core.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler — catches exceptions from ALL controllers in one place.
 *
 * WHY @ControllerAdvice?
 *   Without this, every controller method needs its own try-catch → lots of repeated code.
 *   With this, you throw an exception anywhere in your service/controller and this class
 *   intercepts it and converts it to the right HTTP response automatically.
 *
 *   Flow:
 *     TaskController.updateTaskStatus()
 *         → calls TaskService.updateTask()
 *             → throws TaskNotFoundException  (task not found)
 *                 → @ControllerAdvice intercepts it
 *                     → returns HTTP 404 with JSON error body
 *
 * WHY ProblemDetail?
 *   ProblemDetail is a Spring 6 / RFC 9457 standard for error responses.
 *   Instead of making your own error POJO, use the standard format that API clients expect:
 *   {
 *     "type": "about:blank",
 *     "title": "Not Found",
 *     "status": 404,
 *     "detail": "Task not found with id: abc-123",
 *     "instance": "/api/v1/tasks/abc-123"
 *   }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles TaskNotFoundException → HTTP 404
     * Triggered when a task ID doesn't exist in the database.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleTaskNotFound(TaskNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Task Not Found");
        problem.setProperty("taskId", ex.getTaskId());
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Catches any unhandled exception → HTTP 500
     * Safety net so your API never exposes a raw stack trace to clients.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"   // don't expose internal details to clients
        );
        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
