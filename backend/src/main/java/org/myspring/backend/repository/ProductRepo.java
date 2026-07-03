package org.myspring.backend.repository;

import org.myspring.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    List<Product> findProductsByShoppingList_Id(String id);

    Product findProductById(String id);
}
