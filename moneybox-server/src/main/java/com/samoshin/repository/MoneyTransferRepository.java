package com.samoshin.repository;

import com.samoshin.model.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {
    @Override
    @Query(value = "SELECT * FROM money_transfers WHERE id = ? FOR NO KEY UPDATE", nativeQuery = true)
    Optional<MoneyTransfer> findById(Long id);
}
