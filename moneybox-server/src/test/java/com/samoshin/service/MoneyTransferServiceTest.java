package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.mapper.MoneyTransferDtoMapper;
import com.samoshin.mapper.MoneyboxDtoMapper;
import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import com.samoshin.repository.MoneyTransferRepository;
import com.samoshin.repository.MoneyboxRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"},
        topics = {"moneybox_topic", "info_topic"}
)
class MoneyTransferServiceTest {

    @Autowired
    MoneyTransferService moneyTransferService;

    @MockBean
    EntityManager entityManager;

    @MockBean
    MoneyboxRepository moneyboxRepository;

    @MockBean
    MoneyTransferRepository moneyTransferRepository;

    @Autowired
    MoneyboxDtoMapper moneyboxDtoMapper;

    @MockBean
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
        moneybox.setTransfers(List.of(moneyTransfer));
        moneyboxDto = moneyboxDtoMapper.mapToDto(moneybox);
        moneyTransferDto = moneyTransferDtoMapper.mapToDto(moneyTransfer);
    }

    @Test
    void makeTransaction() {
        when(moneyboxRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findByIdForUpdate(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToDto(any())).then(Mockito.CALLS_REAL_METHODS);
        setUp();
        MoneyTransferDto dtoAfterTransaction = moneyTransferService.makeTransaction(moneyTransferDto);
        assertNotNull(dtoAfterTransaction);
        assertEquals(moneyTransferDto.getSum(), dtoAfterTransaction.getSum());
    }

    @Test
    void getInfo() {
        when(moneyboxRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findByIdForUpdate(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToTransfer(any())).then(Mockito.CALLS_REAL_METHODS);
        MoneyboxDto receivedMoneyboxDto = moneyTransferService.getInfo("1");
        assertEquals(1L, receivedMoneyboxDto.getId());
    }

    @Test
    void getInfoWrongIdType() {
        when(moneyboxRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findByIdForUpdate(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToDto(any())).then(Mockito.CALLS_REAL_METHODS);
        Exception exception = assertThrows(
                NumberFormatException.class,
                () -> moneyTransferService.getInfo("not_a_long_id"));
        assertEquals(true, exception.getMessage().contains("not_a_long_id"));
    }
}