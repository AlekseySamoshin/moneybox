package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    @Value("${kafka.topic-name}")
    private String topicName;
    private final KafkaTemplate<String, MoneyTransferDto> kafkaTemplate;

    public MoneyTransferDto sendTransfer(MoneyTransferDto transfer) {
        try {
            kafkaTemplate.send(topicName, transfer);
        } catch(Exception e) {
            System.out.println("ERROR on money transfer: \n" + e.getStackTrace());
        }
        return transfer;
    }
}
