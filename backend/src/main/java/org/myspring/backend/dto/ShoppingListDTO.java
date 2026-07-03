package org.myspring.backend.dto;

import org.myspring.backend.model.User;

/** Data transfer object for shopping lists
 *
 * @param name of shopping lists
 * @param user who owns the list
 */
public record ShoppingListDTO(
        String name,
        User user) {
}
