package com.online.bus.ticket.reservation.booking.repository;

import com.online.bus.ticket.reservation.booking.model.TicketBookingDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketBookingDetailsRepository extends CrudRepository<TicketBookingDetails, Long> {

    Optional<List<TicketBookingDetails>> findByBookingId(long ticketBookingDetailsId);
}
