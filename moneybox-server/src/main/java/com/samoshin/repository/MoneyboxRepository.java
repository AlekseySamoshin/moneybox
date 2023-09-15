package com.samoshin.repository;


import com.samoshin.model.Moneybox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    @Query(value = "SELECT mb.id,mb.sum FROM moneyboxes mb WHERE id=? FOR UPDATE", nativeQuery = true)
    Optional<Moneybox> findByIdForUpdate(Long id);
}
