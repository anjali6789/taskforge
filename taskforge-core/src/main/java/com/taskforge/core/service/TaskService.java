package com.taskforge.core.service;
import com.taskforge.core.entity.Task;
import com.taskforge.core.entity.TaskStatus;
import com.taskforge.core.exception.TaskNotFoundException;
import com.taskforge.core.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task createTask(String title){
        Task t = new Task(title);
        return taskRepository.save(t);
    }

    @Transactional
    public Task updateTask(String id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

}
