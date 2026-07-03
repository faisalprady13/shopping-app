package org.myspring.backend.service;

import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.UserIdNotFound;
import org.myspring.backend.model.Product;
import org.myspring.backend.model.ShoppingList;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.ListRepo;
import org.myspring.backend.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingListService {
    private final ListRepo listRepo;
    private final IdService idService;
    private final UserRepo userRepo;

    public ShoppingListService(ListRepo listRepo, IdService idService, UserRepo userRepo) {
        this.listRepo = listRepo;
        this.idService = idService;
        this.userRepo = userRepo;
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
    public ShoppingList saveList(ShoppingListDTO listDTO) throws UserIdNotFound {
        ShoppingList newList;
        String id= idService.generateId();
        Instant timestamp= Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User listOwner= listDTO.user();
        List<Product> products= Collections.emptyList();

        if (userRepo.findUserById(listOwner.getId()) != null) {
            newList = new ShoppingList(id, listDTO.name(),
                                        timestamp, listOwner,
                                        products);
            listRepo.save(newList);
            return newList;
        } else {
            throw new UserIdNotFound("User with id " +
                                        listOwner.getId() +
                                        " not found!");
        }
    }
}
