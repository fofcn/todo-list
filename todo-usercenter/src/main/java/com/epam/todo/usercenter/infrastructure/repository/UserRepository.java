package com.epam.todo.usercenter.infrastructure.repository;

import com.epam.todo.usercenter.infrastructure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
