package com.online.bus.ticket.reservation.booking.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketBookingResponse {

    private long bookingId;
    private long busNumber;
    private LocalDateTime bookingDateTime;
    private LocalDateTime travelDateTime;
    private String source;
    private String destination;
    private int totalSeats;
    private long bookedBy;
    private String status;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
