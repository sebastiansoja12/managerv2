package com.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.warehouse.routetracker",
        exclude = { SecurityAutoConfiguration.class})
@EntityScan(basePackages = {"com.warehouse.routetracker"})
@EnableJpaRepositories(basePackages = {"com.warehouse.routetracker"})
@ConfigurationPropertiesScan("com.warehouse.routetracker")
public class RouteTrackerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteTrackerWebApplication.class, args);
    }

}