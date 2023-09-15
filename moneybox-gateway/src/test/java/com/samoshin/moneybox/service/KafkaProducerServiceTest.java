package com.samoshin.moneybox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@EnableKafka
@EmbeddedKafka(
        topics = {"moneybox_topic", "info_topic"},
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29094", "port=9092"}
)
class KafkaProducerServiceTest {

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    private MoneyTransferDto moneyTransferDto;
    private MoneyboxDto moneyboxDto;
    private static BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingDeque<>();

    @KafkaListener(
            topics = "moneybox_topic",
            groupId = "test_group",
            concurrency = "1")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record);
    }

    @KafkaListener(
            topics = "info_topic",
            groupId = "test_group",
            concurrency = "1")
    public void listenInfo(ConsumerRecord<String, String> record) {
        records.add(record);
    }

    @BeforeEach
    void setup() {
        moneyTransferDto = new MoneyTransferDto(1L, 1L, true, 99L);
        moneyboxDto = new MoneyboxDto(1L, 99L, List.of(moneyTransferDto));
        objectMapper = new ObjectMapper();
    }

    @Test
    void sendTransferMessage() throws InterruptedException, JsonProcessingException {
        kafkaProducerService.sendTransfer(moneyTransferDto);
        ConsumerRecord<String, String> received = records.poll(1, TimeUnit.SECONDS);
        assertNotNull(received);
        MoneyTransferDto receivedDto = objectMapper.readValue(received.value(), MoneyTransferDto.class);
        assertEquals(99L, receivedDto.getSum());
    }

    @Test
    void sendGetInfoMessage() throws InterruptedException, JsonProcessingException {
        kafkaProducerService.getInfo(1L);
        ConsumerRecord<String, String> received = records.poll(1, TimeUnit.SECONDS);
        assertNotNull(received);
        assertEquals("1", received.value());
    }
}