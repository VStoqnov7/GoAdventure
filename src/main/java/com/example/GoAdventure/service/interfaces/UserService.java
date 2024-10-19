package com.example.GoAdventure.service.interfaces;

import com.example.GoAdventure.model.dtos.UserRegisterDTO;

public interface UserService {

    void saveUser(UserRegisterDTO userRegisterDTO);

    void dbInitAdmin();

    boolean confirmUserAccount(String token);
}
