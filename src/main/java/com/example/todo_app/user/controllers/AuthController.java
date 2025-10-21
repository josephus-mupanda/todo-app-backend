package com.example.todo_app.user.controllers;

import com.example.todo_app.common.annotations.IsAuthenticated;
import com.example.todo_app.common.annotations.PublicEndpoint;
import com.example.todo_app.user.dtos.*;
import com.example.todo_app.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns the registered user details."
    )
    @PostMapping("/register")
    @PublicEndpoint
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @Operation(
            summary = "Login a user",
            description = "Authenticates a user using email and password, returning an access token."
    )
    @PostMapping("/login")
    @PublicEndpoint
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(userService.loginUser(dto));
    }

    @Operation(
            summary = "Request password reset code",
            description = "Sends a password reset link or verification code to the user's email."
    )
    @PostMapping("/password-reset/request")
    @PublicEndpoint
    public ResponseEntity<String> requestReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        userService.requestPasswordResetCode(dto);
        return ResponseEntity.ok("Password reset email sent.");
    }

    @Operation(
            summary = "Reset user password",
            description = "Resets the user password using a valid reset code or token."
    )
    @PostMapping("/password-reset")
    @PublicEndpoint
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successful.");
    }

    @Operation(
            summary = "Logout a user",
            description = "Invalidates the user session or JWT token."
    )
    @PostMapping("/logout")
    @IsAuthenticated
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        userService.logoutUser(token);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
