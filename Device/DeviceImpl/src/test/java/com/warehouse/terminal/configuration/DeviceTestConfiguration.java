package com.warehouse.terminal.configuration;


import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackages = {
        "com.warehouse.terminal.infrastructure.adapter.secondary.entity",
        "com.warehouse.commonassets.identificator"
})
@EnableJpaRepositories(basePackages = { "com.warehouse.terminal.infrastructure.adapter.secondary" })
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.terminal"})
public class DeviceTestConfiguration {
}
