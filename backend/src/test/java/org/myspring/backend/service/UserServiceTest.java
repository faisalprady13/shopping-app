package org.myspring.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myspring.backend.dto.UserDto;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @Test
    void getUserByName() {
        IdService mockingIdService = mock(IdService.class);
        String id = mockingIdService.generateId();
        User user = User.builder().id(id).name("john doe").build();

        when(userRepo.findByName("john doe")).thenReturn(user);

        UserService userService = new UserService(userRepo, mockingIdService);

        assertEquals(user, userService.getUserByName("john doe"));
    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto("jane doe", null);
        IdService mockingIdService = mock(IdService.class);
        String id = mockingIdService.generateId();
        User savedUser = User.builder().id(id).name("jane doe").build();

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        UserService userService = new UserService(userRepo, mockingIdService);
        User result = userService.createUser(userDto);

        assertEquals(savedUser, result);
        assertEquals("jane doe", result.getName());
    }
}