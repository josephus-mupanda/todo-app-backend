package com.example.todo_app.user.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;
}
