package com.warehouse.terminal.configuration;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.terminal.domain.port.secondary.SoftwareConfigurationServicePort;

@ComponentScan(basePackages = { "com.warehouse.terminal"})
@EntityScan(basePackages = { "com.warehouse.terminal"})
@EnableJpaRepositories(basePackages = { "com.warehouse.terminal"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.terminal"})
public class DeviceTestConfiguration {

    @MockBean
    public SoftwareConfigurationServicePort softwareConfigurationServicePort;
}
