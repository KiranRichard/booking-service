package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentRequest {

    private long bookingId;
    private double amount;
    private String paymentStatus;
    private LocalDateTime paymentDateTime;
}
