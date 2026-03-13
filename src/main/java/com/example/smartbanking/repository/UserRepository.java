package com.example.smartbanking.repository;

import com.example.smartbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email); // lookup by email
}