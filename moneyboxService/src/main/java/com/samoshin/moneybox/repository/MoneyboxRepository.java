package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.Moneybox;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    @Override
    @Query(value = "SELECT mb.id,mb.sum FROM moneyboxes WHERE id=? FOR NO KEY UPDATE", nativeQuery = true)
    Optional<Moneybox> findById(Long id);
}
