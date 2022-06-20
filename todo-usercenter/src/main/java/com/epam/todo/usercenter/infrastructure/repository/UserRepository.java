package com.epam.todo.usercenter.infrastructure.repository;

import com.epam.todo.usercenter.infrastructure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.username=:username")
    User findByUsername(@Param("username") String username);
}
