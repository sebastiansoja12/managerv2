package com.warehouse.returntoken.configuration;

import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;
import com.warehouse.returntoken.domain.port.primary.ReturnTokenPortImpl;
import com.warehouse.returntoken.domain.service.ReturnTokenService;
import com.warehouse.returntoken.domain.service.ReturnTokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReturnTokenConfiguration {

    @Bean
    public ReturnTokenPort returnTokenPort() {
        final ReturnTokenService returnTokenService = new ReturnTokenServiceImpl();
        return new ReturnTokenPortImpl(returnTokenService);
    }
}
