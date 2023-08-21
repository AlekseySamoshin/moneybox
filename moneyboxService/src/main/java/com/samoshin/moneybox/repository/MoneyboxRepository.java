package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.Moneybox;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<Moneybox> findById(Long id);
}
