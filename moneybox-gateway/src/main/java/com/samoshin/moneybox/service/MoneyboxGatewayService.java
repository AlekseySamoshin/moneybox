package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyboxGatewayService {
    private final KafkaProducerService kafkaProducerService;
    public MoneyTransferDto sendTransfer(Long moneyboxId, Long sum, Boolean increase) {
        MoneyTransferDto transfer = new MoneyTransferDto(null, null, moneyboxId, increase, sum);
        return kafkaProducerService.sendTransfer(transfer);
    }

//    @Transactional
    public String getInfo(Long moneyboxId) {
        return kafkaProducerService.getInfo(moneyboxId);
    }
}
