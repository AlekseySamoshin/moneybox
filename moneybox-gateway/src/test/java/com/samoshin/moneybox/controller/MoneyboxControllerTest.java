package com.samoshin.moneybox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.moneybox.service.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@Testcontainers
@AutoConfigureMockMvc
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29093", "port=9092"}
)
class MoneyboxControllerTest {

//--------Раскомментировать для использования TestContainers------------
//    static Network network = Network.newNetwork();
//
//    @Container
//    static final KafkaContainer kafkaContainer1 = new KafkaContainer()
//            .withNetwork(network);
//
//    @DynamicPropertySource
//    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.kafka.bootstrap-servers", () ->
//                kafkaContainer1.getBootstrapServers());
//    }

    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private String wrongAddUrl;
    private String correctAddUrl;
    private String wrongSubtractUrl;
    private String correctSubtractUrl;
    private String getInfoCorrectUrl;

    @BeforeEach
    void setup() {
        wrongAddUrl = "http://localhost:8080/moneybox/id_not_a_long/add/sum_not_a_long";
        correctAddUrl = "http://localhost:8080/moneybox/1/add/1000";
        wrongSubtractUrl = "http://localhost:8080/moneybox/id_not_a_long/subtract/sum_not_a_long";
        correctSubtractUrl = "http://localhost:8080/moneybox/1/subtract/1000";
        getInfoCorrectUrl = "http://localhost:8080/moneybox/1";
    }

    @Test
    void sendTransferAddMoneyCorrectRequest() throws Exception {
        mockMvc.perform(post(correctAddUrl)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendTransferSubtractMoneyCorrectRequest() throws Exception {
        mockMvc.perform(post(correctSubtractUrl)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getInfoCorrectRequest() throws Exception {
        mockMvc.perform(get(getInfoCorrectUrl)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendTransferWrongRequest() throws Exception {
        mockMvc.perform(post("http://localhost:8080/moneybox/1/1000")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void sendTransferAddMoneyWrongPathVariableRequest() throws Exception {
        mockMvc.perform(post(wrongAddUrl)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendTransferSubtractMoneyWrongPathVariableRequest() throws Exception {
        mockMvc.perform(post(wrongSubtractUrl)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInfoWrongRequest() throws Exception {
        mockMvc.perform(get("http://localhost:8080/moneybox/not_a_long")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}