package com.samoshin.moneybox.service;

import com.samoshin.moneybox.constant.Constant;
import com.samoshin.moneybox.model.MoneyTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaTransferProducerService {
    private final KafkaTemplate<String, MoneyTransfer> kafkaTemplate;

    public MoneyTransfer sendTransfer(MoneyTransfer transfer) {
        try {
            transfer = kafkaTemplate.send(Constant.TOPIC_NAME, transfer).get().getProducerRecord().value();
        } catch(Exception e) {
            System.out.println("ERROR on money transfer: \n" + e.getStackTrace());
        }
        return transfer;
    }
}
