package org.myspring.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.myspring.backend.dto.ProductDto;

import java.util.UUID;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private Integer quantity;
    private ProductStatus status;
    @ManyToOne
    @JoinColumn(name = "list_id")
    private ShoppingList shoppingList;


    public Product(ProductDto productDto) {
        this(null, productDto.name(), productDto.quantity(), productDto.status(), null);
    }
}
