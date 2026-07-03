package org.myspring.backend.dto;

import org.myspring.backend.model.ProductStatus;

public record ProductDTO(
        String id,
        String name,
        Integer quantity,
        ProductStatus status,
        String shoppingListId) {
}
