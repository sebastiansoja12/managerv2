package com.warehouse.routetracker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class ShipmentKafkaConfiguration {

    @Bean
    @Primary
    public RecordMessageConverter shipmentKafkaRecordMessageConverter(final ObjectMapper objectMapper) {
        return new StringJsonMessageConverter(objectMapper);
    }
}
