package com.samoshin.moneybox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.moneybox.controller.MoneyboxController;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EmbeddedKafka(
        topics = {"moneybox_topic"},
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29098","port=9092"}
)
@TestPropertySource(properties = {"spring.kafka.bootstrap-servers=localhost:29098"})
@DirtiesContext
class KafkaProducerServiceTest {

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MoneyboxController moneyboxController;

    private MoneyTransferDto moneyTransferDto;

    private MoneyboxDto moneyboxDto;

    @BeforeEach
    void setup() {
        moneyTransferDto = new MoneyTransferDto(1L, 1L, true, 99L);
        moneyboxDto = new MoneyboxDto(1L, 99L, List.of(moneyTransferDto));
        objectMapper = new ObjectMapper();
    }

    @Test
    void sendTransferMessage() throws InterruptedException, JsonProcessingException {
        kafkaProducerService.sendTransfer(moneyTransferDto);
        Thread.sleep(5000);
        Consumer<String, MoneyTransferDto> consumer = createTransferConsumer();
        consumer.subscribe(Collections.singletonList("moneybox_topic"));
        ConsumerRecords<String, MoneyTransferDto> records = consumer.poll(Duration.ofSeconds(5));
        assertNotNull(records);
        MoneyTransferDto receivedDto = records.records("moneybox_topic").iterator().next().value();
        assertEquals(99L, receivedDto.getSum());
    }

    @Test
    void sendGetInfoMessage() throws InterruptedException, JsonProcessingException {
        kafkaProducerService.getInfo(1L);
        Thread.sleep(5000);
        Consumer<String, String> consumer = createInfoConsumer();
        consumer.subscribe(Collections.singletonList("info_topic"));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        assertNotNull(records);
        String receivedDto = records.records("info_topic").iterator().next().value();
        assertEquals("1", receivedDto);
    }


    private Consumer<String, MoneyTransferDto> createTransferConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29098");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "moneybox-test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MoneyTransferDto.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    private Consumer<String, String> createInfoConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29098");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "info-test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MoneyTransferDto.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }
}