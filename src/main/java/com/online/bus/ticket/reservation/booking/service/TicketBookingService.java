package com.online.bus.ticket.reservation.booking.service;

import com.online.bus.ticket.reservation.booking.exception.TicketBookingException;
import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import com.online.bus.ticket.reservation.booking.repository.TicketBookingRepository;
import com.online.bus.ticket.reservation.booking.request.TicketBookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TicketBookingService {

    @Autowired
    private TicketBookingRepository ticketBookingRepository;

    public TicketBooking createTicketBooking(TicketBookingRequest ticketBookingRequest){
        TicketBooking ticketBooking = buildTicketBooking(ticketBookingRequest, new TicketBooking());
        return ticketBookingRepository.save(ticketBooking);
    }

    public TicketBooking getTicketBooking(long bookingId) {
        TicketBooking ticketBooking = ticketBookingRepository.findById(bookingId).orElse(null);
        if (Objects.isNull(ticketBooking)){
            throw new TicketBookingException("Ticket Booking details is not present");
        }
        return ticketBooking;
    }

    public TicketBooking editTicketBooking(TicketBookingRequest ticketBookingRequest, long bookingId) {
        TicketBooking ticketBooking = ticketBookingRepository.findById(bookingId).orElse(null);
        if (Objects.isNull(ticketBooking)){
            throw new TicketBookingException("Ticket Booking details is not present and unable to update");
        }
        return ticketBookingRepository.save(buildTicketBooking(ticketBookingRequest, ticketBooking));
    }

    public void deleteTicketBooking(long bookingId) {
        if (ticketBookingRepository.findById(bookingId).isEmpty()) {
            throw new TicketBookingException("Ticket booking details is not present and unable to delete");
        }
        ticketBookingRepository.deleteById(bookingId);
    }

    public List<TicketBooking> getTicketBookings() {
        return (List<TicketBooking>) ticketBookingRepository.findAll();
    }

    private TicketBooking buildTicketBooking(TicketBookingRequest ticketBookingRequest, TicketBooking ticketBooking) {
        ticketBooking.setBusNumber(ticketBookingRequest.getBusNumber());
        ticketBooking.setBookingDateTime(ticketBookingRequest.getBookingDateTime());
        ticketBooking.setTravelDateTime(ticketBookingRequest.getTravelDateTime());
        ticketBooking.setSource(ticketBookingRequest.getSource());
        ticketBooking.setDestination(ticketBookingRequest.getDestination());
        ticketBooking.setTotalSeats(ticketBookingRequest.getTotalSeats());
        ticketBooking.setBookedBy(ticketBookingRequest.getBookedBy());
        return ticketBooking;
    }
}
