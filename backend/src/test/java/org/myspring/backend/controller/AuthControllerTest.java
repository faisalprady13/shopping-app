package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_createsLocalUserWithHashedPasswordAndReturnsSafeUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Jane Doe",
                                  "email": "jane@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.authProvider").value("LOCAL"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        User savedUser = userRepo.findByEmail("jane@example.com").orElseThrow();

        assertNotEquals("password123", savedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPasswordHash()));
    }

    @Test
    void login_returnsSafeUserForValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "John Doe",
                          "email": "john@example.com",
                          "password": "password123"
                        }
                        """));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "john@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void login_rejectsInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "missing@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}
