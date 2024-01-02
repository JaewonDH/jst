package com.jst;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class JstApplication {

	public static void main(String[] args) {
		SpringApplication.run(JstApplication.class, args);
	}

}
