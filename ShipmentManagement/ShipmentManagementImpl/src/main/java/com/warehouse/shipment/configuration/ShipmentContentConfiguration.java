package com.warehouse.shipment.configuration;

import com.warehouse.shipment.application.port.primary.ShipmentContentPort;
import com.warehouse.shipment.application.port.primary.ShipmentContentPortImpl;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentContentRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentContentApiMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentContentPersistenceMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShipmentContentConfiguration {

    @Bean
    public ShipmentContentPort shipmentContentPort(final ShipmentRepository shipmentRepository,
                                                   final ShipmentContentRepository contentRepository,
                                                   final ShipmentActorServicePort actorServicePort) {
        return new ShipmentContentPortImpl(shipmentRepository, contentRepository, actorServicePort);
    }

    @Bean
    public ShipmentContentApiMapper shipmentContentApiMapper() {
        return new ShipmentContentApiMapper();
    }

    @Bean
    public ShipmentContentPersistenceMapper shipmentContentPersistenceMapper() {
        return new ShipmentContentPersistenceMapper();
    }
}
