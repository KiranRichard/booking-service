package com.online.bus.ticket.reservation.booking.exception;

public class TicketBookingException extends RuntimeException {

    private String errorMessage;

    public TicketBookingException() {
        super();
    }

    public TicketBookingException(String errorMessage) {
        super(errorMessage);
    }
}
