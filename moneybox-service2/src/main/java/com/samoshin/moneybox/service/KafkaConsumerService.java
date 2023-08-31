package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MoneyTransferService moneyTransferService;
    Integer counter = 0;

    @KafkaListener(
            topics = "${kafka.topic-name}",
            groupId = "${kafka.group-id}",
            concurrency = "3",
            containerFactory = "kafkaListenerContainerFactory")
    public MoneyTransferDto consumeTransfer(@Payload MoneyTransferDto transferDto, Acknowledgment acknowledgment) {
        log.info("Kafka Consumer Service: received message from Kafka broker: {}", transferDto);
//        transferDto = moneyTransferService.makeTransaction(transferDto);
        log.info("Transaction to database is done. " + transferDto);
        acknowledgment.acknowledge();
        log.info("Acknowledge transfer to kafka broker is done.");
        return transferDto;
    }
}
