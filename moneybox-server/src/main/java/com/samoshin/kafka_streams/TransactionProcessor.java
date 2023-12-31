//package com.samoshin.kafka_streams;
//
//import com.samoshin.dto.MoneyTransferDto;
//import com.samoshin.dto.MoneyboxDto;
//import com.samoshin.service.MoneyTransferService;
//import lombok.RequiredArgsConstructor;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.KTable;
//import org.apache.kafka.streams.kstream.Materialized;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//@Configuration
//@RequiredArgsConstructor
//public class TransactionProcessor {
//
//    @Autowired
//    private final MoneyTransferService moneyTransferService;
//
//    @Bean
//    public Function<KStream<String, MoneyTransferDto>, KStream<String, MoneyTransferDto>> makeTransactionProcessor() {
//        return input -> input.peek((key, transfer) -> moneyTransferService.makeTransaction(transfer));
//    }
//
////    @Bean
////    public Function<KStream<String, String>, KStream<String, MoneyboxDto>> getInfoProcessor () {
////        return input -> input.mapValues((key, transfer) -> moneyTransferService.getInfo(transfer));
////    }
//}


package com.samoshin.kafka_streams;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class TransactionProcessor {
    private final String countStore = "count-store";

    @Autowired
    private final MoneyTransferService moneyTransferService;

    @Bean
    public Consumer<KStream<String, MoneyTransferDto>> makeTransactionProcessor() {
        return input -> {
            KTable<String, Long> countTable = input.groupByKey()
                    .count(Materialized.as(countStore));
            input.leftJoin(countTable, (value, count) -> {
                        return new MoneyTransferDto(
                                value.getId(),
                                value.getMoneyboxId(),
                                count,
                                value.isIncrease(),
                                value.getSum());
                    }
            ).peek((key, transfer) -> moneyTransferService.makeTransaction(transfer));
        };
    }

    @Bean
    public Consumer<KStream<String, String>> getInfoProcessor() {
        return input -> input.peek((key, value) -> moneyTransferService.getInfo(value));
    }

    ;
}