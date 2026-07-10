package org.myspring.backend.controller;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.LoginDTO;
import org.myspring.backend.dto.RegisterDTO;
import org.myspring.backend.dto.SafeUserDTO;
import org.myspring.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SafeUserDTO register(@RequestBody RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public SafeUserDTO login(@RequestBody LoginDTO loginDto) {
        return authService.login(loginDto);
    }
}
