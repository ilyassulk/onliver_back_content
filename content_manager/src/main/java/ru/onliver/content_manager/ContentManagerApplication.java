package ru.onliver.content_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ContentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentManagerApplication.class, args);
	}

}
