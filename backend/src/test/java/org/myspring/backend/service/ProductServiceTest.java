package org.myspring.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myspring.backend.dto.ProductDto;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ProductStatus;
import org.myspring.backend.repository.ProductRepo;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;

    @Test
    void getProductsByShoppingListId() {
        Product product = new Product(UUID.randomUUID(), "milk", 2, ProductStatus.OPEN, null);

        when(productRepo.findProductsByShoppingList_Id("list-1")).thenReturn(List.of(product));

        ProductService productService = new ProductService(productRepo);
        List<Product> result = productService.getProductsByShoppingListId("list-1");

        assertEquals(List.of(product), result);
    }

    @Test
    void addProductToShoppingList() {
        ProductDto productDto = new ProductDto(null, "milk", 2, ProductStatus.OPEN, null);

        ProductService productService = new ProductService(productRepo);
        productService.addProductToShoppingList(productDto);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepo).save(productCaptor.capture());


        Product savedProduct = productCaptor.getValue();
        assertEquals("milk", savedProduct.getName());
        assertEquals(2, savedProduct.getQuantity());
        assertEquals(ProductStatus.OPEN, savedProduct.getStatus());
    }

    @Test
    void updateProduct() {
        UUID productId = UUID.randomUUID();
        Product oldProduct = new Product(productId, "milk", 2, ProductStatus.OPEN, null);
        ProductDto updateDto = new ProductDto(productId, "bread", 3, ProductStatus.CLOSED, null);

        when(productRepo.findProductById(productId)).thenReturn(oldProduct);

        ProductService productService = new ProductService(productRepo);
        productService.updateProduct(updateDto);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepo).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals(productId, savedProduct.getId());
        assertEquals("bread", savedProduct.getName());
        assertEquals(3, savedProduct.getQuantity());
        assertEquals(ProductStatus.CLOSED, savedProduct.getStatus());
    }

    @Test
    void deleteProduct() {
        UUID productId = UUID.randomUUID();

        ProductService productService = new ProductService(productRepo);
        productService.deleteProduct(productId);

        verify(productRepo).deleteById(productId);
    }
}