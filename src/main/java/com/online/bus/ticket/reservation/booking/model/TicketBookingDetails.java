package com.online.bus.ticket.reservation.booking.model;

import jakarta.persistence.*;
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
    //@OneToMany
    //@Column(insertable=false, updatable=false)
    private long passengerId;
    //@OneToMany
    //@Column(insertable=false, updatable=false)
    private long bookingId;
    private String status;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    /*@OneToOne
    @JoinColumn(name = "passengerId")
    private Passenger passenger;

    @OneToOne
    @JoinColumn(name = "bookingId")
    private TicketBooking ticketBooking;*/
}
