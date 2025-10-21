package com.example.todo_app.user.services;

import com.example.todo_app.user.dtos.*;

public interface UserService {
    UserResponseDTO registerUser(RegisterUserDTO registerDTO);

    AuthResponseDTO loginUser(LoginDTO loginDTO);

    void requestPasswordResetCode(PasswordResetRequestDTO dto);

    void resetPassword(PasswordResetDTO dto);

    void logoutUser(String token);

    UserResponseDTO getUserById(String id);
}
