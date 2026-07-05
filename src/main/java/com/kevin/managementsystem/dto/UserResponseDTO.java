package com.kevin.managementsystem.dto;

import com.kevin.managementsystem.domain.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}