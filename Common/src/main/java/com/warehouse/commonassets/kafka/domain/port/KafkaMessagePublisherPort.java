package com.warehouse.commonassets.kafka.domain.port;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface KafkaMessagePublisherPort {

    CompletableFuture<Void> publish(String topic, String key, String payload, Map<String, String> headers);
}
