package org.myspring.backend.dto;

import org.myspring.backend.model.ProductStatus;

public record ProductDto(
        String name,
        Integer quantity,
        ProductStatus status,
        String shoppingListId) {
}
