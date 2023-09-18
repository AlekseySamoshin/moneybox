//package com.samoshin.moneybox.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.samoshin.dto.MoneyTransferDto;
//import com.samoshin.dto.MoneyboxDto;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.TestPropertySource;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.Network;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
////@Testcontainers
//@SpringBootTest
//@EmbeddedKafka(
//        partitions = 1,
//        brokerProperties = {"listeners=PLAINTEXT://localhost:29094", "port=9092"},
//        topics = {"moneybox_topic", "info_topic"}
//)
//@TestPropertySource(properties = {"spring.kafka.bootstrap-servers=localhost:29094"})
//class KafkaProducerServiceTransferTest {
//
//    @Autowired
//    KafkaProducerService kafkaProducerService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
////    static Network network = Network.newNetwork();
//
////    @Container
////    static final KafkaContainer kafkaContainer1 = new KafkaContainer()
////            .withNetwork(network);
////
////    @Container
////    static final KafkaContainer kafkaContainer2 = new KafkaContainer()
////            .withNetwork(network);
////
////    @Container
////    static final KafkaContainer kafkaContainer3 = new KafkaContainer()
////            .withNetwork(network);
//
////    @DynamicPropertySource
////    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
////        registry.add("spring.kafka.bootstrap-servers", () ->
////                kafkaContainer1.getBootstrapServers() + ","
////                + kafkaContainer2.getBootstrapServers() + ","
////                + kafkaContainer3.getBootstrapServers());
////    }
//
//    MoneyTransferDto moneyTransferDto;
//    MoneyboxDto moneyboxDto;
//
//    private HashMap<String, ConsumerRecord<String, String>> records = new HashMap();
//
//    @KafkaListener(
//            topics = "moneybox_topic",
//            groupId = "test_group",
//            concurrency = "1")
//    public void listen(ConsumerRecord<String, String> record) {
//        records.put("transfer", record);
//    }
//
//    @BeforeEach
//    void setup() throws InterruptedException {
//        moneyTransferDto = new MoneyTransferDto(1L, 1L, true, 99L);
//        moneyboxDto = new MoneyboxDto(1L, 99L, List.of(moneyTransferDto));
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void sendTransferMessage() throws InterruptedException, JsonProcessingException {
//        kafkaProducerService.sendTransfer(moneyTransferDto);
//        Thread.sleep(4000);
//        Optional<ConsumerRecord<String, String>> received = Optional.of(records.get("transfer"));
//        assertEquals(true, received.isPresent());
//        MoneyTransferDto receivedDto = objectMapper.readValue(received.get().value(), MoneyTransferDto.class);
//        assertEquals(99L, receivedDto.getSum());
//    }
//}