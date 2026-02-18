package com.gaston.springcloud.msvc.users.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.gaston.springcloud.msvc.users.entities.User;
import com.gaston.springcloud.msvc.users.services.IUserService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class UserController {

    private final IUserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("UserController::createUser: creando {}", user);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("UserController::updateUser: actualizando usuario  datos {}",  user);
        Optional<User> updatedUserOpt = userService.updateUser(id, user);
        return updatedUserOpt.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("UserController::getUserById: buscando usuario con id {}", id);
        Optional<User> userOpt = userService.findById(id);
        return userOpt.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("UserController::getUserByUsername: login con {}", username);
        Optional<User> userOpt = userService.findByUsername(username);
        return userOpt.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getUsers() {
        logger.info("UserController::getUsers: obteniendo todos los usuarios");
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("UserController::deleteUser: eliminando usuario con id {}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}