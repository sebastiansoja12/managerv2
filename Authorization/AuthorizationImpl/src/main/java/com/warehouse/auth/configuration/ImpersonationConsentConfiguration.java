package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.ImpersonationConsentPort;
import com.warehouse.auth.domain.port.primary.ImpersonationConsentPortImpl;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentNotificationServicePort;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.ImpersonationConsentNotificationServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.ImpersonationConsentReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.ImpersonationConsentRepositoryImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;

@Configuration
public class ImpersonationConsentConfiguration {

    @Bean
    public ImpersonationConsentRepository impersonationConsentRepository(
            final ImpersonationConsentReadRepository impersonationConsentReadRepository) {
        return new ImpersonationConsentRepositoryImpl(impersonationConsentReadRepository);
    }

    @Bean
    public ImpersonationConsentNotificationServicePort impersonationConsentNotificationServicePort(
            final ObjectProvider<SimpMessagingTemplate> messagingTemplate,
            final UserRepository userRepository) {
        final SimpMessagingTemplate template = messagingTemplate.getIfAvailable();
        if (template == null) {
            return operatorId -> {
            };
        }
        return new ImpersonationConsentNotificationServiceAdapter(template, userRepository);
    }

    @Bean
    public ImpersonationConsentPort impersonationConsentPort(
            final ImpersonationConsentRepository impersonationConsentRepository,
            final ImpersonationConsentNotificationServicePort impersonationConsentNotificationServicePort) {
        return new ImpersonationConsentPortImpl(impersonationConsentRepository,
                impersonationConsentNotificationServicePort, Clock.systemUTC());
    }
}
