package com.github.helpdesk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.helpdesk.service.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

	private DBService dbService;
	
	public DevConfig(DBService dbService) {
		this.dbService = dbService;
	}
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String value;
	
	@Bean
	public boolean instanciaDB() {
		if (value.equals("create")) {
			this.dbService.instanciaDB();
		}
		return false;
	}
}
