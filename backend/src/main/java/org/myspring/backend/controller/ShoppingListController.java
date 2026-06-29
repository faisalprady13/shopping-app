package org.myspring.backend.controller;

import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.service.ShoppingListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {
    private final ShoppingListService listService;

    public ShoppingListController(ShoppingListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public List<ShoppingList> getAllLists(){
        return listService.getLists();
    }
}
