package com.example.GoAdventure.service;

import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));
    }

    private UserDetails map(User user) {
        if (user.isBanned()) {
            throw new UsernameNotFoundException("User is banned.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(this::mapRole).collect(Collectors.toList())
        );
    }

    private GrantedAuthority mapRole(UserRole userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getName().name());
    }
}