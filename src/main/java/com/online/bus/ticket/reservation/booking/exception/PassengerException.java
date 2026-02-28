package com.online.bus.ticket.reservation.booking.exception;

public class PassengerException extends RuntimeException {

    private String errorMessage;

    public PassengerException() {
        super();
    }

    public PassengerException(String errorMessage) {
        super(errorMessage);
    }
}
