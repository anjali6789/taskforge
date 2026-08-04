package com.taskforge.core.repository;

import com.taskforge.core.entity.Task;
import com.taskforge.core.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TaskRepository — your database access layer.
 *
 * HOW THIS WORKS (no magic, just clever design):
 *
 *   JpaRepository<Task, String> means:
 *     - Task   = the entity class to manage
 *     - String = the type of the @Id field (our UUID is stored as String)
 *
 *   Spring Data JPA sees this interface at startup and generates a full
 *   implementation class automatically. You never write it.
 *
 *   FREE methods you get instantly (no code needed):
 *     - save(task)           → INSERT or UPDATE
 *     - findById(id)         → SELECT WHERE id = ?   returns Optional<Task>
 *     - findAll()            → SELECT * FROM tasks
 *     - deleteById(id)       → DELETE WHERE id = ?
 *     - count()              → SELECT COUNT(*)
 *     - existsById(id)       → SELECT 1 WHERE id = ?
 *     ... and about 15 more
 *
 *   CUSTOM QUERIES via method naming:
 *   Spring reads the method name and writes the SQL for you.
 *   findByStatus  →  SELECT * FROM tasks WHERE status = ?
 *   findByTitleContainingIgnoreCase  →  SELECT * FROM tasks WHERE LOWER(title) LIKE ?
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    // Spring generates: SELECT * FROM tasks WHERE status = ?
    List<Task> findByStatus(TaskStatus status);

    // Spring generates: SELECT * FROM tasks WHERE LOWER(title) LIKE %keyword%
    List<Task> findByTitleContainingIgnoreCase(String keyword);

}
