package org.myspring.backend.controller;

import org.junit.jupiter.api.Test;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ProductStatus;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.repository.ListRepo;
import org.myspring.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ListRepo listRepo;

//    @Test
//    void getProductsByShoppingListId_returnsProducts_whenListHasProducts() throws Exception {
//        ShoppingList shoppingList = new ShoppingList("list-1", "groceries", null, null, null);
//        listRepo.save(shoppingList);
//        productRepo.save(new Product(null, "milk", 2, ProductStatus.OPEN, shoppingList));
//
//        mockMvc.perform(get("/api/product/list/list-1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].name").value("milk"));
//    }

    @Test
    void getProductsByShoppingListId_returnsEmptyList_whenListHasNoProducts() throws Exception {
        mockMvc.perform(get("/api/product/list/nonexistent-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void addProductToShoppingList_createsProduct() throws Exception {
        long countBefore = productRepo.count();

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"bread\",\"quantity\":1,\"status\":\"OPEN\",\"shoppingListId\":null}"))
                .andExpect(status().isOk());

        assertEquals(countBefore + 1, productRepo.count());
    }

    @Test
    void updateProduct_updatesExistingProduct() throws Exception {
        Product product = productRepo.save(new Product(null, "milk", 2, ProductStatus.OPEN, null));

        mockMvc.perform(put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + product.getId() + "\",\"name\":\"oat milk\",\"quantity\":5,\"status\":\"CLOSED\",\"shoppingListId\":null}"))
                .andExpect(status().isOk());

        Product updated = productRepo.findProductById(product.getId());
        assertEquals("oat milk", updated.getName());
        assertEquals(5, updated.getQuantity());
        assertEquals(ProductStatus.CLOSED, updated.getStatus());
    }

    @Test
    void deleteProduct_removesProduct() throws Exception {
        Product product = productRepo.save(new Product(null, "milk", 2, ProductStatus.OPEN, null));
        UUID productId = product.getId();

        mockMvc.perform(delete("/api/product/" + productId))
                .andExpect(status().isOk());

        assertTrue(productRepo.findById(productId).isEmpty());
    }
}