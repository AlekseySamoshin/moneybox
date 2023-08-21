package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {
}
