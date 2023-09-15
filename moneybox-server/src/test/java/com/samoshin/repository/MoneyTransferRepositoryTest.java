package com.samoshin.repository;

import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MoneyTransferRepositoryTest {

    @Autowired
    MoneyboxRepository moneyboxRepository;

    @Autowired
    MoneyTransferRepository moneyTransferRepository;

    @Test
    void saveAndFindById() {
        Moneybox moneybox = new Moneybox();
        moneybox.setSum(999L);
        MoneyTransfer moneyTransfer = new MoneyTransfer(null, 1L, true, 999L);
        moneyboxRepository.save(moneybox);
        moneyTransferRepository.save(moneyTransfer);

        Optional<MoneyTransfer> readedMoneyTransfer = moneyTransferRepository.findByIdForUpdate(1L);
        assertEquals(true,  readedMoneyTransfer.isPresent());
        assertEquals(999L, readedMoneyTransfer.get().getSum());

        Optional<MoneyTransfer> readedNonExistentMoneyTransfer = moneyTransferRepository.findByIdForUpdate(2L);
        assertEquals(true, readedNonExistentMoneyTransfer.isEmpty());
    }
}