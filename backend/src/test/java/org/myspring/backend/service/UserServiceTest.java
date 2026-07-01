package org.myspring.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @Test
    void getUserByName() {
        User user = User.builder().name("john doe").build();

        when(userRepo.findByName("john doe")).thenReturn(user);

        UserService userService = new UserService(userRepo);

        assertEquals(user, userService.getUserByName("john doe"));
    }

    @Test
    void createUser() {
    }
}