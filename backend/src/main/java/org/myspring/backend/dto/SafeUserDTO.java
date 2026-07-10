package org.myspring.backend.dto;

import org.myspring.backend.model.AuthProvider;
import org.myspring.backend.model.Role;
import org.myspring.backend.model.User;

public record SafeUserDTO(String id, String name, String email, AuthProvider authProvider, Role role) {
    public static SafeUserDTO from(User user) {
        return new SafeUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAuthProvider(), user.getRole());
    }
}
