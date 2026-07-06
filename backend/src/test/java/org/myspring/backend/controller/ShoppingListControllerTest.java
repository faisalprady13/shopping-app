package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.dto.ProductDTO;
import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ProductStatus;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        List<Product> products = Collections.emptyList();
        ShoppingList shoppingList = new ShoppingList("1", "Test",
                date, user, products);
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = "[" + mapper.writeValueAsString(shoppingList) + "]";

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void createList_shouldReturnShoppingList_whenCreated() throws Exception {
        User user = new User("6", "Max", null);
        ShoppingListDTO shoppingList = new ShoppingListDTO("Test", user);
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(shoppingList);

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
        String id = "1";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        List<Product> products = Collections.emptyList();
        ShoppingList shoppingList = new ShoppingList(id, "Test",
                date, user, products);
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(shoppingList);

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(get("/api/lists/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getListById_shouldThrowException_whenNotFound() throws Exception {
        String id = "0";
        String errorMessage = "Die Einkaufsliste wurde nicht gefunden: ";

        errorMessage += "List with id 0 not found!";
        mvc.perform(get("/api/lists/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void addProduct_shouldReturnShoppingListWithProduct_whenAdded() throws Exception {
        String listId = "1";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                date, user, new ArrayList<>());
        ProductDTO productDto = new ProductDTO(null, "Milk", 2, ProductStatus.OPEN, listId);
        ObjectMapper mapper = new ObjectMapper();

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(post("/api/lists/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].name").value("Milk"))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[0].status").value("OPEN"))
                .andExpect(jsonPath("$.products[0].id").isNotEmpty());
    }

    @Test
    void addProduct_shouldThrowException_whenListNotFound() throws Exception {
        String listId = "0";
        String errorMessage = "Die Einkaufsliste wurde nicht gefunden: List with id 0 not found!";
        ProductDTO productDto = new ProductDTO(null, "Milk", 2, ProductStatus.OPEN, listId);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(post("/api/lists/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void updateProduct_shouldReturnShoppingListWithUpdatedProduct_whenUpdated() throws Exception {
        String listId = "1";
        String productId = "10";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        Product product = new Product(productId, "Milk", 2, ProductStatus.OPEN, null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                date, user, new ArrayList<>(List.of(product)));
        product.setShoppingList(shoppingList);
        ProductDTO productDto = new ProductDTO(productId, "Butter", 5, ProductStatus.CLOSED, listId);
        ObjectMapper mapper = new ObjectMapper();

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(put("/api/lists/update-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].name").value("Butter"))
                .andExpect(jsonPath("$.products[0].quantity").value(5))
                .andExpect(jsonPath("$.products[0].status").value("CLOSED"));
    }

    @Test
    void updateProduct_shouldThrowException_whenProductNotFound() throws Exception {
        String listId = "1";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                date, user, new ArrayList<>());
        ProductDTO productDto = new ProductDTO("99", "Butter", 5, ProductStatus.CLOSED, listId);
        String errorMessage = "Das Produkt wurde nicht gefunden: Product 99 not found in shopping list " + listId;
        ObjectMapper mapper = new ObjectMapper();

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(put("/api/lists/update-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void removeProduct_shouldReturnShoppingListWithoutProduct_whenRemoved() throws Exception {
        String listId = "1";
        String productId = "10";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("6", "Max", null);
        Product product = new Product(productId, "Milk", 2, ProductStatus.OPEN, null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                date, user, new ArrayList<>(List.of(product)));
        product.setShoppingList(shoppingList);
        ProductDTO productDto = new ProductDTO(productId, null, null, null, listId);
        ObjectMapper mapper = new ObjectMapper();

        userRepo.save(user);
        listRepo.save(shoppingList);
        mvc.perform(delete("/api/lists/remove-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    @Test
    void removeProduct_shouldThrowException_whenListNotFound() throws Exception {
        String listId = "0";
        String errorMessage = "Die Einkaufsliste wurde nicht gefunden: List with id 0 not found!";
        ProductDTO productDto = new ProductDTO("10", null, null, null, listId);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(delete("/api/lists/remove-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }
}