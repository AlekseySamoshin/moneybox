package com.samoshin.moneybox.service;

import com.samoshin.moneybox.constant.Constant;
import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.model.MoneyTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaTransferConsumerService {
    private final MoneyTransferService moneyTransferService;

    @KafkaListener(topics = Constant.TOPIC_NAME, groupId = Constant.GROUP_ID)
    public MoneyTransferDto consumeTransfer(MoneyTransfer transfer) {
        return moneyTransferService.makeTransaction(transfer);
    }
}
