package com.warehouse.returntoken.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;
import com.warehouse.returntoken.domain.port.primary.ReturnTokenPortImpl;
import com.warehouse.returntoken.domain.port.secondary.ReturnTokenRepository;
import com.warehouse.returntoken.domain.service.ReturnTokenService;
import com.warehouse.returntoken.domain.service.ReturnTokenServiceImpl;
import com.warehouse.returntoken.infrastructure.adapter.secondary.ReturnTokenRepositoryImpl;

@Configuration
public class ReturnTokenConfiguration {

    @Bean
    public ReturnTokenPort returnTokenPort(final ReturnTokenRepository returnTokenRepository) {
        final ReturnTokenService returnTokenService = new ReturnTokenServiceImpl(returnTokenRepository);
        return new ReturnTokenPortImpl(returnTokenService);
    }

    @Bean
    public ReturnTokenRepository returnTokenRepository() {
        return new ReturnTokenRepositoryImpl(null);
    }
}
