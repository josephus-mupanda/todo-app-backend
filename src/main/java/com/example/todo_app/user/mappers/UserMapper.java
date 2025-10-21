package com.example.todo_app.user.mappers;

import com.example.todo_app.common.domains.Role;
import com.example.todo_app.common.models.User;
import com.example.todo_app.user.dtos.UserResponseDTO;

import java.util.stream.Collectors;


public class UserMapper {

    public static UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .isAdmin(user.getIsAdmin())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}


