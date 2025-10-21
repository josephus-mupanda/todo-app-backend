package com.example.todo_app.Task.repositories;

import com.example.todo_app.Task.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByUser_Id(String userId);
}
