package com.warehouse.pallet.configuration;

import tools.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJacksonJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class PalletKafkaConfiguration {

    @Bean
    @Primary
    public RecordMessageConverter palletKafkaRecordMessageConverter(final JsonMapper jsonMapper) {
        return new StringJacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public DefaultErrorHandler palletKafkaErrorHandler(
            final KafkaTemplate<Object, Object> kafkaTemplate,
            @Value("${manager.kafka.consumer.dlt-suffix:.DLT}") final String dltSuffix,
            @Value("${manager.kafka.consumer.retry.max-attempts:3}") final long maxAttempts,
            @Value("${manager.kafka.consumer.retry.backoff-ms:1000}") final long backoffMs) {
        final DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(record.topic() + dltSuffix, record.partition()));
        return new DefaultErrorHandler(recoverer, new FixedBackOff(backoffMs, Math.max(0, maxAttempts - 1)));
    }
}
