package com.example.todo_app.user.controllers;

import com.example.todo_app.common.annotations.IsAuthenticated;
import com.example.todo_app.common.annotations.PublicEndpoint;
import com.example.todo_app.common.exceptions.*;
import com.example.todo_app.common.listeners.UserListener;
import com.example.todo_app.common.models.ConfirmationToken;
import com.example.todo_app.common.models.PasswordResetToken;
import com.example.todo_app.common.responses.GenericResponse;
import com.example.todo_app.common.services.EmailSenderService;
import com.example.todo_app.user.dtos.*;
import com.example.todo_app.user.mappers.UserMapper;
import com.example.todo_app.user.models.User;
import com.example.todo_app.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and account management")
public class AuthController {
    private final UserService userService;
    private final UserListener userListener;
    private final EmailSenderService emailSenderService;
    @Value("${app.frontend-base-url}")
    private String url ;
    @Value("${application.users.admin.email}")
    private String adminEmail;

    @Autowired
    public AuthController(UserService userService, UserListener userListener, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.userListener = userListener;
        this.emailSenderService = emailSenderService;
    }

    // ==================== REGISTER ====================
    @PublicEndpoint
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserDTO.Output>> register(
            @Valid @RequestBody AuthDTO.RegisterRequest request
    ) {
        if (userService.hasUserWithUsername(request.username())) throw new ConflictException("Username exists");
        if (userService.hasUserWithEmail(request.email())) throw new ConflictException("Email exists");

        User user = userService.registerUser(request.username(), request.email(), request.password());
        ConfirmationToken token = userService.createConfirmationToken(user);

        // Send numeric code to email
        emailSenderService.sendEmail(user.getEmail(), "Confirm your account", "Your code is: " + token.getToken());

        userListener.logUserAction(user, "CREATE_USER");

        UserDTO.Output  userDTO = UserMapper.toDTO(user);
        return GenericResponse.created("User registered successfully", userDTO);

    }

    // ==================== LOGIN ====================
    @PublicEndpoint
    @Operation(summary = "Login a user and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthDTO.LoginResponse>> login(
            @Valid @RequestBody AuthDTO.LoginRequest request
    ) {
        String token = userService.verify(request.username(), request.password());

        if ("Failed".equals(token)) {
            throw new UnauthorizedException("Invalid username or password");
        }

        User loggedInUser = userService.getUserByUsername(request.username());

        if (!loggedInUser.getEnabled()) {
            throw new ForbiddenException("Email not confirmed");
        }

        userListener.logUserAction(loggedInUser, "LOGIN");

        AuthDTO.LoginResponse response = new AuthDTO.LoginResponse(token, loggedInUser.getUsername());

        return GenericResponse.ok("Login successful", response);
    }

    // ==================== LOGOUT ====================
    @IsAuthenticated
    @Operation(summary = "Logout a user")
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<String>> logout(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.getUserFromToken(token);
        if (user == null) {
            throw new BadRequestException("Invalid token or user not found");
        }
        // Add token to blacklist
        userService.invalidateToken(token);
        userListener.logUserAction(user, "LOGOUT");

        return GenericResponse.ok("Logged out successfully");
    }

    // ==================== CONFIRM EMAIL ====================
    @PublicEndpoint
    @Operation(summary = "Confirm user email")
    @PostMapping("/confirm")
    public ResponseEntity<GenericResponse<String>> confirmUser(
            @RequestParam("code") String code
    ) {
        ConfirmationToken token = userService.getConfirmationToken(code);
        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Invalid or expired code");

        User user = token.getUser();
        user.setEnabled(true);
        userService.saveUser(user);
        userService.deleteConfirmationToken(token);

        return GenericResponse.ok("User confirmed successfully");
    }

    // ==================== RESET PASSWORD ====================
    @PublicEndpoint
    @Operation(summary = "Request password reset")
    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse<String>> resetPassword(
            @RequestBody AuthDTO.ResetPasswordRequest request
    ) {
        User user = userService.getUserByEmail(request.email());
        if (user == null) throw new NotFoundException("Email not found");

        PasswordResetToken token = userService.createPasswordResetToken(user);

        emailSenderService.sendEmail(user.getEmail(), "Reset your password", "Your reset code is: " + token.getToken());

        return GenericResponse.ok("Password reset code sent to email");
    }

    // ==================== CHANGE PASSWORD ====================
    @PublicEndpoint
    @Operation(summary = "Change password with token")
    @PostMapping("/change-password")
    public ResponseEntity<GenericResponse<String>> changePassword(
            @RequestParam("code") String code,
            @RequestBody AuthDTO.ChangePasswordRequest request
    ) {
        PasswordResetToken token = userService.getPasswordResetToken(code);
        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Invalid or expired code");

        User user = token.getUser();
        user.setPasswordHash(userService.encodePassword(request.password()));
        userService.saveUser(user);
        userService.deletePasswordResetToken(token);

        return GenericResponse.ok("Password changed successfully");
    }
    private void sendMail(String email, String subject, String body) {
        emailSenderService.sendEmail(email, subject, body);
    }
    private void sendMailToAdmin(String fromEmail, String subject, String body) {
        sendMail(adminEmail, subject, "Message from: " + fromEmail + "\n\n" + body);
    }
}