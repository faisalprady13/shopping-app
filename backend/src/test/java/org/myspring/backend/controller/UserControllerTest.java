package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Test
    void postUser_createsNewUser_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"shoppingLists\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("john doe"));
    }

    @Test
    void postUser_returnsExistingUser_whenUserAlreadyExists() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"jane doe\",\"shoppingLists\":null}"));

        long countBefore = userRepo.count();

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"jane doe\",\"shoppingLists\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("jane doe"));

        // second call must not create a duplicate
        assert userRepo.count() == countBefore;
    }
}