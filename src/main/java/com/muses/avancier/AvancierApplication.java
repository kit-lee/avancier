package com.muses.avancier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class AvancierApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvancierApplication.class, args);
	}
}
