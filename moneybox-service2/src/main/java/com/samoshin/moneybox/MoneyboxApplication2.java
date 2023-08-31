package com.samoshin.moneybox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class MoneyboxApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(MoneyboxApplication2.class, args);
	}

}
