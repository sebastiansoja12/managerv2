package com.warehouse.returning;

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
@SpringBootApplication(scanBasePackages = "com.warehouse.returning")
@EntityScan(basePackages = {"com.warehouse.returning"})
@EnableJpaRepositories(basePackages = {"com.warehouse.returning"})
@ConfigurationPropertiesScan("com.warehouse.returning")
public class ReturningTrackSpringApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReturningTrackSpringApplication.class, args);
    }

}
