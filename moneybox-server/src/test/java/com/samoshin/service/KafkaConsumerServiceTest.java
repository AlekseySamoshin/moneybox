package com.samoshin.service;

import com.samoshin.dto.MoneyTransferDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"}
)
class KafkaConsumerServiceTest {

    @Autowired
    KafkaTemplate<String, MoneyTransferDto> kafkaTemplate;

    private static BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingDeque<>();

    @KafkaListener(
            topics = "moneybox_topic",
            groupId = "test_group",
            concurrency = "1")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record);
    }


    @BeforeEach
    void sendMessageToKafka() {
        MoneyTransferDto moneyTransferDto = new MoneyTransferDto(1L, 1L, true, 99L);
        kafkaTemplate.send("moneybox_topic", moneyTransferDto);
    }

    @Test
    void executeTransfer() {
//        MoneyTransferDto result = executeTransfer(MoneyTransferDto transferDto, Acknowledgment acknowledgment)
    }

    @Test
    void getMoneyboxInfo() {
    }
}