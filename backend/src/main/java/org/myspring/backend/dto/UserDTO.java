package org.myspring.backend.dto;

import org.myspring.backend.model.ShoppingList;

public record UserDTO(String name, ShoppingList[] shoppingLists) {
}
