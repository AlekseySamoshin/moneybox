package com.samoshin.dto;

import java.util.List;

public record MoneyboxDto(Long id, Long sum, List<MoneyTransferDto> transfers) {
}
