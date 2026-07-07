package org.myspring.backend.controller;

import org.myspring.backend.dto.ProductDTO;
import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.ListIdNotFound;
import org.myspring.backend.exception.ProductNotFound;
import org.myspring.backend.exception.UserIdNotFound;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {
    private final ShoppingListService listService;

    public ShoppingListController(ShoppingListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public List<ShoppingList> getAllLists() {
        return listService.getLists();
    }

    @GetMapping("/{id}")
    public ShoppingList getListById(@PathVariable String id) throws ListIdNotFound {
        return listService.getListById(id);
    }

    @GetMapping("/all/{id}")
    public List<ShoppingList> getListsByUserId(@PathVariable String id) throws UserIdNotFound {
        return listService.getListsByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingList createList(@RequestBody ShoppingListDTO shopList) throws UserIdNotFound {
        return listService.saveList(shopList);
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestBody ProductDTO productDto) throws ListIdNotFound {
        return listService.addProductToShoppingList(productDto);
    }

    @PutMapping("/update-product")
    public ShoppingList updateProduct(@RequestBody ProductDTO productDto) throws ListIdNotFound, ProductNotFound {
        return listService.updateProductInShoppingList(productDto);
    }

    @DeleteMapping("/remove-product/{productId}")
    public ShoppingList removeProduct(@PathVariable String productId) throws ListIdNotFound, ProductNotFound {
        return listService.removeProductFromShoppingList(productId);
    }
}
