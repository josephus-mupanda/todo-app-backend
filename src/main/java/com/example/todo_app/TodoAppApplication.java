package com.example.todo_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class TodoAppApplication {
//	@Autowired
//	private Environment environment;
	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}
//	@EventListener(ApplicationReadyEvent.class)
//	public void checkConfiguration() {
//		System.out.println("=== Configuration Check ===");
//		System.out.println("Database URL: " + environment.getProperty("spring.datasource.url"));
//		System.out.println("Database User: " + environment.getProperty("spring.datasource.username"));
//		System.out.println("Server Port: " + environment.getProperty("server.port"));
//		System.out.println("JWT Secret: " + environment.getProperty("security.jwt.secret-key"));
//		System.out.println("=== End Configuration Check ===");
//	}
}
