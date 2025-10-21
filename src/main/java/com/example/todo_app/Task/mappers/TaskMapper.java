package com.example.todo_app.Task.mappers;

import com.example.todo_app.Task.dtos.TaskDTO;
import com.example.todo_app.Task.models.Task;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(task.isCompleted())
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .build();
    }

    public static Task fromDTO(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setCompleted(dto.isCompleted());
        return task;
    }
}

