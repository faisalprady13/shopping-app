package org.myspring.backend.controller;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.UserDTO;
import org.myspring.backend.model.User;
import org.myspring.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public User getOrCreateUser(@RequestBody UserDTO userDto) {
        User user = userService.getUserByName(userDto.name());
        if (user == null) {
            return userService.createUser(userDto);
        }
        return user;
    }
}
