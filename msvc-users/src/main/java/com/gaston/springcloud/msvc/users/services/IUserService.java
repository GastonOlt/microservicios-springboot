package com.gaston.springcloud.msvc.users.services;

import java.util.Optional;

import com.gaston.springcloud.msvc.users.entities.User;

public interface IUserService {
    
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User saveUser(User user);
    Optional<User> updateUser(Long id, User user);
    void deleteById(Long id);
    Iterable<User> findAll();
}
