package com.unitrack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class UniTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniTrackApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logFinalMessage() {
		log.info("The server is running at: localhost:2017");
		log.info("Default credentials: Username: admin@email.com; Password: password");
	}
}
