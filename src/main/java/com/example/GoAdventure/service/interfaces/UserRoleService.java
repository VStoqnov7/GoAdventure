package com.example.GoAdventure.service.interfaces;

import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.enums.Role;

public interface UserRoleService {
    UserRole findByRole(Role user);

    void dbInit();

}
