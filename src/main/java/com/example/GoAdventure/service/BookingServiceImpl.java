package com.example.GoAdventure.service;

import com.example.GoAdventure.model.entity.Booking;
import com.example.GoAdventure.model.entity.PendingBooking;
import com.example.GoAdventure.repository.BookingRepository;
import com.example.GoAdventure.service.interfaces.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addBooking(PendingBooking pendingBooking) {
        Booking booking = modelMapper.map(pendingBooking, Booking.class);
        this.bookingRepository.save(booking);
    }
}
