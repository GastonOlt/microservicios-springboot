package com.gaston.springcloud.msvc.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gaston.springcloud.msvc.users.entities.Role;
import com.gaston.springcloud.msvc.users.entities.User;
import com.gaston.springcloud.msvc.users.repositories.RoleRepository;
import com.gaston.springcloud.msvc.users.repositories.UserRepository;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(user));
        if (user.isEnabled() == null) {
            user.setEnabled(true); 
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> updateUser(Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        return existingUserOpt.map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            if (user.isEnabled() == null) {
                existingUser.setEnabled(true);
            }else{
                existingUser.setEnabled(user.isEnabled());
            }
            existingUser.setRoles(getRoles(user));
            return Optional.of(userRepository.save(existingUser));
        }).orElseGet(() -> Optional.empty());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private List<Role> getRoles(User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> userRoleOpt = roleRepository.findByName("ROLE_USER");
        userRoleOpt.ifPresent(roles::add);
        if (user.isAdmin() != null && user.isAdmin()) {
            Optional<Role> adminRoleOpt = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOpt.ifPresent(roles::add);
        }
        return roles;
    }
}