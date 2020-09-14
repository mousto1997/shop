package net.sid.eboutique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class SekApplication {

	public static void main(String[] args) {
		SpringApplication.run(SekApplication.class, args);
		
		System.out.println("Good morning everyone !");
	}
	
}
