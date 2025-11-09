package com.warehouse.pallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.modulith.Modulith;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.warehouse.pallet")
@EntityScan(basePackages = {"com.warehouse.pallet"})
@ConfigurationPropertiesScan("com.warehouse.pallet")
@Modulith(useFullyQualifiedModuleNames = true)
public class PalletHandlingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PalletHandlingApplication.class, args);
    }
}