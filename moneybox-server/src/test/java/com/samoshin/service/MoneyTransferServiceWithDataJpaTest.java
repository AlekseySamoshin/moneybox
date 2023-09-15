package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.mapper.MoneyTransferDtoMapper;
import com.samoshin.mapper.MoneyboxDtoMapper;
import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import com.samoshin.repository.MoneyTransferRepository;
import com.samoshin.repository.MoneyboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"},
        topics = {"moneybox_topic", "info_topic"}
)
@DataJpaTest
@DirtiesContext
public class MoneyTransferServiceWithDataJpaTest {
    @Autowired
    MoneyTransferService moneyTransferService;

    @Mock
    MoneyboxRepository moneyboxRepository;

    @Mock
    MoneyTransferRepository moneyTransferRepository;

    @Mock
    MoneyboxDtoMapper moneyboxDtoMapper;

    @Mock
    MoneyTransferDtoMapper moneyTransferDtoMapper;

    private Moneybox moneybox = new Moneybox();
    private MoneyTransfer moneyTransfer = new MoneyTransfer();
    private MoneyboxDto moneyboxDto = new MoneyboxDto();
    private MoneyTransferDto moneyTransferDto = new MoneyTransferDto();

    @BeforeEach
    void setUp() {
        moneybox.setSum(99L);
        moneyTransfer.setMoneyboxId(1L);
        moneyTransfer.setSum(99L);
        moneyTransfer.setIncrease(true);
        moneyboxDto = moneyboxDtoMapper.mapToDto(moneybox);
        moneyTransferDto = moneyTransferDtoMapper.mapToDto(moneyTransfer);
    }

    @Test
    void makeTransactionWithRollbackAfterException() throws InterruptedException {
        moneyboxRepository.save(moneybox);
        moneyTransferRepository.save(moneyTransfer);
        assertEquals(99L, moneyboxRepository.findById(1L).get().getSum());
        assertEquals(99L, moneyTransferRepository.findAll().stream()
                .mapToLong(MoneyTransfer::getSum)
                .sum());


        moneyTransferService.makeTransaction(moneyTransferDtoMapper.mapToDto(
                new MoneyTransfer(null, 1L, true, 100L)));
        assertEquals(199L, moneyboxRepository.findById(1L).get().getSum());
        assertEquals(199L, moneyTransferRepository.findAll().stream()
                .mapToLong(MoneyTransfer::getSum)
                .sum());
    }
}