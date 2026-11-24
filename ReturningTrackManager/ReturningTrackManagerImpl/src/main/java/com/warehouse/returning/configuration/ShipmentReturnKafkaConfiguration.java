package com.warehouse.returning.configuration;

import com.warehouse.eventoutbox.consumption.IntegrationEventMessageConverter;
import com.warehouse.eventoutbox.consumption.IntegrationEventTypeResolver;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import org.springframework.beans.factory.ObjectProvider;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class ShipmentReturnKafkaConfiguration {

    @Bean
    @Primary
    public RecordMessageConverter shipmentReturnKafkaRecordMessageConverter(
            final JsonMapper jsonMapper,
            final ObjectProvider<IntegrationEventRegistry> integrationEventRegistry) {
        return new IntegrationEventMessageConverter(jsonMapper,
                new IntegrationEventTypeResolver(integrationEventRegistry::getIfAvailable));
    }
}
