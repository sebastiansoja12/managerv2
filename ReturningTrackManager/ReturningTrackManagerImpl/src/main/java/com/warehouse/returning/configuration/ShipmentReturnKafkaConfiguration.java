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
        typeMapper.setIdClassMapping(Map.ofEntries(
                Map.entry("ShipmentCanceled", IgnoredShipmentEvent.class),
                Map.entry("ShipmentCreated", IgnoredShipmentEvent.class),
                Map.entry("ShipmentCreatedEvent", IgnoredShipmentEvent.class),
                Map.entry("ShipmentChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentCountriesChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentCurrencyChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentDangerousGoodAdded", IgnoredShipmentEvent.class),
                Map.entry("ShipmentDangerousGoodRemoved", IgnoredShipmentEvent.class),
                Map.entry("ShipmentDangerousGoodUpdated", IgnoredShipmentEvent.class),
                Map.entry("ShipmentDelivered", IgnoredShipmentEvent.class),
                Map.entry("ShipmentDestinationChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentLocked", IgnoredShipmentEvent.class),
                Map.entry("ShipmentRecipientChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentRedirected", IgnoredShipmentEvent.class),
                Map.entry("ShipmentRelatedLocked", IgnoredShipmentEvent.class),
                Map.entry("ShipmentRerouted", IgnoredShipmentEvent.class),
                Map.entry("ShipmentSenderChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentSent", IgnoredShipmentEvent.class),
                Map.entry("ShipmentStatusChangedEvent", IgnoredShipmentEvent.class),
                Map.entry("ShipmentTypeChanged", IgnoredShipmentEvent.class),
                Map.entry("ShipmentUpdated", IgnoredShipmentEvent.class),
                Map.entry("SignatureChangedEvent", IgnoredShipmentEvent.class),
                Map.entry("SignatureSigned", IgnoredShipmentEvent.class),
                Map.entry("ShipmentReturned", ShipmentReturnCreated.class),
                Map.entry("ShipmentReturnCreated", ShipmentReturnCreated.class),
                Map.entry("ShipmentReturnCanceled", ShipmentReturnCanceled.class)
        ));
        typeMapper.addTrustedPackages("com.warehouse.returning.infrastructure.adapter.primary.kafka.event");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        final StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}
