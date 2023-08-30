package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsoleLoggerService {
    private Integer transferCounter = 0;
    private Long totalAmount = 0L;

    @KafkaListener(
            topics = "${kafka.topic-name}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "3")
    public void log(@Payload MoneyTransferDto transfer, Acknowledgment acknowledgment) {
        System.out.print("money transfer #" + (++transferCounter) + ": ");
        if (transfer.isIncrease()) {
            System.out.print("add ");
            totalAmount += transfer.getSum();
        } else {
            System.out.print("subtract ");
            totalAmount -= transfer.getSum();
        }
        System.out.print(transfer.getSum());
        System.out.println("; total amount: " + totalAmount);
        acknowledgment.acknowledge();
    }
}
