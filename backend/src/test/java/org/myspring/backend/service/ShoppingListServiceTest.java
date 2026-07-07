package org.myspring.backend.service;

import org.myspring.backend.dto.ProductDTO;
import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.ListIdNotFound;
import org.myspring.backend.exception.ProductNotFound;
import org.myspring.backend.exception.UserIdNotFound;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ProductStatus;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ListRepo;
import org.junit.jupiter.api.Test;
import org.myspring.backend.repository.ProductRepo;
import org.myspring.backend.repository.UserRepo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Test
    void getLists_shouldReturnListOfShoppingList_whenDatabaseNotEmpty() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);
        Instant date = Instant.now();
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList("1", "Test",
                date, user, null);
        List<ShoppingList> expected = new ArrayList<>(List.of(shoppingList));
        List<ShoppingList> actual;

        when(mockListRepo.findAll()).thenReturn(expected);
        actual = service.getLists();
        assertEquals(expected, actual);
    }

    @Test
    void getLists_shouldReturnEmptyList_whenDatabaseEmpty() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        List<ShoppingList> expected = Collections.emptyList();
        List<ShoppingList> actual;

        actual = service.getLists();
        assertEquals(expected, actual);
    }

    @Test
    void saveList_shouldReturnShoppingList_whenSaved() throws UserIdNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String uid = "6";
        String id = "1";
        String name = "Test";
        Instant date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User(uid, "Max", null);
        List<Product> products = Collections.emptyList();
        ShoppingListDTO shopList = new ShoppingListDTO(name, user);
        ShoppingList expected = new ShoppingList(id, name,
                date, user, products);
        ShoppingList actual;

        when(mockingIdService.generateId()).thenReturn(id);
        when(mockUserRepo.findUserById(uid)).thenReturn(user);
        when(mockListRepo.save(expected)).thenReturn(expected);
        actual = service.saveList(shopList);
        assertEquals(expected, actual);
    }

    @Test
    void saveList_shouldThrowException_whenUserNotFound() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String uid = "6";
        String name = "Test";
        User user = new User(uid, "Max", null);
        ShoppingListDTO shopList = new ShoppingListDTO(name, user);

        assertThatExceptionOfType(UserIdNotFound.class)
                .isThrownBy(() -> service.saveList(shopList))
                .withMessage("User with id 6 not found!");
    }

    @Test
    void getListById_shouldReturnShoppingList_whenFoundInDatabase() throws ListIdNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String id = "1";
        Instant date = Instant.now();
        User user = new User("6", "Max", null);
        ShoppingList expected = new ShoppingList(id, "Test",
                date, user, null);
        ShoppingList actual;

        when(mockListRepo.findById(id)).thenReturn(Optional.of(expected));
        actual = service.getListById(id);
        assertEquals(expected, actual);
    }

    @Test
    void getListById_shouldThrowException_whenNotFoundInDatabase() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String id = "0";

        assertThatExceptionOfType(ListIdNotFound.class)
                .isThrownBy(() -> service.getListById(id))
                .withMessage("List with id 0 not found!");
    }

    @Test
    void addProductToShoppingList_shouldAddProductToList_whenListFound() throws ListIdNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "1";
        String productId = "10";
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                Instant.now().truncatedTo(ChronoUnit.SECONDS), user, new ArrayList<>());
        ProductDTO productDto = new ProductDTO(null, "Milk", 2, ProductStatus.OPEN, listId);
        ShoppingList actual;

        when(mockListRepo.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(mockingIdService.generateId()).thenReturn(productId);
        actual = service.addProductToShoppingList(productDto);

        assertEquals(1, actual.getProducts().size());
        Product addedProduct = actual.getProducts().get(0);
        assertEquals(productId, addedProduct.getId());
        assertEquals("Milk", addedProduct.getName());
        assertEquals(2, addedProduct.getQuantity());
        assertEquals(ProductStatus.OPEN, addedProduct.getStatus());
        assertEquals(shoppingList, addedProduct.getShoppingList());
        verify(mockProductRepo).save(addedProduct);
        verify(mockListRepo).save(shoppingList);
    }

    @Test
    void addProductToShoppingList_shouldThrowException_whenListNotFound() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "0";
        ProductDTO productDto = new ProductDTO(null, "Milk", 2, ProductStatus.OPEN, listId);

        assertThatExceptionOfType(ListIdNotFound.class)
                .isThrownBy(() -> service.addProductToShoppingList(productDto))
                .withMessage("List with id 0 not found!");
    }

    @Test
    void removeProductFromShoppingList_shouldRemoveProduct_whenFound() throws ListIdNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "1";
        String productId = "10";
        User user = new User("6", "Max", null);
        Product product = new Product(productId, "Milk", 2, ProductStatus.OPEN, null);
        List<Product> products = new ArrayList<>(List.of(product));
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                Instant.now().truncatedTo(ChronoUnit.SECONDS), user, products);
        product.setShoppingList(shoppingList);
        ProductDTO productDto = new ProductDTO(productId, null, null, null, listId);
        ShoppingList actual;

        when(mockListRepo.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(mockProductRepo.findById(productId)).thenReturn(Optional.of(product));
        actual = service.removeProductFromShoppingList(productDto);

        assertTrue(actual.getProducts().isEmpty());
        assertNull(product.getShoppingList());
        verify(mockListRepo).save(shoppingList);
    }

    @Test
    void removeProductFromShoppingList_shouldThrowException_whenListNotFound() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "0";
        ProductDTO productDto = new ProductDTO("10", null, null, null, listId);

        assertThatExceptionOfType(ListIdNotFound.class)
                .isThrownBy(() -> service.removeProductFromShoppingList(productDto))
                .withMessage("List with id 0 not found!");
    }

    @Test
    void removeProductFromShoppingList_shouldThrowException_whenProductNotFound() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "1";
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                Instant.now().truncatedTo(ChronoUnit.SECONDS), user, new ArrayList<>());
        ProductDTO productDto = new ProductDTO("99", null, null, null, listId);

        when(mockListRepo.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(mockProductRepo.findById("99")).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> service.removeProductFromShoppingList(productDto));
    }

    @Test
    void updateProductInShoppingList_shouldUpdateProduct_whenFound() throws ListIdNotFound, ProductNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "1";
        String productId = "10";
        User user = new User("6", "Max", null);
        Product product = new Product(productId, "Milk", 2, ProductStatus.OPEN, null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                Instant.now().truncatedTo(ChronoUnit.SECONDS), user, new ArrayList<>(List.of(product)));
        product.setShoppingList(shoppingList);
        ProductDTO productDto = new ProductDTO(productId, "Butter", 5, ProductStatus.CLOSED, listId);
        ShoppingList actual;

        when(mockListRepo.findById(listId)).thenReturn(Optional.of(shoppingList));
        actual = service.updateProductInShoppingList(productDto);

        Product updatedProduct = actual.getProducts().get(0);
        assertEquals("Butter", updatedProduct.getName());
        assertEquals(5, updatedProduct.getQuantity());
        assertEquals(ProductStatus.CLOSED, updatedProduct.getStatus());
        verify(mockProductRepo).save(product);
    }

    @Test
    void updateProductInShoppingList_shouldThrowException_whenListNotFound() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "0";
        ProductDTO productDto = new ProductDTO("10", "Butter", 5, ProductStatus.CLOSED, listId);

        assertThatExceptionOfType(ListIdNotFound.class)
                .isThrownBy(() -> service.updateProductInShoppingList(productDto))
                .withMessage("List with id 0 not found!");
    }

    @Test
    void updateProductInShoppingList_shouldThrowException_whenProductNotInList() {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);

        String listId = "1";
        User user = new User("6", "Max", null);
        ShoppingList shoppingList = new ShoppingList(listId, "Test",
                Instant.now().truncatedTo(ChronoUnit.SECONDS), user, new ArrayList<>());
        ProductDTO productDto = new ProductDTO("99", "Butter", 5, ProductStatus.CLOSED, listId);

        when(mockListRepo.findById(listId)).thenReturn(Optional.of(shoppingList));

        assertThatExceptionOfType(ProductNotFound.class)
                .isThrownBy(() -> service.updateProductInShoppingList(productDto))
                .withMessage("Product 99 not found in shopping list 1");
    }

    @Test
    void getListsByUserId_shouldReturnListOfShoppingList_whenUserInDatabase() throws UserIdNotFound {
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);
        String userId= "6";
        Instant date = Instant.now();
        User user = new User(userId, "Max", null);
        ShoppingList shoppingList = new ShoppingList("1", "Test",
                date, user, null);
        List<ShoppingList> expected = new ArrayList<>(List.of(shoppingList));
        List<ShoppingList> actual;

        when(mockListRepo.findShoppingListsByUser_Id(userId)).thenReturn(expected);
        actual = service.getListsByUserId(userId);
        assertEquals(expected, actual);
    }

    @Test
    void getListsByUserId_shouldReturnEmptyList_whenUserNotDatabase(){
        ListRepo mockListRepo = mock(ListRepo.class);
        IdService mockingIdService = mock(IdService.class);
        UserRepo mockUserRepo = mock(UserRepo.class);
        ProductRepo mockProductRepo = mock(ProductRepo.class);
        ShoppingListService service = new ShoppingListService(mockListRepo, mockingIdService, mockUserRepo, mockProductRepo);
        String userId= "0";
        List<ShoppingList> expected = Collections.emptyList();
        List<ShoppingList> actual;

        when(mockListRepo.findShoppingListsByUser_Id(userId)).thenReturn(expected);
        actual = service.getListsByUserId(userId);
        assertEquals(expected, actual);
    }
}