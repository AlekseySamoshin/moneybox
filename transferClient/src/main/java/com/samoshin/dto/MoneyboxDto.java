package com.samoshin.moneybox.dto;

import java.util.List;

public record MoneyboxDto(Long id, Long sum, List<MoneyTransferDto> transfers) {
}
