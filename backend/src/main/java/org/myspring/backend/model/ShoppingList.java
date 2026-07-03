package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "Lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    @Id
    private String id;
    private String name;
    private Instant date;
    @ManyToOne
    @JoinColumn( name= "user_id" )
    @JsonBackReference
    private User user; 
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;
}
