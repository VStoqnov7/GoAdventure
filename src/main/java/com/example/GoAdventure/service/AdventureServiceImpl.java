package com.example.GoAdventure.service;

import com.example.GoAdventure.model.dtos.AdventureDTO;
import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.entity.PendingBooking;
import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.view.AdventureView;
import com.example.GoAdventure.repository.AdventureRepository;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.service.interfaces.BookingService;
import com.example.GoAdventure.service.interfaces.PendingBookingService;
import com.example.GoAdventure.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdventureServiceImpl implements AdventureService {

    private final AdventureRepository adventureRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final PendingBookingService pendingBookingService;
    private final BookingService bookingService;
    private final UserService userService;

    public AdventureServiceImpl(AdventureRepository adventureRepository, ModelMapper modelMapper, CloudinaryService cloudinaryService, PendingBookingService pendingBookingService, BookingService bookingService, UserService userService) {
        this.adventureRepository = adventureRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.pendingBookingService = pendingBookingService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @Override
    public void saveAdventure(AdventureDTO adventureDTO) {
        Adventure adventure = modelMapper.map(adventureDTO,Adventure.class);

        List<String> photoUrls = adventureDTO.getPhotoUrls().stream()
                .map(cloudinaryService::saveImage)
                .collect(Collectors.toList());
        adventure.setPhotoUrls(photoUrls);

        this.adventureRepository.save(adventure);
    }

    @Override
    public List<AdventureView> findAllAdventures() {
        List<Adventure> adventures = adventureRepository.findAll();
        return adventures.stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdventureView> findAdventuresByLocationNameAndType(String locationName, AdventureType adventureType) {
        List<Adventure> adventures = adventureRepository.findByLocationNameAndType(locationName, adventureType);
        return adventures.stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class))
                .collect(Collectors.toList());
    }

    @Override
    public Adventure findById(Long adventureId) {
        return this.adventureRepository.findById(adventureId).orElse(null);
    }

    @Override
    public AdventureView createAdventureProfileView(Adventure adventure) {
        return modelMapper.map(adventure, AdventureView.class)
                .setType(adventure.getType().getName());
    }

    @Override
    @Transactional
    public void deleteAdventure(Long id) {
        this.adventureRepository.deleteById(id);
        this.pendingBookingService.deletePendingByAdventureId(id);
    }

    @Override
    @Transactional
    public void approveBooking(Long pendingBookingId) {
        PendingBooking pendingBooking = this.pendingBookingService.findByPendingBookingId(pendingBookingId);
        if (pendingBooking != null) {
            userService.sendNotificationEmail(pendingBooking,true);
            this.bookingService.addBooking(pendingBooking);
            this.pendingBookingService.deletePendingBooking(pendingBookingId);
        } else {
            throw new EntityNotFoundException("Pending booking not found with id: " + pendingBookingId);
        }
    }

    @Override
    @Transactional
    public void rejectBooking(Long pendingBookingId) {
        PendingBooking pendingBooking = this.pendingBookingService.findByPendingBookingId(pendingBookingId);
        if (pendingBooking != null) {
            userService.sendNotificationEmail(pendingBooking,false);
            this.pendingBookingService.deletePendingBooking(pendingBookingId);
        } else {
            throw new EntityNotFoundException("Pending booking not found with id: " + pendingBookingId);
        }
    }

    @Override
    public List<AdventureView> getInitialThreeAdventures() {
        return adventureRepository.findInitialThreeAdventures()
                .stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class)
                        .setType(adventure.getType().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdventureView> getFollowingThreeAdventures() {
        return adventureRepository.findFollowingThreeAdventures()
                .stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class)
                        .setType(adventure.getType().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdventureView> getExtendedTenAdventureList() {
        return adventureRepository.findExtendedTenAdventures()
                .stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class)
                        .setType(adventure.getType().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdventureView> getInitialFourAdventures() {
        return adventureRepository.findInitialFourAdventures()
                .stream()
                .map(adventure -> modelMapper.map(adventure, AdventureView.class)
                        .setType(adventure.getType().getName()))
                .collect(Collectors.toList());
    }
}
