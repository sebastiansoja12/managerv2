package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.ImpersonationConsentPort;
import com.warehouse.auth.domain.port.primary.ImpersonationConsentPortImpl;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.ImpersonationConsentReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.ImpersonationConsentRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ImpersonationConsentConfiguration {

    @Bean
    public ImpersonationConsentRepository impersonationConsentRepository(
            final ImpersonationConsentReadRepository impersonationConsentReadRepository) {
        return new ImpersonationConsentRepositoryImpl(impersonationConsentReadRepository);
    }

    @Bean
    public ImpersonationConsentPort impersonationConsentPort(
            final ImpersonationConsentRepository impersonationConsentRepository) {
        return new ImpersonationConsentPortImpl(impersonationConsentRepository, Clock.systemUTC());
    }
}
