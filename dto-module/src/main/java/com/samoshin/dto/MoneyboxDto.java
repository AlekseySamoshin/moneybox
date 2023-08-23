package com.samoshin.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MoneyboxDto {
    private final Long id;
    private final Long sum;
    private final List<MoneyTransferDto> transfers;
}
