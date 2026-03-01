package com.online.bus.ticket.reservation.booking.validator;

import com.online.bus.ticket.reservation.booking.enums.BookingStatus;
import com.online.bus.ticket.reservation.booking.exception.RequiredFieldsMissingException;
import com.online.bus.ticket.reservation.booking.request.TicketBookingRequest;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class TicketBookingRequestValidator {

    public void validateTicketBookingRequest(TicketBookingRequest ticketBookingRequest) {

        if (Objects.isNull(ticketBookingRequest)) {
            log.info("[Error]: Invalid Create Ticket Booking request is null");
            throw new RequiredFieldsMissingException("Invalid Ticket Booking request is null");
        }

        if(ticketBookingRequest.getBusNumber()<=0) {
            log.info("[Error]: Invalid bus number in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid bus number in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(Objects.isNull(ticketBookingRequest.getBookingDateTime())) {
            log.info("[Error]: Invalid booking date in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid booking date in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(Objects.isNull(ticketBookingRequest.getTravelDateTime())) {
            log.info("[Error]: Invalid travel date in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid travel date in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(StringUtils.isBlank(ticketBookingRequest.getSource())) {
            log.info("[Error]: Invalid source in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid source in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(StringUtils.isBlank(ticketBookingRequest.getDestination())) {
            log.info("[Error]: Invalid destination in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid destination in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(ticketBookingRequest.getTotalSeats()<=0) {
            log.info("[Error]: Invalid total seats in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid total seats in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(ticketBookingRequest.getBookedBy()<=0) {
            log.info("[Error]: Invalid booked by in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid booked by in Ticket Booking request: {}"+ ticketBookingRequest);
        }

        if(StringUtils.isBlank(ticketBookingRequest.getStatus()) || Objects.isNull(BookingStatus.findByName(ticketBookingRequest.getStatus()))) {
            log.info("[Error]: Invalid status in Ticket Booking request: {}", ticketBookingRequest);
            throw new RequiredFieldsMissingException("Invalid status in Ticket Booking request: {}"+ ticketBookingRequest);
        }
    }

    public void validateTicketBookingId(long ticketBookingId) {
        if (ticketBookingId<=0) {
            log.info("[Error]: Invalid ticketBookingId field in request: {}", ticketBookingId);
            throw new RequiredFieldsMissingException("Invalid ticketBookingId field in request: {}"+ ticketBookingId);
        }
    }

    public void validateBusNumber(long busNumber) {
        if(busNumber<=0) {
            log.info("[Error]: Invalid bus number is Ticket Booking request");
            throw new RequiredFieldsMissingException("Invalid bus number is Ticket Booking request");
        }
    }
}
