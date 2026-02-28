package com.online.bus.ticket.reservation.booking.service;

import com.online.bus.ticket.reservation.booking.enums.BookingStatus;
import com.online.bus.ticket.reservation.booking.exception.PassengerException;
import com.online.bus.ticket.reservation.booking.exception.TicketBookingException;
import com.online.bus.ticket.reservation.booking.model.Passenger;
import com.online.bus.ticket.reservation.booking.model.TicketBooking;
import com.online.bus.ticket.reservation.booking.model.TicketBookingDetails;
import com.online.bus.ticket.reservation.booking.repository.TicketBookingDetailsRepository;
import com.online.bus.ticket.reservation.booking.repository.TicketBookingRepository;
import com.online.bus.ticket.reservation.booking.request.PassengerCancelRequest;
import com.online.bus.ticket.reservation.booking.request.TicketCancelRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class TicketCancelService {

    private final TicketBookingDetailsRepository ticketBookingDetailsRepository;
    private final TicketBookingRepository ticketBookingRepository;
    private final TicketBookingService ticketBookingService;
    private final PassengerService passengerService;

    public String cancelTickets(TicketCancelRequest ticketCancelRequest) {
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
                if (StringUtils.pathEquals(ticketBookingDetails.getStatus(), BookingStatus.PENDING.name()) ||
                        StringUtils.pathEquals(ticketBookingDetails.getStatus(), BookingStatus.CONFIRMED.name())) {

                    processCancelTickets(ticketCancelRequest, passengerCancelRequest, ticketBookingDetails, ticketBooking);
                }
            }
        }
        return "Cancellation Request has been processed";
    }

    private void processCancelTickets(TicketCancelRequest ticketCancelRequest, PassengerCancelRequest passengerCancelRequest,
                               TicketBookingDetails ticketBookingDetails, TicketBooking ticketBooking) {
        Passenger passenger = passengerService.getPassenger(ticketBookingDetails.getPassengerId());
        if (Objects.isNull(passenger)) {
            log.info("[Error]: Passenger details are not present for ticket cancel request {}", ticketCancelRequest);
            throw new PassengerException("Passenger details details are not present for ticket cancel request");
        }
        if (passengerCancelRequest.getPassengerId() == passenger.getPassengerId()) {
            ticketBookingDetails.setStatus(BookingStatus.CANCELLED.name());
            ticketBookingDetails.setReasonForCancellation(ticketCancelRequest.getReasonForCancellation());
            ticketBookingDetails.setCancellationDateTime(ticketCancelRequest.getCancellationDateTime());
            ticketBookingDetailsRepository.save(ticketBookingDetails);
            ticketBooking.setTotalSeats(ticketBooking.getTotalSeats() - 1);
            ticketBookingRepository.save(ticketBooking);
        }
    }
}
