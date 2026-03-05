package com.online.bus.ticket.reservation.booking.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    @KafkaListener(topics = "booking-topic", groupId = "admin-group")
    public void consume(String message) {
        System.out.println("Received: " + message);
    }
}
