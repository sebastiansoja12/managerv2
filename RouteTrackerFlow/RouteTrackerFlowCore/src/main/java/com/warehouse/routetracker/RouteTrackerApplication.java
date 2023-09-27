package com.warehouse.routetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "com.warehouse.routetracker",
        exclude = { DataSourceAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@EntityScan(basePackages = {"com.warehouse.routetracker"})
@EnableMongoRepositories(basePackages = {"com.warehouse.routetracker"})
@ConfigurationPropertiesScan("com.warehouse.routetracker")
public class RouteTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteTrackerApplication.class, args);
    }

}
