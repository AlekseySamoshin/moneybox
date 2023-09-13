package com.samoshin.repository;

import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MoneyboxRepositoryTest {
    @Autowired
    MoneyboxRepository moneyboxRepository;

    @Autowired
    MoneyTransferRepository moneyTransferRepository;

    @Test
    void saveAndFindById() {
        Moneybox moneybox = new Moneybox();
        moneybox.setSum(99L);
        MoneyTransfer moneyTransfer = new MoneyTransfer(null, 1L, true, 99L);
        moneyboxRepository.save(moneybox);
        moneyTransferRepository.save(moneyTransfer);

        Optional<Moneybox> readedMoneybox = moneyboxRepository.findById(1L);
        assertEquals(true,  readedMoneybox.isPresent());
        assertEquals(99L, readedMoneybox.get().getSum());

        Optional<Moneybox> readedNonExistentMoneybox = moneyboxRepository.findById(2L);
        assertEquals(true, readedNonExistentMoneybox.isEmpty());
    }
}