package org.myspring.backend.dto;

import org.myspring.backend.model.ShoppingList;

import java.util.List;

public record UserDto(String name, ShoppingList[] shoppingLists) {
}
