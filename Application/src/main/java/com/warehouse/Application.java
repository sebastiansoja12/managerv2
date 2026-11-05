package com.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.warehouse.*")
@EntityScan(basePackages = {"com.warehouse.*"})
@EnableJpaRepositories(basePackages = {"com.warehouse.*"})
@ConfigurationPropertiesScan("com.warehouse.*")
public class Application {

	@Bean
	public EntityManager sharedEntityManager(final EntityManagerFactory entityManagerFactory) {
		return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
