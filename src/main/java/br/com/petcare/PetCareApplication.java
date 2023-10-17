package br.com.petcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PetCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetCareApplication.class, args);
	}

}
