package com.online.bus.ticket.reservation.booking.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusInventory {

    private long busInventoryId;
    private long busRouteNumber;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
