//package com.samoshin.kafka_streams;
//
//import com.samoshin.dto.MoneyTransferDto;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.function.Function;
//
//@Configuration
//public class TransactionAggregator {
//    @Bean
//    public Function<KStream<String, MoneyTransferDto>, KStream<String, MoneyTransferDto>> transactionAggregator() {
//        return input -> input;
//    }
//}
