package com.binara.repositories;

import com.binara.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository for CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
