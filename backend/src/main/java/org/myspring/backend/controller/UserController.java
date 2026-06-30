package org.myspring.backend.controller;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.UserDto;
import org.myspring.backend.model.User;
import org.myspring.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public User getOrCreateUser(@RequestBody UserDto userDto) {
        User user = userService.getUserByName(userDto.name());
        if (user == null) {
            return userService.createUser(userDto);
        }
        return user;
    }
}
