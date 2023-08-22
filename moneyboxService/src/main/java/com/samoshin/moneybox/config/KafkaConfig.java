package com.samoshin.moneybox.config;

import com.samoshin.moneybox.constant.Constant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(Constant.TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
