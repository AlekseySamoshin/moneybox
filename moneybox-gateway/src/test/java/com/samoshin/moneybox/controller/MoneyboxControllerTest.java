package com.samoshin.moneybox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyboxControllerTest {
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
        wrongAddUrl = "/id_not_a_long/add/sum_not_a_long";
        correctAddUrl = "/1/add/100";
        wrongSubtractUrl = "/id_not_a_long/subtract/sum_not_a_long";
        correctSubtractUrl = "/id_not_a_long/subtract/sum_not_a_long";
        getInfoCorrectUrl = "/1";
    }


    @SneakyThrows
    @Test
    void addMoneyCorrect() {
        mockMvc.perform(post(correctAddUrl)
//                        .header("X-Sharer-User-Id", 1)
//                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is(100), Long.class));
    }

    @Test
    void addMoneyWrongRequest() {
    }

    @Test
    void subtractMoneyWrongRequest() {
    }

    @Test
    void getInfo() {
    }
}