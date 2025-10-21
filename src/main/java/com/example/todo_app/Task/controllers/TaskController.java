package com.example.todo_app.Task.controllers;

import com.example.todo_app.Task.dtos.TaskDTO;
import com.example.todo_app.Task.services.TaskService;
import com.example.todo_app.common.annotations.IsAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing user tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Create a new task",
            description = "Creates a task associated with the authenticated user."
    )
    @PostMapping
    @IsAuthenticated
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @Operation(
            summary = "Get task by ID",
            description = "Fetches a task by its ID if it belongs to the authenticated user."
    )
    @GetMapping("/{id}")
    @IsAuthenticated
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(
            summary = "Get all tasks for a user",
            description = "Returns all tasks created by a specific user."
    )
    @GetMapping("/user/{userId}")
    @IsAuthenticated
    public ResponseEntity<List<TaskDTO>> getTasksByUser(@PathVariable String userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    @Operation(
            summary = "Update a task",
            description = "Updates the title or completion status of an existing task."
    )
    @PutMapping("/{id}")
    @IsAuthenticated
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String id, @Valid @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @Operation(
            summary = "Delete a task",
            description = "Soft deletes a task owned by the authenticated user."
    )
    @DeleteMapping("/{id}")
    @IsAuthenticated
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
