package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myspring.backend.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingList> shoppingLists;

    public User(String id, UserDTO userDto) {
        ArrayList<ShoppingList> list = userDto.shoppingLists() == null || userDto.shoppingLists().isEmpty() ?
                new ArrayList<>() : new ArrayList<>(userDto.shoppingLists());
        this(id, userDto.name(), list);
    }
}
