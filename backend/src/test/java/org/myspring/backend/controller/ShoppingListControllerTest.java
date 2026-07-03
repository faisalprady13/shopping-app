package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ListRepo;
import org.myspring.backend.repository.ProductRepo;
import org.myspring.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShoppingListControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ListRepo listRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;

    @Test
    void getAllLists_shouldReturnEmptyJson_whenInitiallyStarted() throws Exception {
        mvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllLists_shouldReturnJsonList_whenCalled() throws Exception {
        Instant date= Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user= new User("6", "Max", null);
        List<Product> products= Collections.emptyList();
        ShoppingList shoppingList= new ShoppingList("1", "Test",
                                                        date, user, products);
        ObjectMapper mapper= new ObjectMapper();
        String jsonList= "[" + mapper.writeValueAsString(shoppingList) + "]";

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void createList_shouldReturnShoppingList_whenCreated() throws Exception {
        User user= new User("6", "Max", null);
        ShoppingListDTO shoppingList= new ShoppingListDTO("Test", user);
        ObjectMapper mapper= new ObjectMapper();
        String jsonList= mapper.writeValueAsString(shoppingList);

        userRepo.save(user);
        mvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonList))
                    .andExpect(status().isCreated())
                    .andExpect(content().json("""
                        {
                          name: "Test"
                        }
                    """))
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    void getListById_shouldReturnShoppingList_whenFound() throws Exception {
        String id= "1";
        Instant date= Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user= new User("6", "Max", null);
        List<Product> products= Collections.emptyList();
        ShoppingList shoppingList= new ShoppingList(id, "Test",
                                                    date, user, products);
        ObjectMapper mapper= new ObjectMapper();
        String jsonList= mapper.writeValueAsString(shoppingList);

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(get("/api/lists/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getListById_shouldThrowException_whenNotFound() throws Exception {
        String id= "0";
        String errorMessage= "Die Einkaufsliste wurde nicht gefunden: ";

        errorMessage+= "List with id 0 not found!";
        mvc.perform(get("/api/lists/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }
}