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
        typeMapper.setIdClassMapping(Map.ofEntries(
                Map.entry("ShipmentCanceled", ShipmentChanged.class),
                Map.entry("ShipmentCreated", ShipmentCreated.class),
                Map.entry("ShipmentCreatedEvent", ShipmentCreated.class),
                Map.entry("ShipmentChanged", ShipmentChanged.class),
                Map.entry("ShipmentCountriesChanged", ShipmentChanged.class),
                Map.entry("ShipmentCurrencyChanged", ShipmentChanged.class),
                Map.entry("ShipmentDangerousGoodAdded", ShipmentChanged.class),
                Map.entry("ShipmentDangerousGoodRemoved", ShipmentChanged.class),
                Map.entry("ShipmentDangerousGoodUpdated", ShipmentChanged.class),
                Map.entry("ShipmentDelivered", ShipmentChanged.class),
                Map.entry("ShipmentDestinationChanged", ShipmentChanged.class),
                Map.entry("ShipmentLocked", ShipmentChanged.class),
                Map.entry("ShipmentRecipientChanged", ShipmentChanged.class),
                Map.entry("ShipmentRedirected", ShipmentChanged.class),
                Map.entry("ShipmentRelatedLocked", ShipmentChanged.class),
                Map.entry("ShipmentRerouted", ShipmentChanged.class),
                Map.entry("ShipmentSenderChanged", ShipmentChanged.class),
                Map.entry("ShipmentSent", ShipmentChanged.class),
                Map.entry("ShipmentStatusChangedEvent", ShipmentChanged.class),
                Map.entry("ShipmentTypeChanged", ShipmentChanged.class),
                Map.entry("ShipmentUpdated", ShipmentChanged.class),
                Map.entry("SignatureChangedEvent", ShipmentChanged.class),
                Map.entry("SignatureSigned", ShipmentChanged.class),
                Map.entry("ShipmentReturned", ShipmentReturned.class),
                Map.entry("ShipmentReturnCreated", ShipmentReturned.class),
                Map.entry("ShipmentReturnCanceled", ShipmentChanged.class)
        ));
        typeMapper.addTrustedPackages("com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        final StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}
