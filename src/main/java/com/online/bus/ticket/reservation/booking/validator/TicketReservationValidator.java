package com.online.bus.ticket.reservation.booking.validator;

import com.online.bus.ticket.reservation.booking.exception.RequiredFieldsMissingException;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import com.online.bus.ticket.reservation.booking.request.TicketReservationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class TicketReservationValidator {

    private final TicketBookingRequestValidator ticketBookingRequestValidator;
    private final PassengerRequestValidator passengerRequestValidator;
    private final AddressValidator addressValidator;

    public void validateTicketBookingDetailsRequest(TicketReservationRequest ticketReservationRequest) {

        log.info("Inside TicketReservationValidator: validateTicketBookingDetailsRequest method");
        if (Objects.isNull(ticketReservationRequest)) {
            log.info("[Error]: Invalid Ticket Booking Details Request is null");
            throw new RequiredFieldsMissingException("Invalid Ticket Booking Details Request is null");
        }

        ticketBookingRequestValidator.validateTicketBookingRequest(ticketReservationRequest.getTicketBookingRequest());
        if (!CollectionUtils.isEmpty(ticketReservationRequest.getPassengerRequests())) {
            for(PassengerRequest passengerRequest : ticketReservationRequest.getPassengerRequests()) {
                if(Objects.nonNull(passengerRequest)) {
                    passengerRequestValidator.validatePassengerRequest(passengerRequest);
                    addressValidator.validateAddressRequest(passengerRequest.getAddress());
                }
                else {
                    log.info("[Error]: Invalid Create passenger request is null");
                    throw new RequiredFieldsMissingException("Invalid Create passenger request is null");
                }
            }
        }
        else {
            log.info("[Error]: Invalid Create passenger request is null");
            throw new RequiredFieldsMissingException("Invalid Create passenger request is null");
        }
    }

    public void validateTicketBookingDetailsId(long ticketBookingDetailsId) {
        if (ticketBookingDetailsId<=0) {
            log.info("[Error]: Booking details Id is missing");
            throw new RequiredFieldsMissingException("Booking details Id is missing");
        }
    }
}
