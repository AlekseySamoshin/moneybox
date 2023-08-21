package com.samoshin.moneybox.service;

import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.dto.MoneyboxDto;
import com.samoshin.moneybox.exception.NotFoundException;
import com.samoshin.moneybox.mapper.MoneyTransferDtoMapper;
import com.samoshin.moneybox.mapper.MoneyboxDtoMapper;
import com.samoshin.moneybox.model.MoneyTransfer;
import com.samoshin.moneybox.model.Moneybox;
import com.samoshin.moneybox.repository.MoneyTransferRepository;
import com.samoshin.moneybox.repository.MoneyboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final MoneyboxRepository moneyboxRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final MoneyboxDtoMapper moneyboxDtoMapper;
    private final MoneyTransferDtoMapper transferDtoMapper;



    @Transactional
    public MoneyTransferDto makeTransaction(Long moneyboxId, Long transactionSum, Boolean increase) {
        Moneybox moneybox = getMoneyboxById(moneyboxId);
        MoneyTransfer transfer = moneyTransferRepository.save(
                MoneyTransfer.builder()
                        .moneyboxId(moneyboxId)
                        .sum(transactionSum)
                        .increase(increase)
                        .build()
        );
        if(increase) {
            moneybox.setSum(moneybox.getSum() + transactionSum);
        } else {
            moneybox.setSum(moneybox.getSum() - transactionSum);
        }
        return transferDtoMapper.mapToDto(transfer);
    }

    @Transactional
    public MoneyboxDto getInfo(Long moneyboxId) {
        Moneybox moneybox = getMoneyboxById(moneyboxId);
        return moneyboxDtoMapper.mapToDto(moneybox);
    }

    private Moneybox getMoneyboxById(Long moneyboxId) {
        return moneyboxRepository.findById(moneyboxId).orElseThrow(
                () -> new NotFoundException("moneybox id=" + moneyboxId + " not found")
        );
    }
}
