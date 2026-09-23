package com.warehouse.returning;

import com.warehouse.returning.application.listener.ReturnIntegrationEventListener;
import com.warehouse.returning.application.service.ReturnProcessingService;
import com.warehouse.returning.configuration.ReturningConfiguration;
import com.warehouse.returning.domain.registry.DomainRegistry;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.outbox.ReturningOutboxEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.outbox.ReturningOutboxIntegrationEventPublisher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.warehouse.returning.infrastructure.adapter.secondary.outbox.ReturningOutboxStore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@EntityScan(basePackageClasses = {ReturnPackageEntity.class, ReturningOutboxEntity.class})
@EnableJpaRepositories(basePackageClasses = ReturnReadRepository.class)
@Import({ReturningConfiguration.class, ReturnProcessingService.class, ReturnIntegrationEventListener.class,
        ReturningOutboxIntegrationEventPublisher.class, ReturningOutboxStore.class, DomainRegistry.class})
public class ReturnPersistenceTestConfiguration {
}
