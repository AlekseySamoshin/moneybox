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
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"},
        topics = {"moneybox_topic", "info_topic"}
)
class MoneyTransferServiceTest {

    @InjectMocks
    private MoneyTransferService moneyTransferService;

    @MockBean
    private EntityManager entityManager;

    @Mock
    private MoneyboxRepository moneyboxRepository;

    @Mock
    private MoneyTransferRepository moneyTransferRepository;

    @Mock
    private MoneyTransferDtoMapper moneyTransferDtoMapper;

    @Mock
    private MoneyboxDtoMapper moneyboxDtoMapper;

    private Moneybox moneybox = new Moneybox();
    private MoneyTransfer moneyTransfer = new MoneyTransfer();
    private MoneyboxDto moneyboxDto;
    private MoneyTransferDto moneyTransferDto;

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
        when(moneyboxRepository.findById(1L)).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findById(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToDto(any())).then(Mockito.CALLS_REAL_METHODS);
        setUp();
        MoneyTransferDto dtoAfterTransaction = moneyTransferService.makeTransaction(moneyTransferDto);
        assertNotNull(dtoAfterTransaction);
        assertEquals(moneyTransferDto.getSum(), dtoAfterTransaction.getSum());
    }

    @Test
    void makeTransactionWithRollbackAfterException() {
        when(moneyboxRepository.findById(any())).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findById(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToDto(any())).then(Mockito.CALLS_REAL_METHODS);
        when(moneyTransferRepository.save(any())).thenThrow(new NullPointerException("error example"));
        setUp();
        Exception exception = assertThrows(
                NullPointerException.class,
                () -> moneyTransferService.makeTransaction(moneyTransferDto));
        assertEquals("error example", exception.getMessage());
        Mockito.verify(entityManager, Mockito.times(1)).getTransaction();
//        Mockito.verify(entityManager.getTransaction(), Mockito.times(1)).rollback();
    }

    @Test
    void getInfo() {
        when(moneyboxRepository.findById(anyLong())).thenReturn(Optional.of(moneybox));
        MoneyboxDto receivedMoneyboxDto = moneyTransferService.getInfo("1");
        assertEquals(1L, receivedMoneyboxDto.getId());
    }

    @Test
    void getInfoWrongIdType() {
        Exception exception = assertThrows(
                NumberFormatException.class,
                () -> moneyTransferService.getInfo("not_a_long_id"));
        assertEquals(true, exception.getMessage().contains("not_a_long_id"));
    }
}