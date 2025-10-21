package com.example.todo_app.user.services.impl;
import com.example.todo_app.common.domains.IamUserDetails;
import com.example.todo_app.common.domains.Permission;
import com.example.todo_app.common.domains.Role;
import com.example.todo_app.common.domains.Token;
import com.example.todo_app.common.enums.UserType;
import com.example.todo_app.common.models.ConfirmationToken;
import com.example.todo_app.common.models.PasswordResetToken;
import com.example.todo_app.common.repositories.ConfirmationTokenRepository;
import com.example.todo_app.common.repositories.PasswordResetTokenRepository;
import com.example.todo_app.common.repositories.RoleRepository;
import com.example.todo_app.common.repositories.TokenRepository;
import com.example.todo_app.common.utils.JwtUtil;
import com.example.todo_app.user.models.User;
import com.example.todo_app.user.repositories.UserRepository;
import com.example.todo_app.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
//    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenRepository tokenRepository;

    // -------------------- USER CHECKS --------------------
    @Override
    public Boolean hasUserWithUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // -------------------- LOAD USER --------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return IamUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .roles(roles)
                .permissions(permissions)
                .isActive(user.getEnabled())
                .build();
    }

    // -------------------- REGISTER USER --------------------
    @Override
    public User registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));
        user.setEnabled(false);

        Role customerRole = roleRepository.findByName(UserType.USER.name())
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
        user.setRoles(Set.of(customerRole));

        return userRepository.save(user);
    }

    // -------------------- LOGIN --------------------
    @Override
    public String verify(String email, String rawPassword) {
        try {
            // Manual authentication instead of using AuthenticationManager
            User user = getUserByEmail(email);
            if (user == null) {
                return "Failed";
            }
            // Check if password matches
            if (!bCryptPasswordEncoder.matches(rawPassword, user.getPasswordHash())) {
                return "Failed";
            }
            // Check if user is enabled
            if (!user.getEnabled()) {
                return "Failed - Email not confirmed";
            }
            // Generate token
            return jwtUtil.generateToken(mapToIamUserDetails(user));

        } catch (Exception e) {
            return "Failed";
        }
//        try {
//            Authentication auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(email, rawPassword)
//            );
//
//            if (auth.isAuthenticated()) {
//                User user = getUserByEmail(email);
//                return jwtUtil.generateToken(mapToIamUserDetails(user));
//            }
//        } catch (AuthenticationException ex) {
//            return "Failed";
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return "Failed";
    }

    @Override
    public void invalidateToken(String token) {
        // Store token in a blacklist table
        Token blacklistedToken = new Token();
        blacklistedToken.setToken(token);
        blacklistedToken.setUserId(extractUserIdFromToken(token));
        blacklistedToken.setRevokedAt(LocalDateTime.now());
        tokenRepository.save(blacklistedToken);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    // -------------------- CONFIRMATION TOKEN --------------------
    @Override
    public ConfirmationToken createConfirmationToken(User user) {
        ConfirmationToken token = new ConfirmationToken();
        token.setUser(user);
        token.setToken(generateRandomCode());
        token.setCreatedDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        return confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getConfirmationToken(String code) {
        return confirmationTokenRepository.findByToken(code);
    }

    @Override
    public void deleteConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    // -------------------- PASSWORD RESET TOKEN --------------------
    @Override
    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(generateRandomCode());
        token.setCreatedDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        return passwordResetTokenRepository.save(token);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String code) {
        return passwordResetTokenRepository.findByToken(code);
    }

    @Override
    public void deletePasswordResetToken(PasswordResetToken token) {
        passwordResetTokenRepository.delete(token);
    }


    // -------------------- HELPER --------------------

    private IamUserDetails mapToIamUserDetails(User user) {
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return IamUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .roles(roles)
                .permissions(permissions)
                .isActive(user.getEnabled())
                .build();
    }

    private String generateRandomCode() {
        return String.valueOf(100000 + new Random().nextInt(900000)); // 6-digit numeric code
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }


    // Helper to get userId from token
    private String extractUserIdFromToken(String token) {
        String email = jwtUtil.extractUsername(token);
        User user = getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    @Override
    public User getUserFromToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }
        String token = bearerToken.substring(7); // remove "Bearer " prefix
        String username = jwtUtil.extractUsername(token);
        if (username == null) return null;

        Optional<User> optionalUser = userRepository.findByEmail(username);
        return optionalUser.orElse(null);
    }
}
