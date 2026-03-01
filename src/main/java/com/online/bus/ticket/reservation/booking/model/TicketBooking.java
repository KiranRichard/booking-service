package com.online.bus.ticket.reservation.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class TicketBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookingId;
    private long busNumber;
    private LocalDateTime bookingDateTime;
    private LocalDateTime travelDateTime;
    private String source;
    private String destination;
    private int totalSeats;
    private long bookedBy;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}
