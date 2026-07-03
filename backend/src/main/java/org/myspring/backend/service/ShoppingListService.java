package org.myspring.backend.service;

import org.myspring.backend.dto.ShoppingListDTO;
import org.myspring.backend.exception.ListIdNotFound;
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

    /** Returns the shopping list with the requested ID.
     *
     * @param id to search for
     * @return found ShoppingList
     * @throws ListIdNotFound when id not exist
     */
    public ShoppingList getListById(String id) throws ListIdNotFound {
        ShoppingList list= listRepo.findById(id).orElse(null);

        if (list != null) {
            return list;
        } else {
            throw new ListIdNotFound("List with id " + id + " not found!");
        }
    }

    /** Create a new shopping list and save it.
     *
     * @param listDTO name and user for new list
     * @return saved list
     * @throws UserIdNotFound when user not exist
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
