package org.myspring.backend.service;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.LoginDTO;
import org.myspring.backend.dto.RegisterDTO;
import org.myspring.backend.dto.SafeUserDTO;
import org.myspring.backend.model.AuthProvider;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final IdService idService;
    private final PasswordEncoder passwordEncoder;

    public SafeUserDTO register(RegisterDTO registerDto) {
        String email = normalizeEmail(registerDto.email());
        validateRegistration(registerDto, email);

        userRepo.findByEmail(email).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        });

        User user = new User(
                idService.generateId(),
                registerDto.name().trim(),
                email,
                passwordEncoder.encode(registerDto.password()),
                AuthProvider.LOCAL,
                null,
                new ArrayList<>()
        );

        return SafeUserDTO.from(userRepo.save(user));
    }

    public SafeUserDTO login(LoginDTO loginDto) {
        String email = normalizeEmail(loginDto.email());
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (user.getPasswordHash() == null || !passwordEncoder.matches(loginDto.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return SafeUserDTO.from(user);
    }

    private void validateRegistration(RegisterDTO registerDto, String email) {
        if (registerDto.name() == null || registerDto.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (registerDto.password() == null || registerDto.password().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
