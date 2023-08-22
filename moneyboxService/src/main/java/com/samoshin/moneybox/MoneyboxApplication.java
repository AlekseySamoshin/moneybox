package com.samoshin.moneybox;

import com.samoshin.moneybox.constant.Constant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@SpringBootApplication
public class MoneyboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyboxApplication.class, args);
	}

}
