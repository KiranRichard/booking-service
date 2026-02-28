package com.online.bus.ticket.reservation.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long passengerId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String emailId;
    private String contactNumber;
    private String buildingNumber;
    private String streetName;
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}
