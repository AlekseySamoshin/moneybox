package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.moneybox.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MoneyTransferService moneyTransferService;

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public MoneyTransferDto consumeTransfer(MoneyTransferDto transfer) {
        return moneyTransferService.makeTransaction(transfer);
    }
}
