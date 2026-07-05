package com.kevin.managementsystem.controller;


import com.kevin.managementsystem.dto.UserRequestDTO;
import com.kevin.managementsystem.dto.UserResponseDTO;
import com.kevin.managementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/users")
    @RequiredArgsConstructor
    public class UserController {

        private final UserService userService;

        @PostMapping
        public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
        }

        @GetMapping
        public ResponseEntity<List<UserResponseDTO>> findAll() {
            return ResponseEntity.ok(userService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
            return ResponseEntity.ok(userService.findById(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }