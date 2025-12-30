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
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {"com.warehouse.returntoken", "com.warehouse.deliverytoken"})
@EntityScan(basePackages = {"com.warehouse.returntoken", "com.warehouse.deliverytoken"})
@EnableJpaRepositories(basePackages = {"com.warehouse.returntoken", "com.warehouse.deliverytoken"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.returntoken", "com.warehouse.deliverytoken"})
public class DeliveryProtectionApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DeliveryProtectionApplication.class, args);
    }

}
