package org.myspring.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private Integer quantity;
    private ProductStatus status;
    @ManyToOne
    @JoinColumn(name = "list_id")
    private ShoppingList shoppingList;
}
