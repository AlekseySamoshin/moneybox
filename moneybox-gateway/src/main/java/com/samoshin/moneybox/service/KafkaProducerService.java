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
    private final KafkaTemplate<String, MoneyTransferDto> kafkaTransferTemplate;
    private final KafkaTemplate<String, String> kafkaInfoTemplate;

    @Value("${transfer-topic-name}")
    private String transferTopicName;

    @Value("${info-topic-name}")
    private String infoTopicName;

    public MoneyTransferDto sendTransfer(MoneyTransferDto transfer) {
        try {
            kafkaTransferTemplate.send(transferTopicName,"transfer", transfer);
        } catch(Exception e) {
            log.error("Money transfer error: " + e.toString());
        }
        log.info("Kafka Producer Service: Money transfer request has been sent to Kafka broker: {}", transfer);
        return transfer;
    }

    public String getInfo(Long moneyboxId) {
        try {
            kafkaInfoTemplate.send(infoTopicName, "info", moneyboxId.toString());
        } catch(Exception e) {
            log.error("Get info error: " + e.toString());
        }
        log.info("Kafka Producer Service: Get info request has been sent to Kafka broker: {}", moneyboxId);
        return "Request to get info was sended for moneyboxId=" + moneyboxId.toString();
    }
}
