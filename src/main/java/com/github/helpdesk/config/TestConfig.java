package com.github.helpdesk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.helpdesk.service.DBService;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("test")
public class TestConfig {

	private DBService dbService;
	
	public TestConfig(DBService dbService) {
		this.dbService = dbService;
	}
	
	@PostConstruct
	public void instanciaDB() {
		this.dbService.instanciaDB();
	}
	
}
