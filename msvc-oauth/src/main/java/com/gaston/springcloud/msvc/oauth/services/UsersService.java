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
    private WebClient.Builder client;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.info("Iniciando búsqueda de usuario: {}", username);
            
            User user = client.build()
                    .get()
                    .uri("/username/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            
            if (user == null) {
                logger.error("Usuario no encontrado: {}", username);
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }
            
            logger.info("Usuario encontrado: {} con {} roles", username, user.getRoles().size());
        
            List<GrantedAuthority> roles = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            
            logger.debug("Roles asignados: {}", roles);
           
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), 
                    user.getPassword(), 
                    user.isEnabled(), 
                    true, 
                    true, 
                    true,
                    roles);
       
        } catch (WebClientResponseException e) {
            logger.error("Error en WebClient al buscar usuario {}: {} - {}", username, e.getStatusCode(), e.getResponseBodyAsString());
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        } catch (Exception e) {
            logger.error("Error inesperado al cargar usuario {}: {}", username, e.getMessage(), e);
            throw new UsernameNotFoundException("Error al cargar usuario: " + username);
        }
    }

}
