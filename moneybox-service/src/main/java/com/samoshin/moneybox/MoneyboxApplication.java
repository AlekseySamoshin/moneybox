package com.samoshin.moneybox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
//@ComponentScan(basePackages = {"com.samoshin"})
public class MoneyboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyboxApplication.class, args);
	}

}
