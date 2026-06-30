package org.myspring.backend.service;

import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.repository.ListRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    /** Displays a list of all saved shopping lists.
     *
     * @return list of shopping lists or empty list
     */
    public List<ShoppingList> getLists(){
        return listRepo.findAll();
    }

    /** Create a new shopping list and save it.
     *
     * @param listDTO name and user for new list
     * @return saved list
     */
    public ShoppingList saveList(ShoppingListDTO listDTO){
        ShoppingList newList;
        String id= idService.generateId();
        Instant timestamp= Instant.now();

        newList= new ShoppingList(id, listDTO.name(),
                                    timestamp, listDTO.user());
        listRepo.save(newList);
        return newList;
    }
}
