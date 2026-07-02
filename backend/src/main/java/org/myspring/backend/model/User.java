package org.myspring.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @UuidGenerator
    private UUID id;
    private String name;
    private List<ShoppingList> shoppingLists;

    public User(UserDto userDto) {
        List<ShoppingList> shoppingLists = userDto.shoppingLists() == null || userDto.shoppingLists().length == 0 ?
                new ArrayList<>() : new ArrayList<>(List.of(userDto.shoppingLists()));
        this(null, userDto.name(), shoppingLists);
    }
}
