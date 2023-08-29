package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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
        transferDto = moneyTransferService.makeTransaction(transferDto);
        System.out.println("Transaction " + ++counter + " to database is done");
        acknowledgment.acknowledge();
        System.out.println("Acknowledge " + counter + " done");
        return transferDto;
    }
}
