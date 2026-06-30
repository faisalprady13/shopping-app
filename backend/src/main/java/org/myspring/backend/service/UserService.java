package org.myspring.backend.service;


import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.UserDto;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User getUserByName(String name) {
        return userRepo.findByName(name);
    }

    public User createUser(UserDto userDto) {
        return userRepo.save(new User(userDto));
    }
}
