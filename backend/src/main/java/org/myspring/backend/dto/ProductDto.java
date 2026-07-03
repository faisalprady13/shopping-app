package org.myspring.backend.dto;

import org.myspring.backend.model.ProductStatus;

import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        Integer quantity,
        ProductStatus status,
        String shoppingListId) {
}
