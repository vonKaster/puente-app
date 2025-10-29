package com.puente.app.api.mapper;

import com.puente.app.api.dto.UserResponse;
import com.puente.app.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}

