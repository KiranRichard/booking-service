package com.online.bus.ticket.reservation.booking.response;

import lombok.Data;

import java.util.List;

@Data
public class TicketDetails {

    private TicketBookingResponse ticketBookingResponse;
    private List<PassengerResponse> passengers;
}
