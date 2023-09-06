package com.samoshin.moneybox.testKafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestKafkaConsumer {
//    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "test_topic")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        payload = consumerRecord.toString();
    }

//    public void resetLatch() {
//        latch = new CountDownLatch(1);
//    }
}
