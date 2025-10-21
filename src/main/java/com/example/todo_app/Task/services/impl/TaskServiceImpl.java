package com.example.todo_app.Task.services.impl;

import com.example.todo_app.Task.dtos.TaskDTO;
import com.example.todo_app.Task.mappers.TaskMapper;
import com.example.todo_app.Task.repositories.TaskRepository;
import com.example.todo_app.Task.services.TaskService;
import com.example.todo_app.Task.models.Task;
import com.example.todo_app.common.domains.IamUserDetails;
import com.example.todo_app.common.exceptions.ForbiddenException;
import com.example.todo_app.common.exceptions.NotFoundException;
import com.example.todo_app.user.models.User;
import com.example.todo_app.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Helper to get current authenticated user
    private User getAuthenticatedUser() {
        IamUserDetails userDetails = (IamUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
    }

    @Override
    public TaskDTO createTask(TaskDTO dto) {
        Task task = TaskMapper.fromDTO(dto);
        User user = getAuthenticatedUser();
        task.setUser(user);
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public TaskDTO getTaskById(String id) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to access this task");
        }
        return TaskMapper.toDTO(task);
    }

    @Override
    public List<TaskDTO> getTasksByUser() {
        User user = getAuthenticatedUser();
        return taskRepository.findByUser_Id(user.getId())
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(String id, TaskDTO dto) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to update this task");
        }

        task.setTitle(dto.getTitle());
        task.setCompleted(dto.isCompleted());
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(String id) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to delete this task");
        }

        taskRepository.delete(task);
    }
}
