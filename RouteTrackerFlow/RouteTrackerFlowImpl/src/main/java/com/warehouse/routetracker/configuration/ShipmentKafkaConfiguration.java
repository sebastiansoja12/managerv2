package com.warehouse.routetracker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChanged;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreated;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentReturned;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

import java.util.Map;

@Configuration
public class ShipmentKafkaConfiguration {

    @Bean
    public RecordMessageConverter shipmentKafkaRecordMessageConverter(final ObjectMapper objectMapper) {
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setIdClassMapping(Map.of(
                "ShipmentCreated", ShipmentCreated.class,
                "ShipmentReturned", ShipmentReturned.class,
                "ShipmentReturnCreated", ShipmentReturned.class,
                "ShipmentReturnCanceled", ShipmentChanged.class,
                "ShipmentChanged", ShipmentChanged.class
        ));
        typeMapper.addTrustedPackages("com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        final StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}
