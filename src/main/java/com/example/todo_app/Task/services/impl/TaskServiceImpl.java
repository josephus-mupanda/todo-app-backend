package com.example.todo_app.Task.services.impl;

import com.example.todo_app.Task.dtos.TaskDTO;
import com.example.todo_app.Task.mappers.TaskMapper;
import com.example.todo_app.Task.repositories.TaskRepository;
import com.example.todo_app.Task.services.TaskService;
import com.example.todo_app.common.models.Task;
import com.example.todo_app.common.models.User;
import com.example.todo_app.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskDTO createTask(TaskDTO dto) {
        Task task = TaskMapper.fromDTO(dto);
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public TaskDTO getTaskById(String id) {
        return taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public List<TaskDTO> getTasksByUser(String userId) {
        return taskRepository.findByUser_Id(userId)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(String id, TaskDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(dto.getTitle());
        task.setCompleted(dto.isCompleted());
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }
}
