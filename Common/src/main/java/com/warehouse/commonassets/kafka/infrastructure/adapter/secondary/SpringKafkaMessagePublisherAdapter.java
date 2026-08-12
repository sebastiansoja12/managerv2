package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;

@Component
public class SpringKafkaMessagePublisherAdapter implements KafkaMessagePublisherPort {

    private final KafkaTemplateClient kafkaTemplateClient;

    public SpringKafkaMessagePublisherAdapter(final KafkaTemplateClient kafkaTemplateClient) {
        this.kafkaTemplateClient = kafkaTemplateClient;
    }

    @Override
    public CompletableFuture<Void> publish(final String topic,
                                           final String key,
                                           final String payload,
                                           final Map<String, String> headers) {
        return this.kafkaTemplateClient.publishSerialized(topic, key, payload, headers);
    }
}
