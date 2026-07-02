package org.myspring.backend.service;


import lombok.RequiredArgsConstructor;
import org.myspring.backend.dto.ProductDto;
import org.myspring.backend.dto.UserDto;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public List<Product> getProductsByShoppingListId(String id) {
        return productRepo.findProductsByShoppingList_Id(id);
    }

    public void addProductToShoppingList(ProductDto productDto) {
        Product product = new Product(productDto);
        //set shopping list here first
        productRepo.save(product);
    }
}
