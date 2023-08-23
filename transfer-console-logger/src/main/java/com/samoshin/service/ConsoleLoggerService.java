package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsoleLoggerService {
    private Long totalAmount = 0L;

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void log(MoneyTransferDto transfer) {
        System.out.print("money transfer: ");
        if (transfer.isIncrease()) {
            System.out.print("add ");
            totalAmount += transfer.getSum();
        } else {
            System.out.print("subtract ");
            totalAmount -= transfer.getSum();
        }
        System.out.print(transfer.getSum());
        System.out.println("; total amount: " + totalAmount);
    }
}
