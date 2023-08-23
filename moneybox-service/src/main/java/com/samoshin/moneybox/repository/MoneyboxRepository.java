package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.Moneybox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    @Override
    @Query(value = "SELECT mb.id,mb.sum FROM moneyboxes mb WHERE id=? FOR NO KEY UPDATE", nativeQuery = true)
    Optional<Moneybox> findById(Long id);
}
