package com.kevin.managementsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.managementsystem.dto.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Disabled;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Disabled("Ambiente local: Docker Desktop retornando resposta incompleta ao Testcontainers via Java. Testcontainers funciona via curl direto; suspeita de interceptação por software de segurança/antivírus. Revisar em outra máquina/ambiente CI.")
class   OrderIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        var dto = new RegisterRequestDTO("Teste Silva", "teste@email.com", "senha123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void naoDeveRegistrarComEmailDuplicado() throws Exception {
        var dto = new RegisterRequestDTO("Teste Silva", "duplicado@email.com", "senha123");

        // primeira vez -> sucesso
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // segunda vez, mesmo email -> deve falhar
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email já cadastrado: duplicado@email.com"));
    }
}
