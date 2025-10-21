package com.example.todo_app.user.services;

import com.example.todo_app.common.models.ConfirmationToken;
import com.example.todo_app.common.models.PasswordResetToken;
import com.example.todo_app.user.dtos.*;
import com.example.todo_app.user.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    // -------------------- USER CHECKS --------------------
    Boolean hasUserWithUsername(String username);

    Boolean hasUserWithEmail(String email);

    // -------------------- LOAD USER --------------------
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    // -------------------- REGISTER USER --------------------
    User registerUser(String username, String email, String password);

    // -------------------- LOGIN --------------------
    String verify(String email, String rawPassword);

    void invalidateToken(String token);

    User getUserByEmail(String email);

    User getUserById(String id);

    void saveUser(User user);

    String encodePassword(String rawPassword);

    // -------------------- CONFIRMATION TOKEN --------------------
    ConfirmationToken createConfirmationToken(User user);

    ConfirmationToken getConfirmationToken(String code);

    void deleteConfirmationToken(ConfirmationToken token);

    // -------------------- PASSWORD RESET TOKEN --------------------
    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken getPasswordResetToken(String code);

    void deletePasswordResetToken(PasswordResetToken token);

    String extractUsernameFromToken(String token);

    User getUserByUsername(String username);

    User getUserFromToken(String bearerToken);
}
