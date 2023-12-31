package com.samoshin.mapper;


import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.model.MoneyTransfer;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransferDtoMapper {
    public MoneyTransferDto mapToDto(MoneyTransfer transfer) {
        return new MoneyTransferDto(
                transfer.getId(),
                transfer.getMoneyboxId(),
                transfer.getTransactionNumber(),
                transfer.isIncrease(),
                transfer.getSum());
    }

    public MoneyTransfer mapToTransfer(MoneyTransferDto transferDto) {
        return MoneyTransfer.builder()
                .moneyboxId(transferDto.getMoneyboxId())
                .transactionNumber(transferDto.getTransactionNumber())
                .increase(transferDto.isIncrease())
                .sum(transferDto.getSum())
                .build();
    }
}
