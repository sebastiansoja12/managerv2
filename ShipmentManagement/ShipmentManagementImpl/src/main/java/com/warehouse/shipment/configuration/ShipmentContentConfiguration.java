package com.warehouse.shipment.configuration;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.shipment.application.port.primary.ShipmentContentPort;
import com.warehouse.shipment.application.port.primary.ShipmentContentPortImpl;
import com.warehouse.shipment.application.port.secondary.AuditLogServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentContentRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.service.ShipmentChangeDetector;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentContentApiMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.AuditLogServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentContentPersistenceMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShipmentContentConfiguration {

    @Bean
    public ShipmentContentPort shipmentContentPort(final ShipmentRepository shipmentRepository,
                                                   final ShipmentContentRepository contentRepository,
                                                   final ShipmentActorServicePort actorServicePort,
                                                   final DomainEventPublisher domainEventPublisher) {
        return new ShipmentContentPortImpl(shipmentRepository, contentRepository, actorServicePort,
                domainEventPublisher);
    }

    @Bean("shipmentAuditLogServicePort")
    @ConditionalOnProperty(
            name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
            havingValue = "true")
    public AuditLogServicePort shipmentAuditLogServicePort(
            final IntegrationEventPublisher integrationEventPublisher) {
        return new AuditLogServiceAdapter(integrationEventPublisher);
    }

    @Bean
    public ShipmentChangeDetector shipmentChangeDetector() {
        return new ShipmentChangeDetector();
    }

    @Bean
    public ShipmentContentApiMapper shipmentContentApiMapper() {
        return new ShipmentContentApiMapper();
    }

    @Bean
    public ShipmentContentPersistenceMapper shipmentContentPersistenceMapper() {
        return new ShipmentContentPersistenceMapper();
    }
}
