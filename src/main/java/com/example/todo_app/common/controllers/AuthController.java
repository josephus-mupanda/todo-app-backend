package com.example.todo_app.common.controllers;

import com.example.todo_app.common.annotations.IsAuthenticated;
import com.example.todo_app.common.annotations.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @PublicEndpoint
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    @PublicEndpoint
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        authService.register(dto);
        return ResponseEntity.ok("Verification code sent to email");
    }

    @PostMapping("/confirm")
    @PublicEndpoint
    public ResponseEntity<?> confirm(@RequestBody ConfirmDTO dto) {
        authService.confirmRegistration(dto.getCode());
        return ResponseEntity.ok("User confirmed successfully");
    }

    @PostMapping("/forgot-password")
    @PublicEndpoint
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        authService.forgotPassword(dto.getEmail());
        return ResponseEntity.ok("Password reset code sent to email");
    }

    @PostMapping("/reset-password")
    @PublicEndpoint
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        authService.resetPassword(dto.getCode(), dto.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/logout")
    @IsAuthenticated
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok("Logged out successfully");
    }
}

