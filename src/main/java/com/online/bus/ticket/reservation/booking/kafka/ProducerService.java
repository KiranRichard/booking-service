package com.online.bus.ticket.reservation.booking.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageForInsertPayments(String message) {
        kafkaTemplate.send("booking-topic-insert", message);
    }

    public void sendMessageForCancelPayments(String message) {
        kafkaTemplate.send("booking-topic-delete", message);
    }
}
