package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.myspring.backend.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingList> shoppingLists;

    public User(String id, UserDto userDto) {
        List<ShoppingList> list = userDto.shoppingLists() == null || userDto.shoppingLists().length == 0 ?
                new ArrayList<>() : new ArrayList<>(List.of(userDto.shoppingLists()));
        this(id, userDto.name(), list);
    }
}
