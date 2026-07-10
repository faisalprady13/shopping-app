package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.model.Role;
import org.myspring.backend.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void lists_requireAuthentication() throws Exception {
        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoints_rejectUserRole() throws Exception {
        mockMvc.perform(get("/api/admin/users").with(authentication(authenticatedUser(Role.USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoints_allowAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/users").with(authentication(authenticatedUser(Role.ADMIN))))
                .andExpect(status().isOk());
    }

    private UsernamePasswordAuthenticationToken authenticatedUser(Role role) {
        AuthenticatedUser principal = new AuthenticatedUser("6", "Max", "max@example.com", role);
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }
}
