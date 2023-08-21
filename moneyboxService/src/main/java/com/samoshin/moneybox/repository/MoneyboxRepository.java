package com.samoshin.moneybox.repository;

import com.samoshin.moneybox.model.MoneyTransfer;
import com.samoshin.moneybox.model.Moneybox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {

}
