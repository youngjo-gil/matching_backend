package com.matching;

import com.matching.post.repository.PostSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = PostSearchRepository.class
))
@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
public class MatchingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchingApplication.class, args);
	}

}
