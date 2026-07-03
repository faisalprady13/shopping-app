package org.myspring.backend.dto;

import org.myspring.backend.model.ShoppingList;

import java.util.ArrayList;

public record UserDTO(String name, ArrayList<ShoppingList> shoppingLists) {
}
