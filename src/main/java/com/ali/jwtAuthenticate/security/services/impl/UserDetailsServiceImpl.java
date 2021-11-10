package com.ali.jwtAuthenticate.security.services.impl;

import com.ali.jwtAuthenticate.model.Users;
import com.ali.jwtAuthenticate.repository.UsersRepository;
import com.ali.jwtAuthenticate.security.services.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Users user = usersRepository.findByEmail(email);
      return (UserDetails) UserPrinciple.build(user);
    }


}
