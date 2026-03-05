package com.online.bus.ticket.reservation.booking.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @KafkaListener(topics = "booking-topic", groupId = "admin-group")
    public void consume(String message) {
        System.out.println("Received: " + message);
    }

    @KafkaListener(topics = "inventory-topic-update", groupId = "admin-group")
    public void consumeInventoryPaymentupdate(String message) throws JsonProcessingException {
        log.info("Received Message for booking update :{}", message);
        BookingUpdateRequest bookingUpdateRequest =
                objectMapper.readValue(message, BookingUpdateRequest.class);
        ticketReservationService.confirmTicket(bookingUpdateRequest.getBookingId());
        log.info("The message received: {} has been processed sucessfully.", message);
    }
}
