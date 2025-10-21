package com.example.todo_app.user.dtos;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean isActive;
    private boolean isAdmin;
}
