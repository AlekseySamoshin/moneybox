package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, MoneyTransferDto> kafkaTemplate;

    @Value("${kafka.topic-name}")
    private String topicName;

    public MoneyTransferDto sendTransfer(MoneyTransferDto transfer) {
        try {
            kafkaTemplate.send(topicName, transfer);
        } catch(Exception e) {
            System.out.print("ERROR on money transfer: \n" + e.toString() + "\n");
        }
        return transfer;
    }
}
