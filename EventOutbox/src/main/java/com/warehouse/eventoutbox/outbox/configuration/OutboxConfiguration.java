package com.warehouse.eventoutbox.outbox.configuration;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxPublicationMetrics;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.application.service.OutboxCleanup;
import com.warehouse.eventoutbox.outbox.application.service.OutboxRelay;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.primary.OutboxRelayScheduler;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.EventMetadataFactory;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.KafkaOutboxDeadLetterAdapter;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.IntegrationEventOutboxAdapter;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventJpaRepository;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventRepositoryImpl;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventEntityMapper;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxEventFactory;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.MicrometerOutboxPublicationMetrics;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventSenderServiceAdapter;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.PostgresOutboxRelayLock;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventHeadersMapper;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventPayloadMapper;
import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.eventoutbox.publication.DomainEventTranslationBridge;
import com.warehouse.eventoutbox.publication.IntegrationEventTranslationRegistry;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(OutboxProperties.class)
@ConditionalOnProperty(name = "manager.events.outbox.enabled", havingValue = "true")
public class OutboxConfiguration {

    private static final Clock OUTBOX_CLOCK = Clock.systemUTC();

    @Bean
    public OutboxEventRepository outboxEventRepository(final OutboxEventJpaRepository outboxEventJpaRepository) {
        return new OutboxEventRepositoryImpl(outboxEventJpaRepository, new OutboxEventEntityMapper());
    }

    @Bean
    public IntegrationEventPublisher integrationEventPublisher(
            final OutboxProperties properties,
            final IntegrationEventRegistry integrationEventRegistry,
            final OutboxEventRepository outboxEventRepository,
            final ObjectProvider<IntegrationEventContext> integrationEventContext,
            final ObjectMapper objectMapper,
            final ObjectProvider<OutboxRelayScheduler> outboxRelayScheduler,
            final ObjectProvider<OutboxTracing> outboxTracing,
            @Value("${spring.application.name:unknown-service}") final String applicationName) {
        final IntegrationEventContext context = integrationEventContext.getIfAvailable(() -> IntegrationEventContext.NONE);
        return new IntegrationEventOutboxAdapter(
                integrationEventRegistry,
                outboxEventRepository,
                new EventMetadataFactory(context, OUTBOX_CLOCK, applicationName),
                new OutboxEventHeadersMapper(properties.legacyEnvelope()),
                new OutboxEventPayloadMapper(objectMapper, properties.legacyEnvelope(), context),
                new OutboxEventFactory(),
                outboxTracing.getIfAvailable(() -> OutboxTracing.NOOP),
                () -> outboxRelayScheduler.ifAvailable(OutboxRelayScheduler::wakeUp));
    }

    @Bean
    public DomainEventTranslationBridge domainEventTranslationBridge(
            final IntegrationEventTranslationRegistry integrationEventTranslationRegistry,
            final IntegrationEventPublisher integrationEventPublisher) {
        return new DomainEventTranslationBridge(integrationEventTranslationRegistry, integrationEventPublisher);
    }

    @Bean
    public OutboxPublicationMetrics outboxPublicationMetrics(final ObjectProvider<MeterRegistry> meterRegistry,
                                                             final OutboxEventRepository outboxEventRepository) {
        return new MicrometerOutboxPublicationMetrics(
                meterRegistry.getIfAvailable(SimpleMeterRegistry::new), outboxEventRepository, OUTBOX_CLOCK);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = "manager.events.outbox.relay.enabled", havingValue = "true")
    public ExecutorService outboxSendExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    @ConditionalOnProperty(name = "manager.events.outbox.relay.enabled", havingValue = "true")
    public OutboxRelay outboxRelay(final OutboxProperties properties,
                                   final OutboxEventRepository outboxEventRepository,
                                   final OutboxPublicationMetrics outboxPublicationMetrics,
                                   final JdbcTemplate jdbcTemplate,
                                   final KafkaTemplate<String, String> kafkaTemplate,
                                   final PlatformTransactionManager transactionManager,
                                   final ObjectProvider<OutboxTracing> outboxTracing,
                                   @Qualifier("outboxSendExecutor") final ExecutorService outboxSendExecutor) {
        final OutboxProperties.Relay relay = properties.relay();
        final OutboxProperties.Retry retry = properties.retry();
        return new OutboxRelay(
                outboxEventRepository,
                new PostgresOutboxRelayLock(jdbcTemplate, relay.lockKey()),
                new OutboxEventSenderServiceAdapter(kafkaTemplate, outboxTracing.getIfAvailable(() -> OutboxTracing.NOOP)),
                new KafkaOutboxDeadLetterAdapter(kafkaTemplate, properties.deadLetterTopic()),
                new OutboxRetryPolicy(retry.initialDelay(), retry.maxDelay(), retry.maxAttempts()),
                outboxPublicationMetrics,
                new TransactionTemplate(transactionManager),
                outboxSendExecutor,
                OUTBOX_CLOCK,
                relay.batchSize(),
                relay.sendTimeout());
    }

    @Bean
    @ConditionalOnProperty(name = "manager.events.outbox.relay.enabled", havingValue = "true")
    public OutboxRelayScheduler outboxRelayScheduler(final OutboxProperties properties,
                                                     final OutboxRelay outboxRelay,
                                                     final OutboxEventRepository outboxEventRepository) {
        final OutboxProperties.Cleanup cleanup = properties.cleanup();
        return new OutboxRelayScheduler(
                outboxRelay,
                new OutboxCleanup(outboxEventRepository, OUTBOX_CLOCK, cleanup.retention(), cleanup.batchSize()),
                properties.relay().pollInterval(),
                cleanup.interval());
    }
}
