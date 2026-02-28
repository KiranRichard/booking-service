package com.online.bus.ticket.reservation.booking.controller;

import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import com.online.bus.ticket.reservation.booking.request.TicketBookingRequest;
import com.online.bus.ticket.reservation.booking.service.TicketBookingService;
import com.online.bus.ticket.reservation.booking.validator.TicketBookingRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/booking-tickets")
@RestController
@AllArgsConstructor
public class TicketBookingController {

    private final TicketBookingRequestValidator ticketBookingRequestValidator;
    private final TicketBookingService ticketBookingService;

    @PostMapping
    public TicketBooking createTicketBooking(@RequestBody TicketBookingRequest ticketBookingRequest) {
        log.info("Inside TicketBookingController createTicketBooking Method");
        ticketBookingRequestValidator.validateTicketBookingRequest(ticketBookingRequest);
        return ticketBookingService.createTicketBooking(ticketBookingRequest);
    }

    @GetMapping("/{bookingId}")
    public TicketBooking getTicketBooking(@PathVariable("bookingId") long bookingId) {
        log.info("Inside TicketBookingController getTicketBooking Method with bookingId: {}", bookingId);
        ticketBookingRequestValidator.validateTicketBookingId(bookingId);
        return ticketBookingService.getTicketBooking(bookingId);
    }

    @GetMapping()
    public List<TicketBooking> getTicketBookings() {
        log.info("Inside TicketBookingController getTicketBookings Method");
        return ticketBookingService.getTicketBookings();
    }

    @PutMapping("/{bookingId}")
    public TicketBooking editTicketBooking(@RequestBody TicketBookingRequest ticketBookingRequest, @PathVariable("bookingId") long bookingId) {
        log.info("Inside TicketBookingController editTicketBooking Method with bookingId: {}", bookingId);
        ticketBookingRequestValidator.validateTicketBookingId(bookingId);
        ticketBookingRequestValidator.validateTicketBookingRequest(ticketBookingRequest);
        return ticketBookingService.editTicketBooking(ticketBookingRequest, bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteTicketBooking(@PathVariable("bookingId") long bookingId) {
        log.info("Inside TicketBookingController deleteTicketBooking Method with bookingId: {}", bookingId);
        ticketBookingRequestValidator.validateTicketBookingId(bookingId);
        ticketBookingService.deleteTicketBooking(bookingId);
    }
}
