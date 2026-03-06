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
import com.online.bus.ticket.reservation.booking.repository.TicketBookingRepository;
import com.online.bus.ticket.reservation.booking.request.PassengerCancelRequest;
import com.online.bus.ticket.reservation.booking.request.RefundRequest;
import com.online.bus.ticket.reservation.booking.request.TicketCancelRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TicketCancelService {

    @Autowired
    private TicketBookingDetailsRepository ticketBookingDetailsRepository;
    @Autowired
    private TicketBookingRepository ticketBookingRepository;
    @Autowired
    private TicketBookingService ticketBookingService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProducerService producerService;

    public String cancelTickets(TicketCancelRequest ticketCancelRequest) throws JsonProcessingException {
        log.info("Inside TicketCancelService: cancelTickets method");

        TicketBooking ticketBooking = ticketBookingService.getTicketBooking(ticketCancelRequest.getBookingId());
        if (Objects.isNull(ticketBooking)) {
            log.info("[Error]: Ticket Booking details are not present for ticket cancel request {}", ticketCancelRequest);
            throw new TicketBookingException("Ticket Booking details are not present for ticket cancel request");
        }

        List<TicketBookingDetails> ticketBookingDetailsList = ticketBookingDetailsRepository.findByBookingId(ticketCancelRequest.getBookingId()).orElse(null);
        if (CollectionUtils.isEmpty(ticketBookingDetailsList)) {
            log.info("[Error]: Ticket details are not available");
            throw new TicketBookingException("Ticket details are not available");
        }

        for (PassengerCancelRequest passengerCancelRequest : ticketCancelRequest.getPassengerCancelRequests()) {
            for(TicketBookingDetails ticketBookingDetails : ticketBookingDetailsList) {
                if (StringUtils.equalsIgnoreCase(ticketBookingDetails.getStatus(), BookingStatus.COMPLETED.name()) ||
                        StringUtils.equalsIgnoreCase(ticketBookingDetails.getStatus(), BookingStatus.PAID.name()) ||
                        StringUtils.equalsIgnoreCase(ticketBookingDetails.getStatus(), BookingStatus.CONFIRMED.name())) {

                    processCancelTickets(ticketCancelRequest, passengerCancelRequest, ticketBookingDetails, ticketBooking);
                }
                else {
                    log.info("[ERROR] The booking cannot be cancelled");
                }
            }
        }
        refundRequestInitiated(ticketCancelRequest);
        return "Cancellation Request has been processed";
    }

    private void refundRequestInitiated(TicketCancelRequest ticketCancelRequest) throws JsonProcessingException {
        TicketBooking ticketBooking = ticketBookingRepository.findById(ticketCancelRequest.getBookingId()).orElse(null);
        if (Objects.isNull(ticketBooking) || Objects.isNull(ticketBooking.getBookingId())) {
            log.info("[Error] Ticket booking id not available, hence cancellation is impossible");
        }
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setBookingId(ticketCancelRequest.getBookingId());
        refundRequest.setBusRouteNum(ticketBooking.getBookingId());
        refundRequest.setRefundedDateTime(LocalDateTime.now());
        refundRequest.setReasonForCancellation(ticketCancelRequest.getReasonForCancellation());

        String jsonMessage = objectMapper.writeValueAsString(refundRequest);
        producerService.sendMessageForCancelPayments(jsonMessage);
    }

    private void processCancelTickets(TicketCancelRequest ticketCancelRequest, PassengerCancelRequest passengerCancelRequest,
                               TicketBookingDetails ticketBookingDetails, TicketBooking ticketBooking) {
        Passenger passenger = passengerService.getPassenger(ticketBookingDetails.getPassengerId());
        if (Objects.isNull(passenger)) {
            log.info("[Error]: Passenger details are not present for ticket cancel request {}", ticketCancelRequest);
            throw new PassengerException("Passenger details details are not present for ticket cancel request");
        }
        if (passengerCancelRequest.getPassengerId() == passenger.getPassengerId()) {
            ticketBookingDetails.setStatus(BookingStatus.CANCELLATION_REQUESTED.name());
            ticketBookingDetails.setReasonForCancellation(ticketCancelRequest.getReasonForCancellation());
            ticketBookingDetails.setCancellationDateTime(ticketCancelRequest.getCancellationDateTime());
            ticketBookingDetailsRepository.save(ticketBookingDetails);
            ticketBooking.setTotalSeats(ticketBooking.getTotalSeats() - 1);
            ticketBookingRepository.save(ticketBooking);
        }
    }
}
