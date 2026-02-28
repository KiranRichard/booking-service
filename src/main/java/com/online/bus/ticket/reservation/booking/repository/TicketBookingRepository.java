package com.online.bus.ticket.reservation.booking.repository;

import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import org.springframework.data.repository.CrudRepository;

public interface TicketBookingRepository extends CrudRepository<TicketBooking, Long> {
}
