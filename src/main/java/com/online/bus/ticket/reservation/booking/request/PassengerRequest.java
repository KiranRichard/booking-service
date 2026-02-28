package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

@Data
public class PassengerRequest {

    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String emailId;
    private String contactNumber;
    private Address address;
}
