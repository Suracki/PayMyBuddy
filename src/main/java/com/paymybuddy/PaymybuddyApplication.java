package com.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymybuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

}
