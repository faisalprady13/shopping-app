package org.myspring.backend.controller;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.ProductDto;
import org.myspring.backend.model.Product;
import org.myspring.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list/{listId}")
    public List<Product> getProductsByShoppingListId(@PathVariable String listId) {
        return productService.getProductsByShoppingListId(listId);
    }


    @PostMapping()
    public void addProductToShoppingList(@RequestBody ProductDto productDto) {
        productService.addProductToShoppingList(productDto);
    }

    @PutMapping()
    public void updateProduct(@RequestBody ProductDto productDto) {
        productService.updateProduct(productDto);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
    }
}