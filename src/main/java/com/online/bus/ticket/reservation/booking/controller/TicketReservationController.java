package com.online.bus.ticket.reservation.booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.online.bus.ticket.reservation.booking.request.TicketReservationRequest;
import com.online.bus.ticket.reservation.booking.response.TicketDetails;
import com.online.bus.ticket.reservation.booking.service.TicketReservationService;
import com.online.bus.ticket.reservation.booking.validator.TicketReservationValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/reserve-tickets")
public class TicketReservationController {

    @Autowired
    private TicketReservationService ticketReservationService;
    @Autowired
    private TicketReservationValidator ticketReservationValidator;

    @PostMapping
    public String bookTickets(@RequestBody TicketReservationRequest ticketReservationRequest) throws JsonProcessingException {
        log.info("Inside TicketReservationController: bookTickets method");

        ticketReservationValidator.validateTicketBookingDetailsRequest(ticketReservationRequest);
        return ticketReservationService.createTicketBookingDetails(ticketReservationRequest);
    }

    @GetMapping("/{ticketBookingDetailsId}")
    public TicketDetails getTicketDetails(@PathVariable("ticketBookingDetailsId") long ticketBookingDetailsId) {
        log.info("Inside TicketReservationController: getTicketDetails method");

        ticketReservationValidator.validateTicketBookingDetailsId(ticketBookingDetailsId);
        return ticketReservationService.getTickets(ticketBookingDetailsId);
    }
}
