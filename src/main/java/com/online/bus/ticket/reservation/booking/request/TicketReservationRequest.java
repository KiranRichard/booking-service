package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.util.List;

@Data
public class TicketReservationRequest {

    private TicketBookingRequest ticketBookingRequest;
    private List<PassengerRequest> passengerRequests;
}
