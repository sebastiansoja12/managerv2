package com.warehouse.commonassets.kafka.infrastructure.config;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.util.backoff.FixedBackOff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaOperatorContextRecordInterceptor;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public RecordMessageConverter kafkaRecordMessageConverter(
            final ObjectMapper objectMapper,
            @Value("${manager.kafka.type-mappings:}") final String typeMappings) {
        final StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        converter.setTypeMapper(this.kafkaTypeMapper(typeMappings));
        return converter;
    }

    private DefaultJackson2JavaTypeMapper kafkaTypeMapper(final String typeMappings) {
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        final Map<String, Class<?>> mappings = new LinkedHashMap<>();
        for (final String mapping : typeMappings.split(",")) {
            if (mapping.isBlank()) {
                continue;
            }
            final String[] mappingParts = mapping.split(":", 2);
            if (mappingParts.length != 2) {
                throw new IllegalArgumentException("Invalid Kafka type mapping: " + mapping);
            }
            mappings.put(mappingParts[0].trim(), this.classForName(mappingParts[1].trim()));
        }
        typeMapper.setIdClassMapping(mappings);
        return typeMapper;
    }

    private Class<?> classForName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException exception) {
            throw new IllegalArgumentException("Invalid Kafka type mapping class: " + className, exception);
        }
    }

    @Bean
    public KafkaOperatorContextRecordInterceptor kafkaOperatorContextRecordInterceptor(
            final OperatorContext operatorContext) {
        return new KafkaOperatorContextRecordInterceptor(operatorContext);
    }

    @Bean
    public DeadLetterPublishingRecoverer kafkaDeadLetterPublishingRecoverer(
            final KafkaTemplate<String, String> kafkaTemplate,
            @Value("${manager.kafka.consumer.dlt-suffix:.DLT}") final String dltSuffix,
            @Value("${manager.kafka.consumer.dlt.publish-timeout-ms:10000}") final long publishTimeoutMs) {
        if (dltSuffix.isBlank()) {
            throw new IllegalArgumentException("Kafka consumer DLT suffix must not be blank");
        }
        if (publishTimeoutMs <= 0) {
            throw new IllegalArgumentException("Kafka consumer DLT publish timeout must be positive");
        }

        final DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(
                        record.topic() + dltSuffix,
                        record.partition()));
        recoverer.setAppendOriginalHeaders(true);
        recoverer.setFailIfSendResultIsError(true);
        recoverer.setWaitForSendResultTimeout(Duration.ofMillis(publishTimeoutMs));
        return recoverer;
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            @Qualifier("kafkaDeadLetterPublishingRecoverer") final DeadLetterPublishingRecoverer recoverer,
            @Value("${manager.kafka.consumer.retry.max-attempts:3}") final long maxAttempts,
            @Value("${manager.kafka.consumer.retry.backoff-ms:1000}") final long backoffMs) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Kafka consumer retry max attempts must be positive");
        }
        if (backoffMs < 0) {
            throw new IllegalArgumentException("Kafka consumer retry backoff must not be negative");
        }

        final DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(backoffMs, maxAttempts - 1));
        errorHandler.setAckAfterHandle(true);
        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            final ConsumerFactory<String, String> consumerFactory,
            final RecordMessageConverter kafkaRecordMessageConverter,
            final KafkaOperatorContextRecordInterceptor kafkaOperatorContextRecordInterceptor,
            @Qualifier("kafkaErrorHandler") final DefaultErrorHandler errorHandler) {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(kafkaRecordMessageConverter);
        factory.setRecordInterceptor(kafkaOperatorContextRecordInterceptor);
        factory.setCommonErrorHandler(errorHandler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setDeliveryAttemptHeader(true);
        return factory;
    }
}
