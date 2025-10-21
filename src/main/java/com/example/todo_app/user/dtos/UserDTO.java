package com.example.todo_app.user.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private Boolean isActive;
    private Boolean isAdmin;
    private Set<String> roles;
    private LocalDateTime createdAt;
}

