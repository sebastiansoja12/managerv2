package com.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.warehouse.*")
@EntityScan(basePackages = {"com.warehouse.*"})
@EnableJpaRepositories(basePackages = {"com.warehouse.*"})
@ConfigurationPropertiesScan("com.warehouse.*")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
