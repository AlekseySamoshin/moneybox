package com.samoshin.kafka_streams;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class TransactionProcessor {

    @Autowired
    private final MoneyTransferService moneyTransferService;

    @Bean
    public Function<KStream<String, MoneyTransferDto>, KStream<String, MoneyTransferDto>> makeTransactionProcessor () {
        return input -> input.peek((key, transfer) -> moneyTransferService.makeTransaction(transfer));
    }
}
