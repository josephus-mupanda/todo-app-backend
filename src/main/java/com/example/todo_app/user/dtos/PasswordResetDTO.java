package com.example.todo_app.user.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetDTO {
    private String token;
    private String newPassword;
}

