package com.samoshin.moneybox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoshin.moneybox.service.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29093", "port=9092"}
)
class MoneyboxControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducerService kafkaProducerService;

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