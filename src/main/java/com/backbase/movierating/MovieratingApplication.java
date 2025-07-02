package com.backbase.movierating;

import com.backbase.movierating.backend.config.AzureB2CProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(AzureB2CProperties.class)
@EnableCaching
public class MovieratingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieratingApplication.class, args);
	}

}
