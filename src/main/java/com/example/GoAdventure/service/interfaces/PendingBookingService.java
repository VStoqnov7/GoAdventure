package com.example.GoAdventure.service.interfaces;


import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.entity.PendingBooking;
import com.example.GoAdventure.model.view.PendingBookingView;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public interface PendingBookingService {
    void savePendingBooking(Adventure adventure, LocalDateTime dateTime, UserDetails userDetails);

    void deletePendingByAdventureId(Long id);

    List<PendingBookingView> findAll();

    PendingBooking findByPendingBookingId(Long pendingBookingId);

    void deletePendingBooking(Long pendingBookingId);
}
