package com.samoshin.moneybox.mapper;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.moneybox.model.MoneyTransfer;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransferDtoMapper {
    public MoneyTransferDto mapToDto(MoneyTransfer transfer) {
        return new MoneyTransferDto(
                transfer.getId(),
                transfer.getMoneyboxId(),
                transfer.isIncrease(),
                transfer.getSum());
    }

    public MoneyTransfer mapToTransfer(MoneyTransferDto transferDto) {
        return MoneyTransfer.builder()
                .moneyboxId(transferDto.moneyboxId())
                .increase(transferDto.increase())
                .sum(transferDto.sum())
                .build();
    }
}
