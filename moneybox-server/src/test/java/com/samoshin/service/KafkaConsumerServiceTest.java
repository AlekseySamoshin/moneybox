package com.samoshin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"},
        topics = {"moneybox_topic","info_topic"}
)
class KafkaConsumerServiceTest {

    @InjectMocks
    KafkaConsumerService kafkaConsumerService;

    @MockBean
    private KafkaListenerEndpointRegistry endpointRegistry;

    @MockBean
    Acknowledgment acknowledgment;

    @MockBean
    KafkaTemplate<String, String> infoKafkaTemplate;

    private MoneyTransferDto moneyTransferDto;
    private MoneyboxDto moneyboxDto;
    private ObjectMapper objectMapper;
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
    void setup() throws JsonProcessingException {
        moneyTransferDto = new MoneyTransferDto(1L, 1L, true, 99L);
        moneyboxDto = new MoneyboxDto(1L, 99L, List.of(moneyTransferDto));
        objectMapper = new ObjectMapper();
        infoKafkaTemplate.send("moneybox_topic", objectMapper.writeValueAsString(moneyTransferDto));
    }

    @Test
    void executeTransfer() throws InterruptedException, JsonProcessingException {
        ConsumerRecord<String, String> received = records.poll(1, TimeUnit.SECONDS);
        assertNotNull(received);
        MoneyTransferDto receivedDto = objectMapper.readValue(received.value(), MoneyTransferDto.class);
        MoneyTransferDto testingDto = kafkaConsumerService.executeTransfer(receivedDto, acknowledgment);
        assertNotNull(testingDto);
//        kafkaConsumerService.executeTransfer()
    }
}