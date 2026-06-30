package org.myspring.backend.service;

import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.repository.ListRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ShoppingListService {
    private final ListRepo listRepo;
    private final IdService idService;

    public ShoppingListService(ListRepo listRepo, IdService idService) {
        this.listRepo = listRepo;
        this.idService = idService;
    }

    public List<ShoppingList> getLists(){
        return listRepo.findAll();
    }
}
