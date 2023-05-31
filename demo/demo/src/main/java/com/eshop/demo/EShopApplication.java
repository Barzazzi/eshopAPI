package com.eshop.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan(basePackages={"com.eshop.demo.services"})
@EnableJpaRepositories(basePackages={"com.eshop.demo.repositories"})
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages={"com.eshop.demo.entities"})
public class EShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopApplication.class, args);
	}



}
