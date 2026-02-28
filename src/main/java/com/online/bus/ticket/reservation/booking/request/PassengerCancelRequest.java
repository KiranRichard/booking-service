package com.online.bus.ticket.reservation.booking.request;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
