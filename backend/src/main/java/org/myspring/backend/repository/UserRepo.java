package org.myspring.backend.repository;

import org.myspring.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findUserById(String id);
    User findByName(String name);
}
