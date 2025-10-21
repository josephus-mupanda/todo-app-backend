package com.example.todo_app.user.dtos;

import com.example.todo_app.common.enums.UserType;
import java.util.Set;


public final class UserDTO {
    private UserDTO() {}

    public record Input(
            String username,
            String email,
            String passwordHash,
            UserType userType,
            Boolean enabled
    ) {}

    public record Output(
            String id,
            String username,
            String email,
            UserType userType,
            Boolean enabled,
            Set<String> roleIds,
            Set<String> tasksIds
    ) {}
}

