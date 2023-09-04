package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsoleLoggerService {
    private Integer transferCounter = 0;
    private Long totalAmount = 0L;

    @KafkaListener(
            topics = "${kafka.topic-name}",
            groupId = "${kafka.group-id}",
            concurrency = "3",
            containerFactory = "kafkaListenerContainerFactory")
    public void log(@Payload MoneyTransferDto transferDto, Acknowledgment acknowledgment) {
        System.out.print("money transfer #" + (++transferCounter) + ": ");
        if (transferDto.isIncrease()) {
            System.out.print("add ");
            totalAmount += transferDto.getSum();
        } else {
            System.out.print("subtract ");
            totalAmount -= transferDto.getSum();
        }
        System.out.print(transferDto.getSum());
        System.out.println("; total amount: " + totalAmount);
        acknowledgment.acknowledge();
    }
}
