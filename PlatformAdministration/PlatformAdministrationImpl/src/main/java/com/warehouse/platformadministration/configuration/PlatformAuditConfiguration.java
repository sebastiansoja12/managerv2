package com.warehouse.platformadministration.configuration;

import com.warehouse.auth.PlatformActorService;
import com.warehouse.auth.PlatformOwnerApiService;
import com.warehouse.platformadministration.application.port.primary.PlatformAuditPort;
import com.warehouse.platformadministration.application.port.primary.PlatformAuditPortImpl;
import com.warehouse.platformadministration.application.port.primary.PlatformOwnerAccountPort;
import com.warehouse.platformadministration.application.port.primary.PlatformOwnerAccountPortImpl;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.application.port.secondary.PlatformOwnerAccountServicePort;
import com.warehouse.platformadministration.infrastructure.adapter.primary.PlatformAuditInterceptor;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.PlatformActorServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.PlatformAuditReadRepository;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.PlatformAuditRepositoryImpl;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.PlatformOwnerAccountServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PlatformAuditConfiguration {

    @Bean("platformAdministration.platformActorServicePort")
    public PlatformActorServicePort platformActorServicePort(final PlatformActorService platformActorService) {
        return new PlatformActorServiceAdapter(platformActorService);
    }

    @Bean
    public PlatformAuditRepository platformAuditRepository(final PlatformAuditReadRepository platformAuditReadRepository) {
        return new PlatformAuditRepositoryImpl(platformAuditReadRepository);
    }

    @Bean
    public PlatformAuditPort platformAuditPort(final PlatformActorServicePort platformActorServicePort,
                                               final PlatformAuditRepository platformAuditRepository) {
        return new PlatformAuditPortImpl(platformActorServicePort, platformAuditRepository);
    }

    @Bean
    public PlatformOwnerAccountServicePort platformOwnerAccountServicePort(
            final PlatformOwnerApiService platformOwnerApiService) {
        return new PlatformOwnerAccountServiceAdapter(platformOwnerApiService);
    }

    @Bean
    public PlatformOwnerAccountPort platformOwnerAccountPort(
            final PlatformOwnerAccountServicePort platformOwnerAccountServicePort,
            final PlatformActorServicePort platformActorServicePort) {
        return new PlatformOwnerAccountPortImpl(platformOwnerAccountServicePort, platformActorServicePort);
    }

    @Bean
    public WebMvcConfigurer platformAuditWebMvcConfigurer(final PlatformAuditPort platformAuditPort) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(final InterceptorRegistry registry) {
                registry.addInterceptor(new PlatformAuditInterceptor(platformAuditPort));
            }
        };
    }
}
