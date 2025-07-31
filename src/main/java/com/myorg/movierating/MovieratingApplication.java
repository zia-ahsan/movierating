package com.myorg.movierating;

import com.myorg.movierating.backend.config.AzureB2CProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(AzureB2CProperties.class)
@EnableCaching
public class MovieratingApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("ENTRA_EXTERNAL_TENANT_ID", dotenv.get("ENTRA_EXTERNAL_TENANT_ID"));
		System.setProperty("ENTRA_EXTERNAL_CLIENT_ID", dotenv.get("ENTRA_EXTERNAL_CLIENT_ID"));
		System.setProperty("ENTRA_EXTERNAL_CLIENT_SECRET", dotenv.get("ENTRA_EXTERNAL_CLIENT_SECRET"));
		SpringApplication.run(MovieratingApplication.class, args);
	}

}
