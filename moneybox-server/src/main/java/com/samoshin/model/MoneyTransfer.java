package com.samoshin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "money_transfers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moneybox_id")
    private Long moneyboxId;

    private boolean increase;

    private Long sum;
}
