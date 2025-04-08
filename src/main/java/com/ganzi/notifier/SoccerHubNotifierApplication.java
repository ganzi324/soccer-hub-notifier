package com.ganzi.notifier;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableRabbit
public class SoccerHubNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoccerHubNotifierApplication.class, args);
	}

}
