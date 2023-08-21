package com.samoshin.moneybox.mapper;

import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.dto.MoneyboxDto;
import com.samoshin.moneybox.model.Moneybox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MoneyboxDtoMapper {
    private final MoneyTransferDtoMapper transferDtoMapper;
    public MoneyboxDto mapToDto(Moneybox moneybox) {
        List<MoneyTransferDto> transferDtos = moneybox.getTransfers().stream()
                .map((transfer) -> transferDtoMapper.mapToDto(transfer))
                .collect(Collectors.toList());
        return new MoneyboxDto(moneybox.getId(), moneybox.getSum(), transferDtos);

    }
}
