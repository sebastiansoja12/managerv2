package com.warehouse.eventoutbox.outbox;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventJpaRepository;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventRepositoryImpl;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventEntityMapper;

@Configuration
@EnableJpaRepositories(basePackageClasses = OutboxEventJpaRepository.class)
@EntityScan(basePackageClasses = OutboxEventEntity.class)
public class OutboxPersistenceTestConfiguration {

    @Bean
    public OutboxEventRepository outboxEventRepository(final OutboxEventJpaRepository outboxEventJpaRepository) {
        return new OutboxEventRepositoryImpl(outboxEventJpaRepository, new OutboxEventEntityMapper());
    }
}
