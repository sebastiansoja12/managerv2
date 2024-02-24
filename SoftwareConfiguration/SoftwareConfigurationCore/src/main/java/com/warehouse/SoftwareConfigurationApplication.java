package com.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.warehouse.softwareconfiguration")
@EntityScan(basePackages = {"com.warehouse.softwareconfiguration"})
@EnableMongoRepositories(basePackages = {"com.warehouse.softwareconfiguration"})
@ConfigurationPropertiesScan("com.warehouse.softwareconfiguration")
public class SoftwareConfigurationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftwareConfigurationApplication.class, args);
    }

}
