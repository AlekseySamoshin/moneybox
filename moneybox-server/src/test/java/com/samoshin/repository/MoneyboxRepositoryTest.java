package com.samoshin.repository;

import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        MoneyTransfer moneyTransfer = new MoneyTransfer(null, null, 1L, true, 99L);
        moneyboxRepository.save(moneybox);
        moneyTransferRepository.save(moneyTransfer);

        Optional<Moneybox> readedMoneybox = moneyboxRepository.findByIdForUpdate(1L);
        assertEquals(true,  readedMoneybox.isPresent());
        assertEquals(99L, readedMoneybox.get().getSum());

        Optional<Moneybox> readedNonExistentMoneybox = moneyboxRepository.findByIdForUpdate(2L);
        assertEquals(true, readedNonExistentMoneybox.isEmpty());
    }
}