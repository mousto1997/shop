package net.sid.eboutique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import net.sid.eboutique.dao.UserDAO;
//import net.sid.eboutique.entities.User;



@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class SekApplication implements CommandLineRunner{

	public static void main(String[] args) {
		
		SpringApplication.run(SekApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		// TODO Auto-generated method stub
//		User user = new User("soumah", "Saliou Soumah", "barry@gmail.com", "123456", bc.encode("12345"), true);
		
//		userRep.save(user);
		
		System.out.println("Good morning everyone !");
		
	}
	
}
