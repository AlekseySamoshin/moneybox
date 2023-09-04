package com.samoshin.service;


import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.exception.NotFoundException;
import com.samoshin.mapper.MoneyTransferDtoMapper;
import com.samoshin.mapper.MoneyboxDtoMapper;
import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import com.samoshin.repository.MoneyTransferRepository;
import com.samoshin.repository.MoneyboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final MoneyboxRepository moneyboxRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final MoneyTransferDtoMapper transferDtoMapper;
    private final MoneyboxDtoMapper moneyboxDtoMapper;

    @Transactional
    public MoneyTransferDto makeTransaction(MoneyTransferDto transferDto) {
        Moneybox moneybox = getMoneyboxById(transferDto.getMoneyboxId());
        MoneyTransfer transfer = moneyTransferRepository.save(transferDtoMapper.mapToTransfer(transferDto));
        if(transfer.isIncrease()) {
            moneybox.setSum(moneybox.getSum() + transfer.getSum());
        } else {
            moneybox.setSum(moneybox.getSum() - transfer.getSum());
        }
        return transferDtoMapper.mapToDto(transfer);
    }

    public MoneyboxDto getInfo(String moneyboxId) {
        Long moneyboxIdLong = Long.parseLong(moneyboxId);
        return moneyboxDtoMapper.mapToDto(getMoneyboxById(moneyboxIdLong));
    }

    private Moneybox getMoneyboxById(Long moneyboxId) {
        return moneyboxRepository.findById(moneyboxId).orElseThrow(
                () -> new NotFoundException("moneybox id=" + moneyboxId + " not found")
        );
    }
}
