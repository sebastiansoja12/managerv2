package com.warehouse.returning.configuration;


import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.returning.domain.provider.JwtProvider;
import com.warehouse.returning.domain.service.ApiKeyService;

@ComponentScan(basePackages = { "com.warehouse.returning" })
@EntityScan(basePackages = { "com.warehouse.returning" })
@EnableJpaRepositories(basePackages = { "com.warehouse.returning" })
public class ReturningTestConfiguration {

    @Bean
    private TenantMdcFilter tenantMdcFilter() {
        return Mockito.mock(TenantMdcFilter.class);
    }

    @Bean
    public ApiKeyService apiKeyService() {
        return Mockito.mock(ApiKeyService.class);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return Mockito.mock(JwtProvider.class);
    }

    @Bean
    public ShipmentProperties shipmentProperties() {
        return Mockito.mock(ShipmentProperties.class);
    }
}
