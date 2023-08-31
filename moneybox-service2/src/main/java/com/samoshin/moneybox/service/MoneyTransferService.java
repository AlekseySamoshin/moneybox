package com.samoshin.moneybox.service;


import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
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
    private final KafkaProducerService kafkaProducerService;

    public MoneyTransferDto sendTransfer(Long moneyboxId, Long sum, Boolean increase) {
        MoneyTransferDto transfer = new MoneyTransferDto(null, moneyboxId, increase, sum);
        return kafkaProducerService.sendTransfer(transfer);
    }

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
