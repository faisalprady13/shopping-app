package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private int quantity;
    private ProductStatus status;
    @ManyToOne
    @JoinColumn( name= "list_id" )
    @JsonBackReference
    private ShoppingList shoppingList;


    public Product(ProductDto productDto) {
        this(null, productDto.name(), productDto.quantity(), productDto.status(), null);
    }


    public Product(ProductDto newProductDto, Product oldProduct) {
        String name = !newProductDto.name().isEmpty() ? newProductDto.name() : oldProduct.getName();
        int quantity = newProductDto.quantity() != null && newProductDto.quantity() > 0 ? newProductDto.quantity() : oldProduct.getQuantity();
        ProductStatus status = newProductDto.status() != null ? newProductDto.status() : oldProduct.getStatus();
        this(oldProduct.getId(), name, quantity, status, oldProduct.getShoppingList());
    }
}
