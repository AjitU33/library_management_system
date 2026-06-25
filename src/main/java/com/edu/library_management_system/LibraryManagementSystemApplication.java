package com.edu.library_management_system;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryManagementSystemApplication {
	
	//http://localhost:8080/swagger-ui/index.html

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
		
		
	}
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
}
