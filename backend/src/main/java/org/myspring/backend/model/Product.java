package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myspring.backend.dto.ProductDTO;

@Entity
@Table(name = "Products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private int quantity;
    private ProductStatus status;
    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference
    private ShoppingList shoppingList;


    public Product(String id, ProductDTO productDto) {
        this(id, productDto.name(), productDto.quantity(), productDto.status(), null);
    }

    public void update(ProductDTO productDto) {
        this.name = productDto.name() != null ? productDto.name() : this.name;
        this.quantity = productDto.quantity() != null && productDto.quantity() > 0 ? productDto.quantity() : this.quantity;
        this.status = productDto.status() != null ? productDto.status() : this.status;
    }
}
