package com.example.todo_app.common.repositories;
import com.example.todo_app.common.models.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    long deleteByExpiryDateBefore(LocalDateTime now);

    ConfirmationToken findByToken(String token);
}

