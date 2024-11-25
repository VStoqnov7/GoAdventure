package com.example.GoAdventure.service;

import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.entity.PendingBooking;
import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.view.PendingBookingView;
import com.example.GoAdventure.repository.PendingBookingRepository;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.service.interfaces.PendingBookingService;
import com.example.GoAdventure.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PendingBookingServiceImpl implements PendingBookingService {

    private final PendingBookingRepository pendingBookingRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final AdventureService adventureService;

    public PendingBookingServiceImpl(PendingBookingRepository pendingBookingRepository, UserService userService, ModelMapper modelMapper, @Lazy AdventureService adventureService) {
        this.pendingBookingRepository = pendingBookingRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.adventureService = adventureService;
    }

    @Override
    public void savePendingBooking(Adventure adventure, LocalDateTime dateTime, UserDetails userDetails) {
        PendingBooking pendingBooking = new PendingBooking();
        Optional<User>  user = this.userService.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userDetails.getUsername());
        }
        pendingBooking.setUser(user.get());
        pendingBooking.setAdventure(adventure);
        pendingBooking.setRegisteredOn(LocalDateTime.now());
        pendingBooking.setBookingDate(dateTime);
        this.pendingBookingRepository.save(pendingBooking);
    }

    @Override
    public void deletePendingByAdventureId(Long id) {
        this.pendingBookingRepository.deleteByAdventureId(id);
    }

    @Override
    public List<PendingBookingView> findAll() {
        List<PendingBooking> pendingBookings = this.pendingBookingRepository.findAll();
        return pendingBookings.stream()
                .map(pb -> modelMapper.map(pb, PendingBookingView.class)
                        .setAdventureView(this.adventureService.createAdventureProfileView(this.adventureService.findById(pb.getAdventure().getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public PendingBooking findByPendingBookingId(Long pendingBookingId) {
        return this.pendingBookingRepository.findById(pendingBookingId).get();
    }

    @Override
    public void deletePendingBooking(Long pendingBookingId) {
        this.pendingBookingRepository.deleteById(pendingBookingId);
    }
}
