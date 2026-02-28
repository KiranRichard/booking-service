package com.online.bus.ticket.reservation.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class TicketBookingDetails {
    @Id
    private long ticketBookingDetailsId;
    private long passengerId;
    private long bookingId;
    private String status;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    @OneToOne
    @JoinColumn(name = "passengerId")
    private Passenger passenger;

    @OneToOne
    @JoinColumn(name = "bookingId")
    private TicketBooking ticketBooking;
}
