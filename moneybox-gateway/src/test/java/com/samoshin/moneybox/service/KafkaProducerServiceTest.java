package com.samoshin.moneybox.service;

import com.samoshin.moneybox.testKafka.TestKafkaConsumer;
import com.samoshin.moneybox.testKafka.TestKafkaProducer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaProducerServiceTest {
    @Autowired
    private TestKafkaConsumer consumer;

    @Autowired
    private TestKafkaProducer producer;

    private String topic = "test_topic";

    @Test
    void sendTransfer() {
    }

    @Test
    void getInfo() {
    }
}