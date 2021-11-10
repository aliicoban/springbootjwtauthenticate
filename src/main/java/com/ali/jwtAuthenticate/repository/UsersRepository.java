package com.ali.jwtAuthenticate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ali.jwtAuthenticate.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
    Boolean existsByEmail(String email);
}
