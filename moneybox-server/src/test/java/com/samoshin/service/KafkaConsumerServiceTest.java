package com.samoshin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.mapper.MoneyTransferDtoMapper;
import com.samoshin.mapper.MoneyboxDtoMapper;
import com.samoshin.model.MoneyTransfer;
import com.samoshin.model.Moneybox;
import com.samoshin.repository.MoneyTransferRepository;
import com.samoshin.repository.MoneyboxRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29094", "port=9092"},
        topics = {"moneybox_topic","info_topic"}
)
class KafkaConsumerServiceTest {

    @Autowired
    MoneyTransferService moneyTransferService;

    @Autowired
    KafkaConsumerService kafkaConsumerService;

    @MockBean
    MoneyboxRepository moneyboxRepository;

    @MockBean
    MoneyTransferRepository moneyTransferRepository;

    @Autowired
    MoneyboxDtoMapper moneyboxDtoMapper;

    @MockBean
    MoneyTransferDtoMapper moneyTransferDtoMapper;

    @MockBean
    private KafkaListenerEndpointRegistry endpointRegistry;
    @MockBean
    Acknowledgment acknowledgment;

    @MockBean
    KafkaTemplate<String, String> infoKafkaTemplate;

    MoneyTransferDto moneyTransferDto;
    MoneyboxDto moneyboxDto;
    ObjectMapper objectMapper;
    Moneybox moneybox = new Moneybox();
    MoneyTransfer moneyTransfer = new MoneyTransfer();
    private static BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingDeque<>();

    @KafkaListener(
            topics = "moneybox_topic",
            groupId = "test_group",
            concurrency = "1")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record);
    }

    @BeforeEach
    void setup() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
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
    void executeTransfer() throws InterruptedException, JsonProcessingException {
        when(moneyboxRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(moneybox));
        when(moneyboxRepository.save(any())).thenReturn(moneybox);
        when(moneyTransferRepository.findByIdForUpdate(any())).thenReturn(Optional.of(moneyTransfer));
        when(moneyTransferRepository.save(any())).thenReturn(moneyTransfer);
        when(moneyTransferDtoMapper.mapToDto(any())).then(Mockito.CALLS_REAL_METHODS);
        setup();
        MoneyTransferDto testingDto = kafkaConsumerService.executeTransfer(moneyTransferDto, acknowledgment);
        assertNotNull(testingDto);
    }
}