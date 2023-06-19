package com.udms.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
@Configuration
public class UDMSConfig {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	@Bean
	public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDbFactory) {
		return new MongoTransactionManager(mongoDbFactory);
	}
}
