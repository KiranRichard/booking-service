package com.online.bus.ticket.reservation.booking.controller;

import com.online.bus.ticket.reservation.booking.request.TicketCancelRequest;
import com.online.bus.ticket.reservation.booking.service.TicketCancelService;
import com.online.bus.ticket.reservation.booking.validator.TicketCancelRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/cancel-tickets")
public class TicketCancellationController {

    private final TicketCancelService ticketCancelService;
    private final TicketCancelRequestValidator ticketCancelRequestValidator;

    @PostMapping
    public String cancelTickets(@RequestBody TicketCancelRequest ticketCancelRequest){
        log.info("Inside TicketCancellationController cancelTickets");

        ticketCancelRequestValidator.validateTicketCancelRequest(ticketCancelRequest);
        return ticketCancelService.cancelTickets(ticketCancelRequest);
    }
}
