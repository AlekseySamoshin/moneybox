package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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
            log.error("Money transfer error: " + e.toString());
        }
        log.info("Kafka Producer Service: Money transfer request has been sent to Kafka broker: {}", transfer);
        return transfer;
    }
}
