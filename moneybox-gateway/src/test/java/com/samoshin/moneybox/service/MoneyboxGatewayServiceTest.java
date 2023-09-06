package com.samoshin.moneybox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyboxGatewayServiceTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void sendTransfer() {

    }

    @Test
    void getInfo() {
    }
}