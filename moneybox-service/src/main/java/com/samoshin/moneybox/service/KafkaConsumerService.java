package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MoneyTransferService moneyTransferService;

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")//, concurrency = "3")
    public MoneyTransferDto consumeTransfer(MoneyTransferDto transferDto) {
        return moneyTransferService.makeTransaction(transferDto);
    }
}
