package org.myspring.backend.service;

import org.myspring.backend.model.Product;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ListRepo;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Test
    void getLists_shouldReturnListOfShoppingList_whenDatabaseNotEmpty() {
        ListRepo mockingRepro= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        ShoppingListService service= new ShoppingListService(mockingRepro, mockingIdService);
        Instant date= Instant.now();
        User user= new User();
        Product product= new Product();
        List<Product> products= new ArrayList<>(List.of(product));
        ShoppingList shoppingList= new ShoppingList("1", "Test",
                                                    date, user);
        List<ShoppingList> expected= new ArrayList<>(List.of(shoppingList));
        List<ShoppingList> actual;

        when(mockingRepro.findAll()).thenReturn(expected);
        actual= service.getLists();
        assertEquals(expected, actual);
    }

    @Test
    void getLists_shouldReturnEmptyList_whenDatabaseEmpty() {
        ListRepo mockingRepro= mock(ListRepo.class);
        IdService mockingIdService= mock(IdService.class);
        ShoppingListService service= new ShoppingListService(mockingRepro, mockingIdService);
        List<ShoppingList> expected= Collections.emptyList();
        List<ShoppingList> actual;

        actual= service.getLists();
        assertEquals(expected, actual);
    }
}