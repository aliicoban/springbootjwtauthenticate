package com.ali.jwtAuthenticate.security.services.impl;

import com.ali.jwtAuthenticate.model.Users;
import com.ali.jwtAuthenticate.repository.UsersRepository;
import com.ali.jwtAuthenticate.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersRepository usersRepository;

    @Override
    public Users findOne(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public Users save(Users users) {
        return usersRepository.save(users);
    }
}
