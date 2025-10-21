package com.example.todo_app.Task.services;

import com.example.todo_app.Task.dtos.TaskDTO;
import com.example.todo_app.common.domains.IamUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO dto);
    TaskDTO getTaskById(String id);
    List<TaskDTO> getTasksByUser();

    TaskDTO updateTask(String id, TaskDTO dto);
    void deleteTask(String id);
}
