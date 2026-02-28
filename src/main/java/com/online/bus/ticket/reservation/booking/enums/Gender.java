package com.online.bus.ticket.reservation.booking.enums;

public enum Gender {
    MALE, FEMALE;

    public static Gender findByName(String name) {
        Gender gender = null;
        for (Gender genderValue : values()) {
            if (genderValue.name().equalsIgnoreCase(name)) {
                gender = genderValue;
                break;
            }
        }
        return gender;
    }
}
