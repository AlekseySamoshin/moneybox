package com.samoshin.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MoneyTransferDto {
    private final Long id;
    private final Long moneyboxId;
    private final boolean increase;
    private final Long sum;

    @Override
    public String toString() {
        return "MoneyTransfer[" +
                "id=" + id +
                "moneyboxId=" + moneyboxId +
                "sum=" + sum +
                "increase=" + increase +
                "]";
    }
}
