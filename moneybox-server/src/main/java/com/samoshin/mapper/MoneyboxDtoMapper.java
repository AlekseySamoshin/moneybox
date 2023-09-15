package com.samoshin.mapper;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.model.Moneybox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MoneyboxDtoMapper {
    private final MoneyTransferDtoMapper transferDtoMapper;
    List<MoneyTransferDto> transferDtos;
    public MoneyboxDto mapToDto(Moneybox moneybox) {
        if(null != moneybox.getTransfers()) {
            transferDtos = moneybox.getTransfers().stream()
                    .map((transfer) -> transferDtoMapper.mapToDto(transfer))
                    .collect(Collectors.toList());
        }
        return new MoneyboxDto(moneybox.getId(), moneybox.getSum(), transferDtos);

    }
}
