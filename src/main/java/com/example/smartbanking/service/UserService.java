package com.example.smartbanking.service;

import com.example.smartbanking.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user); // save new user

    User getByEmail(String email); // find by email

    User getById(Long id); // find by id

    List<User> getAllUsers(); // list users

    User updateUser(User user); // update info

    void deleteUser(Long id); // remove user
}