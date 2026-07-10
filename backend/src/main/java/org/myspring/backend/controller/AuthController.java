package org.myspring.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.LoginDTO;
import org.myspring.backend.dto.RegisterDTO;
import org.myspring.backend.dto.SafeUserDTO;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.myspring.backend.security.AuthenticatedUser;
import org.myspring.backend.service.AuthService;
import org.myspring.backend.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SafeUserDTO register(@RequestBody RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public SafeUserDTO login(@RequestBody LoginDTO loginDto, HttpServletRequest request, HttpServletResponse response) {
        User user = authService.validateLogin(loginDto);
        sessionService.login(user, request, response);
        return SafeUserDTO.from(user);
    }

    @GetMapping("/me")
    public SafeUserDTO me(@AuthenticationPrincipal AuthenticatedUser principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        User user = userRepo.findById(principal.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated"));
        return SafeUserDTO.from(user);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        sessionService.logout(request, response);
    }
}
