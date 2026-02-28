package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

@Data
public class Address {

    private String buildingNumber;
    private String streetName;
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
}
