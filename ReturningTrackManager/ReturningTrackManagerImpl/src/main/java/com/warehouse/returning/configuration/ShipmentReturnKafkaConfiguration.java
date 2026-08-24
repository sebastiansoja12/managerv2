package com.warehouse.returning.configuration;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.IgnoredShipmentEvent;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCanceled;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCreated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

@Configuration
public class ShipmentReturnKafkaConfiguration {

    @Bean
    public RecordMessageConverter shipmentReturnKafkaRecordMessageConverter(final ObjectMapper objectMapper) {
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setIdClassMapping(Map.of(
                "ShipmentCreated", IgnoredShipmentEvent.class,
                "ShipmentChanged", IgnoredShipmentEvent.class,
                "ShipmentReturned", ShipmentReturnCreated.class,
                "ShipmentReturnCreated", ShipmentReturnCreated.class,
                "ShipmentReturnCanceled", ShipmentReturnCanceled.class
        ));
        typeMapper.addTrustedPackages("com.warehouse.returning.infrastructure.adapter.primary.kafka.event");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        final StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}
