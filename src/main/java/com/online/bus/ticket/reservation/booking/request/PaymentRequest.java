package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentRequest {

    private long bookingId;
    private long busRouteNum;
    private double amount;
    private int noOfSeatsBooked;
    private String paymentStatus;
    private LocalDateTime paymentDateTime;
}
