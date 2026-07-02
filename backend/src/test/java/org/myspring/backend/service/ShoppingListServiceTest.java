package org.myspring.backend.service;

import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.UserIdNotFound;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ListRepo;
import org.junit.jupiter.api.Test;
import org.myspring.backend.repository.UserRepo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Test
    void getLists_shouldReturnListOfShoppingList_whenDatabaseNotEmpty() {
        ListRepo mockListRepo= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        UserRepo mockUserRepo= mock(UserRepo.class);
        ShoppingListService service= new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo);
        Instant date= Instant.now();
        User user= new User("6", "Max", null);
        ShoppingList shoppingList= new ShoppingList("1", "Test",
                                                    date, user, null);
        List<ShoppingList> expected= new ArrayList<>(List.of(shoppingList));
        List<ShoppingList> actual;

        when(mockListRepo.findAll()).thenReturn(expected);
        actual= service.getLists();
        assertEquals(expected, actual);
    }

    @Test
    void getLists_shouldReturnEmptyList_whenDatabaseEmpty() {
        ListRepo mockListRepo= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        UserRepo mockUserRepo= mock(UserRepo.class);
        ShoppingListService service= new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo);
        List<ShoppingList> expected= Collections.emptyList();
        List<ShoppingList> actual;

        actual= service.getLists();
        assertEquals(expected, actual);
    }

    @Test
    void saveList_shouldReturnShoppingList_whenSaved() throws UserIdNotFound {
        ListRepo mockListRepo= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        UserRepo mockUserRepo= mock(UserRepo.class);
        ShoppingListService service= new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo);
        String uid= "6";
        String id= "1";
        String name= "Test";
        Instant date= Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user= new User(uid, "Max", null);
        ShoppingListDTO shopList= new ShoppingListDTO(name, user);
        ShoppingList expected= new ShoppingList(id, name,
                                                date, user, null);
        ShoppingList actual;

        when(mockingIdService.generateId()).thenReturn(id);
        when(mockUserRepo.findUserById(uid)).thenReturn(user);
        when(mockListRepo.save(expected)).thenReturn(expected);
        actual= service.saveList(shopList);
        assertEquals(expected, actual);
    }

    @Test
    void saveList_shouldThrowException_whenUserNotFound(){
        ListRepo mockListRepo= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        UserRepo mockUserRepo= mock(UserRepo.class);
        ShoppingListService service= new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo);
        String uid= "6";
        String name= "Test";
        User user= new User(uid, "Max", null);
        ShoppingListDTO shopList= new ShoppingListDTO(name, user);

        assertThatExceptionOfType(UserIdNotFound.class)
                .isThrownBy( () -> service.saveList(shopList) )
                .withMessage("User with id 6 not found!");
    }
}