//package com.samoshin.moneybox;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//
//@Configuration
////@EmbeddedKafka(
////        topics = {"moneybox_topic", "info_topic", "test"},
////        partitions = 1,
////        brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"}
////)
//public class EmbeddedKafkaConfig {
//    @Bean
//    public KafkaEmbedded kafkaEmbedded() {
//        // Конфигурация брокера Kafka
//        KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(3, false, 30);
//        kafkaEmbedded.setKafkaPorts(9092, 9093, 9094);  // Порты, которые будут использоваться
//        kafkaEmbedded.setKafkaBrokerProperties(createBrokerProperties());
//        kafkaEmbedded.setTopics("moneybox_topic", "info_topic");  // Создаваемые топики
//        return kafkaEmbedded;
//    }
//
//    private Properties createBrokerProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("listeners", "PLAINTEXT://localhost:29092,PLAINTEXT://localhost:29093,PLAINTEXT://localhost:29094");
//        return properties;
//    }
//}
//
//}
