package com.online.bus.ticket.reservation.booking.controller;

import com.online.bus.ticket.reservation.booking.model.Passenger;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import com.online.bus.ticket.reservation.booking.service.PassengerService;
import com.online.bus.ticket.reservation.booking.validator.AddressValidator;
import com.online.bus.ticket.reservation.booking.validator.PassengerRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/passengers")
@RestController
@AllArgsConstructor
public class PassengerController {

    private final PassengerRequestValidator passengerRequestValidator;
    private final AddressValidator addressValidator;
    private final PassengerService passengerService;

    @PostMapping
    public Passenger createPassenger(@RequestBody PassengerRequest passengerRequest) {
        log.info("Inside PassengerController createPassenger Method");
        passengerRequestValidator.validatePassengerRequest(passengerRequest);
        addressValidator.validateAddressRequest(passengerRequest.getAddress());
        return passengerService.createPassenger(passengerRequest);
    }

    @GetMapping("/{passengerId}")
    public Passenger getPassenger(@PathVariable("passengerId") long passengerId) {
        log.info("Inside PassengerController getPassenger Method with passengerId: {}", passengerId);
        passengerRequestValidator.validatePassengerId(passengerId);
        return passengerService.getPassenger(passengerId);
    }

    @GetMapping()
    public List<Passenger> getPassengers() {
        log.info("Inside PassengerController getPassengers Method");
        return passengerService.getPassengers();
    }

    @PutMapping("/{passengerId}")
    public Passenger editPassenger(@RequestBody PassengerRequest passengerRequest, @PathVariable("passengerId") long passengerId) {
        log.info("Inside PassengerController editPassenger Method with passengerId: {}", passengerId);
        passengerRequestValidator.validatePassengerId(passengerId);
        passengerRequestValidator.validatePassengerRequest(passengerRequest);
        addressValidator.validateAddressRequest(passengerRequest.getAddress());
        return passengerService.editPassenger(passengerRequest, passengerId);
    }

    @DeleteMapping("/{passengerId}")
    public void deletePassenger(@PathVariable("passengerId") long passengerId) {
        log.info("Inside PassengerController deletePassenger Method with passengerId: {}", passengerId);
        passengerRequestValidator.validatePassengerId(passengerId);
        passengerService.deletePassenger(passengerId);
    }
}
