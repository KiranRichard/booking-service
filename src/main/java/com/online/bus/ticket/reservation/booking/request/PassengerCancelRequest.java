package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;

@Data
public class PassengerCancelRequest {

    private long passengerId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String emailId;
    private String contactNumber;
}
