package com.gaston.springcloud.msvc.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.gaston.springcloud.msvc.oauth.models.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;


@Service
public class UsersService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private WebClient client;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.info("Ingresando al proceso de login UsersService::loadUserByUsername con {}", username);
            
            User user = client
                    .get()
                    .uri("/username/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            
            List<GrantedAuthority> roles = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            
           
            logger.info("se ha realizado el login con exito  by username: {}", user);
            
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), 
                    user.getPassword(), 
                    user.isEnabled(), 
                    true, 
                    true, 
                    true,
                    roles);
       
        } catch (WebClientResponseException e) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        } catch (Exception e) {
            String errorMsg = "Error en el login , no existe user '" + username + "' en el sistema ";
            logger.error(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }
    }

}
