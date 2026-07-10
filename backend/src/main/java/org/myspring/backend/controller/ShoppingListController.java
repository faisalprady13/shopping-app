package org.myspring.backend.controller;

import org.myspring.backend.dto.ProductDTO;
import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.ListIdNotFound;
import org.myspring.backend.exception.ProductNotFound;
import org.myspring.backend.exception.UserIdNotFound;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.Role;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.security.AuthenticatedUser;
import org.myspring.backend.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {
    private final ShoppingListService listService;

    public ShoppingListController(ShoppingListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public List<ShoppingList> getAllLists(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        if (isAdmin(currentUser)) {
            return listService.getLists();
        }

        return listService.getListsByUserId(currentUser.id());
    }

    @GetMapping("/{id}")
    public ShoppingList getListById(@PathVariable String id, @AuthenticationPrincipal AuthenticatedUser currentUser) throws ListIdNotFound {
        ShoppingList list = listService.getListById(id);
        assertListAccess(currentUser, list);
        return list;
    }

    @GetMapping("/all/{id}")
    public List<ShoppingList> getListsByUserId(@PathVariable String id, @AuthenticationPrincipal AuthenticatedUser currentUser) throws UserIdNotFound {
        if (!isAdmin(currentUser) && !currentUser.id().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own lists");
        }

        return listService.getListsByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingList createList(@RequestBody ShoppingListDTO shopList, @AuthenticationPrincipal AuthenticatedUser currentUser) throws UserIdNotFound {
        if (!isAdmin(currentUser) && !currentUser.id().equals(shopList.user().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only create your own lists");
        }

        return listService.saveList(shopList);
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestBody ProductDTO productDto, @AuthenticationPrincipal AuthenticatedUser currentUser) throws ListIdNotFound {
        assertListAccess(currentUser, listService.getListById(productDto.shoppingListId()));
        return listService.addProductToShoppingList(productDto);
    }

    @PutMapping("/update-product")
    public ShoppingList updateProduct(@RequestBody ProductDTO productDto, @AuthenticationPrincipal AuthenticatedUser currentUser) throws ListIdNotFound, ProductNotFound {
        assertListAccess(currentUser, listService.getListById(productDto.shoppingListId()));
        return listService.updateProductInShoppingList(productDto);
    }

    @DeleteMapping("/remove-product/{productId}")
    public ShoppingList removeProduct(@PathVariable String productId, @AuthenticationPrincipal AuthenticatedUser currentUser) throws ListIdNotFound, ProductNotFound {
        assertListAccess(currentUser, listService.getListByProductId(productId));
        return listService.removeProductFromShoppingList(productId);
    }

    private boolean isAdmin(AuthenticatedUser currentUser) {
        return currentUser.role() == Role.ADMIN;
    }

    private void assertListAccess(AuthenticatedUser currentUser, ShoppingList list) {
        if (isAdmin(currentUser)) {
            return;
        }

        if (list.getUser() == null || !currentUser.id().equals(list.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own lists");
        }
    }
}
