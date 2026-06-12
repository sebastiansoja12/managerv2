package com.warehouse.commonassets.kafka.infrastructure.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

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
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
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
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            final ConsumerFactory<String, String> consumerFactory,
            final RecordMessageConverter kafkaRecordMessageConverter,
            final KafkaOperatorContextRecordInterceptor kafkaOperatorContextRecordInterceptor) {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(kafkaRecordMessageConverter);
        factory.setRecordInterceptor(kafkaOperatorContextRecordInterceptor);
        return factory;
    }
}
