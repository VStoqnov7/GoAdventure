package com.example.GoAdventure.repository;

import com.example.GoAdventure.model.entity.PendingBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingBookingRepository extends JpaRepository<PendingBooking,Long> {

    void deleteByAdventureId(Long id);

    PendingBooking findByAdventureId(Long adventureId);
}
