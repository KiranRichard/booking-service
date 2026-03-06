package com.online.bus.ticket.reservation.booking.service;

import com.online.bus.ticket.reservation.booking.exception.TicketBookingException;
import com.online.bus.ticket.reservation.booking.response.BusInventory;
import com.online.bus.ticket.reservation.booking.validator.TicketBookingRequestValidator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class BusInventoryClientService {

    private final WebClient inventoryWebClient;
    private final TicketBookingRequestValidator ticketBookingRequestValidator;

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fetchSeatAvailabilityDetailsFallback")
    public BusInventory fetchSeatAvailabilityDetails(long busRouteNumber) {
        log.info("Inside BusInventoryClientService fetchSeatAvailabilityDetails() method");

        ticketBookingRequestValidator.validateBusNumber(busRouteNumber);
        try {
            return inventoryWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/inventory/buses/busRoute/"+busRouteNumber).build())
                    .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(BusInventory.class).block();
        }
        catch (Exception ex) {
            log.info("[Exception] There was an error occurred while fetching the details from inventory services: {}", ex.getMessage());
            throw new TicketBookingException("Exception occurred while fetching the details from inventory services");
        }
    }

    public BusInventory fetchSeatAvailabilityDetailsFallback(long busRouteNumber, Exception ex) {
        log.info("Inside BusInventoryClientService is down");

        BusInventory busInventory = new BusInventory();
        busInventory.setBusRouteNumber(busRouteNumber);
        busInventory.setTotalSeats(0);
        busInventory.setAvailableSeats(0);
        busInventory.setPrice(0);
        busInventory.setCreatedDateTime(LocalDateTime.now());
        busInventory.setUpdatedDateTime(LocalDateTime.now());
        return busInventory;
    }
}
