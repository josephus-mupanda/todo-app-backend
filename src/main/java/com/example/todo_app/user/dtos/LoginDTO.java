package com.example.todo_app.user.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {
    public record Input(String email, String password) {}
    public record Output(String token) {}
}

