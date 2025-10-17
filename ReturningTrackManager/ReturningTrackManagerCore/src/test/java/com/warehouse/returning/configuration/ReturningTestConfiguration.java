package com.warehouse.returning.configuration;


import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.warehouse.returning.domain.provider.JwtProvider;
import com.warehouse.returning.domain.service.ApiKeyService;

@TestConfiguration
public class ReturningTestConfiguration {

    @Bean
    public TenantMdcFilter tenantMdcFilter() {
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
