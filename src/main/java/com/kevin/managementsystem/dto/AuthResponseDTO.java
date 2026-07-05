package com.kevin.managementsystem.dto;

public record AuthResponseDTO(
        String token,
        String name,
        String email,
        String role
) {}