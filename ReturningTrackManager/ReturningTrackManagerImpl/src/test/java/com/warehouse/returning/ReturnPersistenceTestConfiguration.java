package com.warehouse.returning;

import com.warehouse.eventoutbox.configuration.EventOutboxConfiguration;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventJpaRepository;
import com.warehouse.returning.application.listener.ReturnIntegrationEventTranslator;
import com.warehouse.returning.application.service.ReturnProcessingService;
import com.warehouse.returning.configuration.ReturningConfiguration;
import com.warehouse.returning.domain.registry.DomainRegistry;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.event.ReturnIntegrationEventContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@EntityScan(basePackageClasses = {ReturnPackageEntity.class, OutboxEventEntity.class})
@EnableJpaRepositories(basePackageClasses = {ReturnReadRepository.class, OutboxEventJpaRepository.class})
@Import({EventOutboxConfiguration.class, ReturningConfiguration.class, ReturnProcessingService.class,
        ReturnIntegrationEventTranslator.class, ReturnIntegrationEventContext.class, DomainRegistry.class})
public class ReturnPersistenceTestConfiguration {
}
