package com.epam.common.debezium.service;

import com.epam.common.debezium.entity.UserEntity;

public interface UserService {

    void addUser(UserEntity userEntity);
}
