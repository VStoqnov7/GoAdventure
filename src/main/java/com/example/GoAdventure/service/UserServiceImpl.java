package com.example.GoAdventure.service;

import com.example.GoAdventure.config.AdminConfig;
import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.UserRepository;
import com.example.GoAdventure.service.interfaces.UserRoleService;
import com.example.GoAdventure.service.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public UserServiceImpl(AdminConfig adminConfig,PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleService userRoleService) {
        this.adminConfig = adminConfig;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    @Override
    public void dbInitAdmin() {
        if (this.userRepository.count() == 0) {
            Set<UserRole> roles = new HashSet<>();
            roles.add(this.userRoleService.findByRole(Role.USER));
            roles.add(this.userRoleService.findByRole(Role.ADMIN));

            User user = new User()
                    .setUsername(adminConfig.getUsername())
                    .setPassword(passwordEncoder.encode(adminConfig.getPassword()))
                    .setEmail(adminConfig.getEmail())
                    .setPhone(adminConfig.getPhone())
                    .setRoles(roles)
                    .setEnabled(true);
            this.userRepository.save(user);
        }
    }
}
