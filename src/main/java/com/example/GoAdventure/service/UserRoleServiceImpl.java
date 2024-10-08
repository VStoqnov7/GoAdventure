package com.example.GoAdventure.service;

import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.UserRoleRepository;
import com.example.GoAdventure.service.interfaces.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole findByRole(Role role) {
        return userRoleRepository.findByName(role);
    }

    @Override
    public void dbInit() {
        if (userRoleRepository.count() == 0) {
            List<UserRole> roles = Arrays.asList(
                    new UserRole(Role.USER),
                    new UserRole(Role.ADMIN));
            this.userRoleRepository.saveAllAndFlush(roles);
        }
    }
}
