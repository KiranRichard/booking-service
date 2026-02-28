package com.online.bus.ticket.reservation.booking.service;

import com.online.bus.ticket.reservation.booking.enums.BookingStatus;
import com.online.bus.ticket.reservation.booking.exception.PassengerException;
import com.online.bus.ticket.reservation.booking.exception.TicketBookingException;
import com.online.bus.ticket.reservation.booking.model.Passenger;
import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import com.online.bus.ticket.reservation.booking.model.TicketBookingDetails;
import com.online.bus.ticket.reservation.booking.repository.TicketBookingDetailsRepository;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import com.online.bus.ticket.reservation.booking.request.TicketReservationRequest;
import com.online.bus.ticket.reservation.booking.response.AddressResponse;
import com.online.bus.ticket.reservation.booking.response.PassengerResponse;
import com.online.bus.ticket.reservation.booking.response.TicketBookingResponse;
import com.online.bus.ticket.reservation.booking.response.TicketDetails;
import com.online.bus.ticket.reservation.booking.validator.PassengerRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class TicketReservationService {

    private final TicketBookingDetailsRepository ticketBookingDetailsRepository;
    private final TicketBookingService ticketBookingService;
    private final PassengerService passengerService;
    private final PassengerRequestValidator passengerRequestValidator;

    public String createTicketBookingDetails(TicketReservationRequest ticketReservationRequest) {
        log.info("Inside TicketBookingDetailsService: createTicketBookingDetails method");

        TicketBooking ticketBooking = ticketBookingService.createTicketBooking(ticketReservationRequest.getTicketBookingRequest());
        if (ticketBooking.getBookingId()<=0) {
            log.info("[Error]: Ticket Booking creation in the database failed: {}", ticketBooking);
            throw new TicketBookingException("[Error]: Ticket Booking creation in the database failed");
        }
        for (PassengerRequest passengerRequest : ticketReservationRequest.getPassengerRequests()) {
            Passenger passenger = passengerService.createPassenger(passengerRequest);
            if (passenger.getPassengerId()<=0) {
                log.info("[Error]: Passenger creation in the database failed: {}", passenger);
                throw new PassengerException("[Error]: Passenger creation in the database failed");
            }
            saveTicketBookingDetails(ticketBooking.getBookingId(), passenger.getPassengerId());
        }
        return "Ticket Booking initiated successfully";
    }

    private void saveTicketBookingDetails(long bookingId, long passengerId) {
        TicketBookingDetails ticketBookingDetails = new TicketBookingDetails();
        ticketBookingDetails.setBookingId(bookingId);
        ticketBookingDetails.setPassengerId(passengerId);
        ticketBookingDetails.setStatus(BookingStatus.PENDING.name());
        ticketBookingDetailsRepository.save(ticketBookingDetails);
    }

    public TicketDetails getTickets(long ticketBookingDetailsId) {
        log.info("Inside TicketBookingDetailsService: getTickets method");

        List<TicketBookingDetails> ticketBookingDetails = ticketBookingDetailsRepository.findByBookingId(ticketBookingDetailsId).orElse(null);
        if (CollectionUtils.isEmpty(ticketBookingDetails)) {
            log.info("[Error]: Ticket details are not available");
            throw new TicketBookingException("Ticket details are not available");
        }
        TicketBooking ticketBooking = ticketBookingService.getTicketBooking(ticketBookingDetailsId);
        if (Objects.isNull(ticketBooking)) {
            log.info("[Error]: Ticket booking details are not available");
            throw new TicketBookingException("Ticket booking details are not available");
        }
        List<Passenger> passengers = new ArrayList<>();
        for (TicketBookingDetails ticketBookingDetail : ticketBookingDetails) {
            passengerRequestValidator.validatePassengerId(ticketBookingDetail.getPassengerId());
            Passenger passenger = passengerService.getPassenger(ticketBookingDetail.getPassengerId());
            passengers.add(passenger);
        }
        return buildTicketDetails(ticketBooking, passengers);
    }

    private TicketDetails buildTicketDetails(TicketBooking ticketBooking, List<Passenger> passengers) {

        TicketDetails ticketDetails = new TicketDetails();
        if (Objects.nonNull(ticketBooking)) {
            ticketDetails.setTicketBookingResponse(buildTicketBookingResponse(ticketBooking));
        }
        if (!CollectionUtils.isEmpty(passengers)) {
            ticketDetails.setPassengers(buildPassengersResponse(passengers));
        }
        return ticketDetails;
    }

    private List<PassengerResponse> buildPassengersResponse(List<Passenger> passengers) {
        List<PassengerResponse> passengerResponseList = new ArrayList<>();
        for(Passenger passenger : passengers) {
            if (Objects.nonNull(passenger)) {
                PassengerResponse passengerResponse = new PassengerResponse();
                passengerResponse.setPassengerId(passenger.getPassengerId());
                passengerResponse.setFirstName(passenger.getFirstName());
                passengerResponse.setLastName(passenger.getLastName());
                passengerResponse.setAge(passenger.getAge());
                passengerResponse.setGender(passengerResponse.getGender());
                passengerResponse.setEmailId(passengerResponse.getEmailId());
                passengerResponse.setContactNumber(passenger.getContactNumber());
                AddressResponse addressResponse = new AddressResponse();
                addressResponse.setBuildingNumber(passenger.getBuildingNumber());
                addressResponse.setStreetName(passenger.getStreetName());
                addressResponse.setAddressLine(passenger.getAddressLine());
                addressResponse.setCity(passenger.getCity());
                addressResponse.setState(passenger.getState());
                addressResponse.setZipCode(passenger.getZipCode());
                passengerResponse.setAddressResponse(addressResponse);
                passengerResponseList.add(passengerResponse);
            }
        }
        return passengerResponseList;
    }

    private TicketBookingResponse buildTicketBookingResponse(TicketBooking ticketBooking) {

        TicketBookingResponse ticketBookingResponse = new TicketBookingResponse();
        ticketBookingResponse.setBookingId(ticketBooking.getBookingId());
        ticketBookingResponse.setBusNumber(ticketBooking.getBusNumber());
        ticketBookingResponse.setBookingDateTime(ticketBooking.getBookingDateTime());
        ticketBookingResponse.setTravelDateTime(ticketBooking.getTravelDateTime());
        ticketBookingResponse.setSource(ticketBooking.getSource());
        ticketBookingResponse.setDestination(ticketBooking.getDestination());
        ticketBookingResponse.setTotalSeats(ticketBooking.getTotalSeats());
        ticketBookingResponse.setBookedBy(ticketBooking.getBookedBy());
        ticketBookingResponse.setStatus(ticketBooking.getStatus());
        ticketBookingResponse.setCreatedDateTime(ticketBooking.getCreatedDateTime());
        ticketBookingResponse.setUpdatedDateTime(ticketBooking.getUpdatedDateTime());
        return ticketBookingResponse;
    }
}
