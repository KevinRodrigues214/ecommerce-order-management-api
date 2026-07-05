package com.kevin.managementsystem.service;

import com.kevin.managementsystem.domain.User;
import com.kevin.managementsystem.dto.UserRequestDTO;
import com.kevin.managementsystem.dto.UserResponseDTO;
import com.kevin.managementsystem.exception.BusinessException;
import com.kevin.managementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email já cadastrado: " + dto.email());
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password()) // por enquanto sem hash, na Etapa 3 a gente criptografa
                .role(User.Role.CUSTOMER)
                .build();

        User saved = userRepository.save(user);
        return UserResponseDTO.fromEntity(saved);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado: " + id));
        return UserResponseDTO.fromEntity(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("Usuário não encontrado: " + id);
        }
        userRepository.deleteById(id);
    }
}