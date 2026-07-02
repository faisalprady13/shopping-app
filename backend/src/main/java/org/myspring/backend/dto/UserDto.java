package org.myspring.backend.dto;

import org.myspring.backend.model.ShoppingList;

public record UserDto(String name, ShoppingList[] shoppingLists) {
}