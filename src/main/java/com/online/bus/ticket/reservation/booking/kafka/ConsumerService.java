package com.online.bus.ticket.reservation.booking.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.bus.ticket.reservation.booking.enums.BookingStatus;
import com.online.bus.ticket.reservation.booking.request.BookingUpdateRequest;
import com.online.bus.ticket.reservation.booking.service.TicketReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TicketReservationService ticketReservationService;

    @KafkaListener(topics = "inventory-topic-update", groupId = "admin-group")
    public void consumeInventoryPaymentUpdate(String message) throws JsonProcessingException {
        log.info("In ConsumerService consumeInventoryPaymentUpdate method, Received Message for booking update :{}", message);
        BookingUpdateRequest bookingUpdateRequest =
                objectMapper.readValue(message, BookingUpdateRequest.class);
        ticketReservationService.updateTicketStatus(bookingUpdateRequest.getBookingId(), BookingStatus.CONFIRMED);
        log.info("The message: {} has been processed and updated sucessfully.", message);
    }

    @KafkaListener(topics = "inventory-topic-delete", groupId = "admin-group")
    public void consumeInventoryPaymentCancel(String message) throws JsonProcessingException {
        log.info("In ConsumerService consumeInventoryPaymentCancel method, Received Message for booking cancel :{}", message);
        BookingUpdateRequest bookingUpdateRequest =
                objectMapper.readValue(message, BookingUpdateRequest.class);
        ticketReservationService.updateTicketStatus(bookingUpdateRequest.getBookingId(), BookingStatus.CANCELLED);
        log.info("The message: {} has been processed and cancelled sucessfully.", message);
    }

    @KafkaListener(topics = "payment-topic-reject", groupId = "admin-group")
    public void consumePaymentReject(String message) throws JsonProcessingException {
        log.info("In ConsumerService consumePaymentReject method, Received Message for booking reject :{}", message);
        BookingUpdateRequest bookingUpdateRequest =
                objectMapper.readValue(message, BookingUpdateRequest.class);
        ticketReservationService.updateTicketStatus(bookingUpdateRequest.getBookingId(), BookingStatus.REJECTED);
        log.info("The message: {} has been processed and rejected sucessfully.", message);
    }
}
