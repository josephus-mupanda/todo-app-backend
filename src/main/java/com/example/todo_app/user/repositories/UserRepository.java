package com.example.todo_app.user.repositories;
import com.example.todo_app.common.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPasswordResetToken(String token);
}
