package com.taskforge.api.controller;

import com.taskforge.core.entity.Task;
import com.taskforge.core.entity.TaskStatus;
import com.taskforge.core.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    // constructor injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    // ResponseEntity gives us control over status: 200 if found, 404 if not
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @ResponseStatus(CREATED) always sends 201 for successful creation
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestParam String title) {
        return taskService.createTask(title);
    }

    // ResponseEntity gives us 200 on success; 404 is handled by GlobalExceptionHandler
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable String id, @RequestParam TaskStatus status) {
        Task updated = taskService.updateTask(id, status);
        return ResponseEntity.ok(updated);
    }

    // @ResponseStatus(NO_CONTENT) sends 204 — success with no body
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
    }

}
