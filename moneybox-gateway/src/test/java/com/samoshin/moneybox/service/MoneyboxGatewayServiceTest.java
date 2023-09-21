package com.samoshin.moneybox.service;

import com.samoshin.dto.MoneyTransferDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:29097", "port=9092"},
        controlledShutdown = true
)
class MoneyboxGatewayServiceTest {

    @Autowired
    MoneyboxGatewayService moneyboxGatewayService;

    @Test
    void sendTransfer() {
        MoneyTransferDto moneyTransferDto = moneyboxGatewayService.sendTransfer(1L, 999L, true);
        assertEquals(999L, moneyTransferDto.getSum());
    }

    @Test
    void getInfo() {
        String result = moneyboxGatewayService.getInfo(1L);
        assertEquals("Request to get info was sended for moneyboxId=1", result);
    }
}