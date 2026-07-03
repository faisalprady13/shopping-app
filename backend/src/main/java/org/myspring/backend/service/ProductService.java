package org.myspring.backend.service;


import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.ProductDto;
import org.myspring.backend.model.Product;
import org.myspring.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public List<Product> getProductsByShoppingListId(String id) {
        return productRepo.findProductsByShoppingList_Id(id);
    }

    public void addProductToShoppingList(ProductDto productDto) {
        Product product = new Product(productDto);
        //TODO: set product to shopping list
        productRepo.save(product);
    }

    public void updateProduct(ProductDto productDto) {
        Product oldProduct = productRepo.findProductById(productDto.id());
        Product newProduct = new Product(productDto, oldProduct);
        productRepo.save(newProduct);
    }

    public void deleteProduct(UUID productId) {
        productRepo.deleteById(productId);
    }
}
