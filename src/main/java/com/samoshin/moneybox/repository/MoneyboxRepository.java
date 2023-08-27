package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.Moneybox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
}
