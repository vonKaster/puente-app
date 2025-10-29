package com.puente.app.application;

import com.puente.app.domain.user.User;
import com.puente.app.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuditService auditService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, 
                      JwtTokenProvider jwtTokenProvider, AuditService auditService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditService = auditService;
    }

    public String login(String email, String plainPassword) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(plainPassword, user.getPasswordHash())) {
            auditService.log("LOGIN_FAILED", email, "Invalid password attempt");
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtTokenProvider.createToken(email, user.getRoles());
        auditService.log("LOGIN_SUCCESS", email, "User logged in successfully");
        return token;
    }

    public User register(String name, String email, String plainPassword) {
        return userService.register(name, email, plainPassword);
    }

    public User getUserByEmail(String email) {
        return userService.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}

