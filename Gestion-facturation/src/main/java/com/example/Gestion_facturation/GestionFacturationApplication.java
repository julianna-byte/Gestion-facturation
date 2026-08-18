package com.example.Gestion_facturation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@ComponentScan(basePackages = {"com.example.Gestion_facturation", "Controller", "Service", "Repository", "Mapper", "Exception", "DTO" , "Configuration"})
@EntityScan(basePackages = {"Entity"})
@EnableJpaRepositories(basePackages = {"Repository"})

public class GestionFacturationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionFacturationApplication.class, args);
	}

}
