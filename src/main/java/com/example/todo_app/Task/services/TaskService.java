package com.example.todo_app.Task.services;

import com.example.todo_app.Task.dtos.TaskDTO;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO dto);
    TaskDTO getTaskById(String id);
    List<TaskDTO> getTasksByUser(String userId);
    TaskDTO updateTask(String id, TaskDTO dto);
    void deleteTask(String id);
}
