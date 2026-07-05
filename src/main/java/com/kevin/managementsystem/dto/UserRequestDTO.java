package com.kevin.managementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password
) {}