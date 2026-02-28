package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketCancelRequest {

    private long bookingId;
    private String reasonForCancellation;
    private LocalDateTime cancellationDateTime;
    private List<PassengerCancelRequest> passengerCancelRequests;
}
