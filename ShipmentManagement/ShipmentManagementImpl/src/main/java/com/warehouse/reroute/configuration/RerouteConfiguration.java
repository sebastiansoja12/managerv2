package com.warehouse.reroute.configuration;

import java.time.Clock;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.reroute.application.port.primary.RerouteTokenPort;
import com.warehouse.reroute.application.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.application.port.secondary.RerouteIntegrationEventServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteNotificationServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.application.port.secondary.ShipmentRerouteServicePort;
import com.warehouse.reroute.domain.service.RerouteTokenValueGenerator;
import com.warehouse.reroute.domain.vo.RerouteTokenPolicy;
import com.warehouse.reroute.infrastructure.adapter.primary.ExpiredRerouteTokenCleanupJob;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteApiMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.validator.RerouteRequestValidator;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteIntegrationEventServiceAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteNotificationServiceAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenJpaRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenRepositoryAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.ShipmentRerouteServiceAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenPersistenceMapper;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

@Configuration
public class RerouteConfiguration {

    @Bean
    public RerouteTokenPort rerouteTokenPort(final RerouteTokenRepository rerouteTokenRepository,
                                             final ShipmentRerouteServicePort shipmentRerouteServicePort,
                                             final RerouteNotificationServicePort rerouteNotificationServicePort,
                                             final RerouteIntegrationEventServicePort rerouteIntegrationEventServicePort,
                                             final RerouteTokenPolicy rerouteTokenPolicy) {
        return new RerouteTokenPortImpl(rerouteTokenRepository, shipmentRerouteServicePort,
                rerouteNotificationServicePort, rerouteIntegrationEventServicePort, new RerouteTokenValueGenerator(),
                rerouteTokenPolicy, Clock.systemUTC());
    }

    @Bean
    public RerouteIntegrationEventServicePort rerouteIntegrationEventServicePort(
            final IntegrationEventPublisher integrationEventPublisher) {
        return new RerouteIntegrationEventServiceAdapter(integrationEventPublisher);
    }

    @Bean
    public RerouteTokenPolicy rerouteTokenPolicy(
            @Value("${reroute.token-validity:PT30M}") final Duration tokenValidity,
            @Value("${reroute.max-failed-attempts:5}") final int maxFailedAttempts) {
        return new RerouteTokenPolicy(tokenValidity, maxFailedAttempts);
    }

    @Bean
    public RerouteTokenRepository rerouteTokenRepository(final RerouteTokenJpaRepository rerouteTokenJpaRepository) {
        return new RerouteTokenRepositoryAdapter(rerouteTokenJpaRepository, new RerouteTokenPersistenceMapper());
    }

    @Bean
    public ShipmentRerouteServicePort shipmentRerouteServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentRerouteServiceAdapter(shipmentApiService);
    }

    @Bean
    public RerouteNotificationServicePort rerouteNotificationServicePort(
            final NotificationEventPublisher notificationEventPublisher,
            @Value("${reroute.shipment-edit-url:http://localhost:3000/shipment-edit}") final String shipmentEditUrl) {
        return new RerouteNotificationServiceAdapter(notificationEventPublisher, shipmentEditUrl);
    }

    @Bean
    public RerouteApiMapper rerouteApiMapper() {
        return new RerouteApiMapper();
    }

    @Bean
    public RerouteRequestValidator rerouteRequestValidator() {
        return new RerouteRequestValidator();
    }

    @Bean
    public ExpiredRerouteTokenCleanupJob expiredRerouteTokenCleanupJob(final RerouteTokenPort rerouteTokenPort) {
        return new ExpiredRerouteTokenCleanupJob(rerouteTokenPort);
    }
}
