package com.puente.app.application;

import com.puente.app.domain.user.Role;
import com.puente.app.domain.user.RoleType;
import com.puente.app.domain.user.User;
import com.puente.app.infrastructure.persistence.springdata.RoleJpaRepository;
import com.puente.app.infrastructure.persistence.springdata.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private static final String USER_NOT_FOUND = "User not found";
    
    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public UserService(UserJpaRepository userRepository, RoleJpaRepository roleRepository, PasswordEncoder passwordEncoder, AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    public User register(String name, String email, String plainPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User(name, email, passwordEncoder.encode(plainPassword));
        
        Role userRole = roleRepository.findByName(RoleType.USER)
            .orElseThrow(() -> new IllegalStateException("USER role not found in database"));
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);
        auditService.log("USER_REGISTERED", email, "New user registered: " + name);
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateProfile(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setName(name);
        user.setEmail(email);
        user.touch();
        
        User updated = userRepository.save(user);
        auditService.log("USER_UPDATED", email, "User profile updated");
        return updated;
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.touch();
        userRepository.save(user);
        auditService.log("PASSWORD_CHANGED", user.getEmail(), "User password changed");
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        
        String email = user.getEmail();
        userRepository.delete(user);
        auditService.log("USER_DELETED", email, "User deleted by admin");
    }

    public User promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        
        Role adminRole = roleRepository.findByName(RoleType.ADMIN)
            .orElseThrow(() -> new IllegalStateException("ADMIN role not found in database"));
        
        user.getRoles().add(adminRole);
        user.touch();
        User updated = userRepository.save(user);
        auditService.log("USER_PROMOTED", user.getEmail(), "User promoted to ADMIN");
        return updated;
    }
}

