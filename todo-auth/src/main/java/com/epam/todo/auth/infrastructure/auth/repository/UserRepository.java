package com.epam.todo.auth.infrastructure.auth.repository;

import com.epam.todo.auth.infrastructure.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.username=?1")
    User findByUsername(String username);
}
