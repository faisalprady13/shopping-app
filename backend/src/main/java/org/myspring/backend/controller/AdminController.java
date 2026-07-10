package org.myspring.backend.controller;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.RoleUpdateDTO;
import org.myspring.backend.dto.SafeUserDTO;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.myspring.backend.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepo userRepo;
    private final ShoppingListService shoppingListService;

    @GetMapping("/users")
    public List<SafeUserDTO> getUsers() {
        return userRepo.findAll().stream()
                .map(SafeUserDTO::from)
                .toList();
    }

    @GetMapping("/lists")
    public List<ShoppingList> getLists() {
        return shoppingListService.getLists();
    }

    @PutMapping("/users/{id}/role")
    public SafeUserDTO updateRole(@PathVariable String id, @RequestBody RoleUpdateDTO roleUpdateDTO) {
        if (roleUpdateDTO.role() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(roleUpdateDTO.role());

        return SafeUserDTO.from(userRepo.save(user));
    }
}
