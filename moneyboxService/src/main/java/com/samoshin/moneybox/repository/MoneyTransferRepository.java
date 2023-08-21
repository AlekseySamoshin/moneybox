package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.MoneyTransfer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<MoneyTransfer> findById(Long id);
}
