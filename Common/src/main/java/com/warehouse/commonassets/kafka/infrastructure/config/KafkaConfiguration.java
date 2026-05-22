package com.warehouse.commonassets.kafka.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaOperatorContextRecordInterceptor;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public RecordMessageConverter kafkaRecordMessageConverter(final ObjectMapper objectMapper) {
        return new StringJsonMessageConverter(objectMapper);
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
