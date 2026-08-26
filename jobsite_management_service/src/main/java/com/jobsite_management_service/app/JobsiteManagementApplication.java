package com.jobsite_management_service.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class JobsiteManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsiteManagementApplication.class, args);
	}

}
