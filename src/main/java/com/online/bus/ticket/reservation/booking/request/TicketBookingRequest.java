package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketBookingRequest {

    private long busNumber;
    private LocalDateTime bookingDateTime;
    private LocalDateTime travelDateTime;
    private LocalDateTime cancellationDateTime;
    private String source;
    private String destination;
    private int totalSeats;
    private long bookedBy;
    private String status;
}
