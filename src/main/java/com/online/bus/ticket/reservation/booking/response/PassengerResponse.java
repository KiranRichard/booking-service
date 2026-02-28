package com.online.bus.ticket.reservation.booking.response;

import lombok.Data;

@Data
public class PassengerResponse {
    private long passengerId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String emailId;
    private String contactNumber;
    private AddressResponse addressResponse;
}
