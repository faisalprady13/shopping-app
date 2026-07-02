package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myspring.backend.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name= "Users" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingList> shoppingLists;

    public User(UserDto userDto) {
        List<ShoppingList> shoppingLists = userDto.shoppingLists() == null || userDto.shoppingLists().length == 0 ?
                new ArrayList<>() : new ArrayList<>(List.of(userDto.shoppingLists()));
        this(null, userDto.name(), shoppingLists);
    }
}
