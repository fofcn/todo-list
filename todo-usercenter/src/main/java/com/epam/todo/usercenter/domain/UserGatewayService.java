package com.epam.todo.usercenter.domain;

import com.epam.todo.usercenter.infrastructure.model.User;

public interface UserGatewayService {

    /**
     * find user by username
     * @param username username
     * @return user
     */
    User findByUsername(String username);
}
