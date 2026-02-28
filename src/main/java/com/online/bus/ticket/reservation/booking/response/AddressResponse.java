package com.online.bus.ticket.reservation.booking.response;

import lombok.Data;

@Data
public class AddressResponse {

    private String buildingNumber;
    private String streetName;
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
}