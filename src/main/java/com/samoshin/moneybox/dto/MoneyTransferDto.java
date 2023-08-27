package com.samoshin.moneybox.dto;

public record MoneyTransferDto(Long id, Long moneyboxId, Boolean increase, Long sum) {
}
