package com.example.GoAdventure.service.interfaces;

import com.example.GoAdventure.model.dtos.AdventureDTO;
import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.view.AdventureView;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

public interface AdventureService {

    void saveAdventure(AdventureDTO adventureDTO);

    List<AdventureView> findAllAdventures();

    List<AdventureView> findAdventuresByLocationNameAndType(String locationName, AdventureType adventureType);

    Adventure findById(Long adventureId);

    AdventureView createAdventureProfileView(Adventure adventure);

    void deleteAdventure(Long id);

    void approveBooking(Long pendingBookingId);

    void rejectBooking(Long pendingBookingId);

    List<AdventureView> getInitialThreeAdventures();

    List<AdventureView> getFollowingThreeAdventures();

    List<AdventureView> getExtendedTenAdventureList();

    List<AdventureView> getInitialFourAdventures();

    void sentMessage(String name, String email, String subject, String message);
}
