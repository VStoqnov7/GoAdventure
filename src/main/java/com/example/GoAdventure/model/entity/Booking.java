package com.example.GoAdventure.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity{

    @ManyToOne
    private User user;

    @ManyToOne
    private Adventure adventure;

    @Column(nullable = false, name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(nullable = false, name = "registered_on")
    private LocalDateTime registeredOn;
}
