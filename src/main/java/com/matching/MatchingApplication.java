package com.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MatchingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchingApplication.class, args);
	}

}
