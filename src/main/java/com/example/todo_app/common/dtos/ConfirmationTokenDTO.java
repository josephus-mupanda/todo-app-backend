package com.example.todo_app.common.dtos;
import java.time.LocalDateTime;

public final class ConfirmationTokenDTO {
    private ConfirmationTokenDTO() {}

    public record Input(
            String token,
            LocalDateTime createdDate,
            LocalDateTime expiryDate,
            String userId
    ) {}

    public record Output(
            String id,
            String token,
            LocalDateTime createdDate,
            LocalDateTime expiryDate,
            String userId
    ) {}
}
