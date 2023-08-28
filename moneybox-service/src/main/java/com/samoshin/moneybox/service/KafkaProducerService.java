package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MoneyTransferDto sendTransfer(MoneyTransferDto transfer) {
        try {
            kafkaTemplate.send("${kafka.topic-name}", transfer);
        } catch(Exception e) {
            System.out.println("ERROR on money transfer: \n" + e.getStackTrace());
        }
        return transfer;
    }
}
