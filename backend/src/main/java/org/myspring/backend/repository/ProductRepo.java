package org.myspring.backend.repository;

import org.myspring.backend.model.Product;
import org.myspring.backend.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    List<Product> findByListName(String name);
    List<Product> findByStatus(ProductStatus status);
}
