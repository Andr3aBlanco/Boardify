package com.boardify.boardify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class BoardifyApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BoardifyApplication.class, args);
	}

}
