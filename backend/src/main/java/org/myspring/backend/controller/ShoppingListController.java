package org.myspring.backend.controller;

import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.service.ShoppingListService;
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
    public List<ShoppingList> getAllLists(){
        return listService.getLists();
    }

    @PostMapping
    public ShoppingList createList(@RequestBody ShoppingListDTO shopList){
        return listService.saveList(shopList);
    }
}
