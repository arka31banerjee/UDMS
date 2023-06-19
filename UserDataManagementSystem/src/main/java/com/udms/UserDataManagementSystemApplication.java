package com.udms;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.udms.utility.UserEventListener;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "UDMS - User Data Management Service",
				description = "UDMS - REST API Documentation",
				version = "v1.0",
				contact = @Contact(
					name = "Arka Banerjee",
					email = "arka31banerjee@gmail.com"
				)
			)
		)
@EnableTransactionManagement
public class UserDataManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserDataManagementSystemApplication.class, args);
	}

}
