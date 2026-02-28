package com.online.bus.ticket.reservation.booking.repository;

import com.online.bus.ticket.reservation.booking.model.Passenger;
import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, Long> {
}
