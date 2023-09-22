package com.samoshin.kafka_streams;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class InfoProcessor {

    @Autowired
    private final MoneyTransferService moneyTransferService;

    @Bean
    public Function<KStream<String, String>, KStream<String, MoneyboxDto>> getInfoProcessor () {
        return input -> input.mapValues((key, transfer) -> moneyTransferService.getInfo(transfer));
    }
}
