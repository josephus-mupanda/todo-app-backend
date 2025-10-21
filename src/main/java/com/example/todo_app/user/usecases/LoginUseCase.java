package com.example.todo_app.user.usecases;

import com.example.todo_app.common.domains.IamUserDetails;
import com.example.todo_app.common.domains.Token;
import com.example.todo_app.common.exceptions.UnauthorizedException;
import com.example.todo_app.common.interfaces.IamUserDetailsService;
import com.example.todo_app.common.utils.JwtUtil;
import com.example.todo_app.user.dtos.LoginDTO;
import com.example.todo_app.user.repositories.TokenRepository;
import com.example.todo_app.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;
    private final TokenRepository tokenRepository;
    private final IamUserDetailsService iamUserDetailsService;
    private final UserRepository userRepository;

    /**
     * Executes the login flow:
     * - Finds user by email
     * - Validates password
     * - Generates JWT token
     * - Clears old tokens and saves the new one
     */
    @Transactional
    public LoginDTO.Output execute(LoginDTO.Input loginRequestDTO) {
        IamUserDetails user = findVerifiedUser(loginRequestDTO.email());
        validatePassword(loginRequestDTO.password(), user);
        String token = generateToken(user, loginRequestDTO.email());
        return new LoginDTO.Output(token);
    }

    private void validatePassword(String inputPassword, IamUserDetails user) {
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    private String generateToken(IamUserDetails user, String identifier) {
        String jwtToken = jwtService.generateToken(user);
        deleteAllUserTokens(user.getId());
        saveUserToken(user, jwtToken, identifier);
        return jwtToken;
    }

    private IamUserDetails findVerifiedUser(String email) {
        IamUserDetails user = iamUserDetailsService.findUserByUsername(email);
        if (user == null || !user.getIsActive()) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return user;
    }

    private void deleteAllUserTokens(String userId) {
        tokenRepository.deleteAllTokensByUserId(userId);
        logger.debug("Deleted all tokens for user with ID {}", userId);
    }

    private void saveUserToken(IamUserDetails user, String jwtToken, String username) {
        Token token = new Token();
        token.setUserId(user.getId());
        token.setToken(jwtToken);
        tokenRepository.save(token);
    }
}

