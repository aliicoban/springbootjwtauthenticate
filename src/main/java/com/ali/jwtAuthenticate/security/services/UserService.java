package com.ali.jwtAuthenticate.security.services;

import com.ali.jwtAuthenticate.model.Users;

public interface UserService {
    Users findOne(String email);
    Users save(Users users);
}
