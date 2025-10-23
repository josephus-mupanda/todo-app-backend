package com.example.todo_app.common.repositories;

import com.example.todo_app.common.models.PasswordResetToken;
import com.example.todo_app.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    void deleteByExpiryDateBefore(LocalDateTime now);
    PasswordResetToken findByToken(String token);
    void deleteByUser(User user);
}
