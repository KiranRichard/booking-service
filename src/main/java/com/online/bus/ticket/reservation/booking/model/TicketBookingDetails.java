package com.online.bus.ticket.reservation.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class TicketBookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ticketBookingDetailsId;
    private long passengerId;
    private long bookingId;
    private String status;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    private LocalDateTime cancellationDateTime;
    private String reasonForCancellation;
}
