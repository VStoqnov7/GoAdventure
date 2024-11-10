package com.example.GoAdventure.service.interfaces;

import com.example.GoAdventure.model.dtos.UserRegisterDTO;
import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.entity.PendingBooking;
import com.example.GoAdventure.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserRegisterDTO userRegisterDTO);

    void dbInitAdmin();

    boolean confirmUserAccount(String token);

    Optional<User> findByEmail(String email);

    void sendNotificationEmail(PendingBooking pendingBooking, boolean isApproved);
}
