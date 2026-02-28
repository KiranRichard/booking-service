package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketBookingRequest {

    private long busNumber;
    private LocalDateTime bookingDateTime;
    private LocalDateTime travelDateTime;
    private LocalDateTime cancellationDateTime;
    private String source;
    private String destination;
    private int totalSeats;
    private List<PassengerRequest> passengerRequests;
    private long bookedBy;
    private String status;
}
