package com.online.bus.ticket.reservation.booking.enums;

public enum BookingStatus {

    PENDING, CANCELLED, CONFIRMED, REFUNDED, COMPLETED, ONBOARDED, PAID, CANCELLATION_REQUESTED, REJECTED;

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
