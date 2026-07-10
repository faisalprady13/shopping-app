package org.myspring.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myspring.backend.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "Users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"authProvider", "providerId"})
)
@NoArgsConstructor
@Data
public class User {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String email;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private String providerId;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingList> shoppingLists;

    public User(String id, String name, List<ShoppingList> shoppingLists) {
        this(id, name, null, null, AuthProvider.LOCAL, null, shoppingLists);
    }

    @Builder
    public User(String id, String name, String email, String passwordHash, AuthProvider authProvider,
                String providerId, List<ShoppingList> shoppingLists) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.authProvider = authProvider;
        this.providerId = providerId;
        this.shoppingLists = shoppingLists;
    }

    public User(String id, UserDTO userDto) {
        ArrayList<ShoppingList> list = userDto.shoppingLists() == null || userDto.shoppingLists().isEmpty() ?
                new ArrayList<>() : new ArrayList<>(userDto.shoppingLists());
        this.id = id;
        this.name = userDto.name();
        this.authProvider = AuthProvider.LOCAL;
        this.shoppingLists = list;
    }
}
