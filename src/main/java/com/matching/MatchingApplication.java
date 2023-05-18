package com.matching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
@Slf4j
public class MatchingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchingApplication.class, args);
	}

}
