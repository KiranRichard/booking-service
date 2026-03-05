package com.online.bus.ticket.reservation.booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.bus.ticket.reservation.booking.enums.BookingStatus;
import com.online.bus.ticket.reservation.booking.exception.PassengerException;
import com.online.bus.ticket.reservation.booking.exception.TicketBookingException;
import com.online.bus.ticket.reservation.booking.kafka.ProducerService;
import com.online.bus.ticket.reservation.booking.model.Passenger;
import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import com.online.bus.ticket.reservation.booking.model.TicketBookingDetails;
import com.online.bus.ticket.reservation.booking.repository.TicketBookingDetailsRepository;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import com.online.bus.ticket.reservation.booking.request.PaymentRequest;
import com.online.bus.ticket.reservation.booking.request.TicketReservationRequest;
import com.online.bus.ticket.reservation.booking.response.*;
import com.online.bus.ticket.reservation.booking.validator.PassengerRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TicketReservationService {

    @Autowired
    private TicketBookingDetailsRepository ticketBookingDetailsRepository;
    @Autowired
    private TicketBookingService ticketBookingService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private BusInventoryClientService busInventoryClientService;
    @Autowired
    private PassengerRequestValidator passengerRequestValidator;
    @Autowired
    private ObjectMapper objectMapper;

    public String createTicketBookingDetails(TicketReservationRequest ticketReservationRequest) throws JsonProcessingException {
        log.info("Inside TicketBookingDetailsService: createTicketBookingDetails method");

        //TO DO: Check for the availability of seats Rest api call to inventory microservice
        BusInventory busInventory = busInventoryClientService.fetchSeatAvailabilityDetails
                (ticketReservationRequest.getTicketBookingRequest().getBusNumber());
        if (Objects.isNull(busInventory)) {
            log.info("[Error] The bus inventory details are not found");
            throw new TicketBookingException("The bus inventory details are not found");
        }
        if (ticketReservationRequest.getTicketBookingRequest().getTotalSeats() <= busInventory.getAvailableSeats()){
            TicketBooking ticketBooking = performBooking(ticketReservationRequest);
            if (Objects.nonNull(ticketBooking)) {
                //TO Do: kafka message to payment microservice
                calculateBillingAndInitiatePayment(busInventory.getPrice(), ticketBooking);
            }
            else {
                log.info("[Error]: Number of passengers is zero, hence transaction is terminated");
                throw new TicketBookingException("Number of passengers is zero, hence transaction is terminated");
            }
        }
        else {
            log.info("[Error] Seats are not available");
            throw new TicketBookingException("Seats are not available, booking can't be proceeded");
        }
        //TO Do: kafka message to payment microservice
        return "Ticket Booking initiated successfully";
    }

    private void calculateBillingAndInitiatePayment(double price, TicketBooking ticketBooking) throws JsonProcessingException {
        double totalAmount = price * ticketBooking.getTotalSeats();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(ticketBooking.getBookingId());
        paymentRequest.setAmount(totalAmount);
        paymentRequest.setPaymentStatus(BookingStatus.PENDING.name());
        paymentRequest.setPaymentDateTime(LocalDateTime.now());
        paymentRequest.setBusRouteNum(ticketBooking.getBusNumber());
        paymentRequest.setNoOfSeatsBooked(ticketBooking.getTotalSeats());

        String jsonMessage = objectMapper.writeValueAsString(paymentRequest);
        producerService.sendMessageForInsertPayments(jsonMessage);
    }

    private TicketBooking performBooking(TicketReservationRequest ticketReservationRequest) {
        //TO Do: Checks if the seats are available for booking : then proceed, else stop
        TicketBooking ticketBooking = ticketBookingService.createTicketBooking(ticketReservationRequest.getTicketBookingRequest());
        if (ticketBooking.getBookingId()<=0) {
            log.info("[Error]: Ticket Booking creation in the database failed: {}", ticketBooking);
            throw new TicketBookingException("[Error]: Ticket Booking creation in the database failed");
        }
        int passengerCount = 0;
        for (PassengerRequest passengerRequest : ticketReservationRequest.getPassengerRequests()) {
            Passenger passenger = passengerService.createPassenger(passengerRequest);
            if (passenger.getPassengerId()<=0) {
                log.info("[Error]: Passenger creation in the database failed: {}", passenger);
                throw new PassengerException("[Error]: Passenger creation in the database failed");
            }
            passengerCount = passengerCount + 1;
            saveTicketBookingDetails(ticketBooking.getBookingId(), passenger.getPassengerId());
        }
        return ticketBooking;
    }

    private TicketBookingDetails saveTicketBookingDetails(long bookingId, long passengerId) {
        TicketBookingDetails ticketBookingDetails = new TicketBookingDetails();
        ticketBookingDetails.setBookingId(bookingId);
        ticketBookingDetails.setPassengerId(passengerId);
        ticketBookingDetails.setStatus(BookingStatus.PENDING.name());
        return ticketBookingDetailsRepository.save(ticketBookingDetails);
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
        ticketBookingResponse.setCreatedDateTime(ticketBooking.getCreatedDateTime());
        ticketBookingResponse.setUpdatedDateTime(ticketBooking.getUpdatedDateTime());
        return ticketBookingResponse;
    }
}
