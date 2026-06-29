package org.myspring.backend.repository;

import org.myspring.backend.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepo extends JpaRepository<ShoppingList, String> {
    List<ShoppingList> findByUserName(String name);
}
