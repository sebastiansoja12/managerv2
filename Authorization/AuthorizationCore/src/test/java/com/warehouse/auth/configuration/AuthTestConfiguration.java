package com.warehouse.auth.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.depot.api.DepotService;

@ComponentScan(basePackages = { "com.warehouse.auth"})
@EntityScan(basePackages = { "com.warehouse.auth"})
@EnableJpaRepositories(basePackages = { "com.warehouse.auth"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.auth"})
@EnableConfigurationProperties({JwtProvider.class})
public class AuthTestConfiguration {

    @MockBean
    public DepotService depotService;


}
