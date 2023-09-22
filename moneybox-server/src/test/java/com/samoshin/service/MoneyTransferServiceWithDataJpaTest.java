package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.exception.NotFoundException;
import com.samoshin.mapper.MoneyTransferDtoMapper;
import com.samoshin.mapper.MoneyboxDtoMapper;
import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import com.samoshin.repository.MoneyTransferRepository;
import com.samoshin.repository.MoneyboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ComponentScan("../*")
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29096", "port=9093"}
)
@DataJpaTest
@DirtiesContext
public class MoneyTransferServiceWithDataJpaTest {
    @Autowired
    MoneyTransferService moneyTransferService;

    @Autowired
    MoneyboxRepository moneyboxRepository;

    @Autowired
    MoneyTransferRepository moneyTransferRepository;

    @Autowired
    MoneyboxDtoMapper moneyboxDtoMapper;

    @Autowired
    MoneyTransferDtoMapper moneyTransferDtoMapper;

    Moneybox moneybox = new Moneybox();
    MoneyTransfer moneyTransfer = new MoneyTransfer();
    MoneyboxDto moneyboxDto;
    MoneyTransferDto moneyTransferDto;

    @BeforeEach
    void setUp() {
        moneybox.setId(1L);
        moneybox.setSum(99L);
        moneyTransfer.setId(1L);
        moneyTransfer.setMoneyboxId(1L);
        moneyTransfer.setSum(99L);
        moneyTransfer.setIncrease(true);
        moneyboxDto = moneyboxDtoMapper.mapToDto(moneybox);
        moneyTransferDto = moneyTransferDtoMapper.mapToDto(moneyTransfer);
    }


    @Test
    void makeTransactionWithRollbackAfterException() throws InterruptedException {
        setUp();
        moneyboxRepository.save(moneybox);
        moneyTransferRepository.save(moneyTransfer);
        Moneybox moneybox1 = moneyboxRepository.findById(1L).get();
        assertEquals(99L, moneyboxRepository.findById(1L).get().getSum());
        assertEquals(99L, moneyTransferRepository.findAll().stream()
                .mapToLong(MoneyTransfer::getSum)
                .sum());

        moneyTransferService.makeTransaction(moneyTransferDtoMapper.mapToDto(
                new MoneyTransfer(null, null, 1L, true, 100L)));
        assertEquals(199L, moneyboxRepository.findById(1L).get().getSum());
        assertEquals(199L, moneyTransferRepository.findAll().stream()
                .mapToLong(MoneyTransfer::getSum)
                .sum());

        //ошибочная транзакция
        Exception exception = assertThrows(
                NotFoundException.class,
                () -> moneyTransferService.makeTransaction(moneyTransferDtoMapper.mapToDto(
                        new MoneyTransfer(1L, null, 2L, true, 100L))));

        //проверка того, что данные не поменялись
        assertEquals(199L, moneyboxRepository.findById(1L).get().getSum());
        assertEquals(199L, moneyTransferRepository.findAll().stream()
                .mapToLong(MoneyTransfer::getSum)
                .sum());

        //проверка того, что никакие ошибочные данные не добавились
        List<MoneyTransfer> allTransfers = moneyTransferRepository.findAll();
        assertEquals(0, allTransfers.stream().filter((moneyTransfer) -> moneyTransfer.getMoneyboxId().equals(2L)).count());
    }
}