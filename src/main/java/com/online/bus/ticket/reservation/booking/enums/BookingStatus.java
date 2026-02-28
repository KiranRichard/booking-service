package com.online.bus.ticket.reservation.booking.enums;

public enum BookingStatus {

    PENDING, CONFIRMED, REFUNDED;

    public static BookingStatus findByName(String name) {
        BookingStatus bookingStatus = null;
        for (BookingStatus bookingStatusValue : values()) {
            if (bookingStatusValue.name().equalsIgnoreCase(name)) {
                bookingStatus = bookingStatusValue;
                break;
            }
        }
        return bookingStatus;
    }
}
