package com.warehouse.routetracker.configuration;

import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJacksonJsonMessageConverter;

@Configuration
public class ShipmentKafkaConfiguration {

    @Bean
    @Primary
    public RecordMessageConverter shipmentKafkaRecordMessageConverter(final JsonMapper jsonMapper) {
        return new StringJacksonJsonMessageConverter(jsonMapper);
    }
}
