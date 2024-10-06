package com.example.GoAdventure.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "pending_bookings")
public class PendingBooking extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Adventure adventure;

    @Column(nullable = false, name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(nullable = false, name = "registered_on")
    private LocalDateTime registeredOn;
}