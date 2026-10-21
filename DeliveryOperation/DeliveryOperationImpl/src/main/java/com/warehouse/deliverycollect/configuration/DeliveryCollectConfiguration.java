package com.warehouse.deliverycollect.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverycollect.api.DeliveryCollectApiService;
import com.warehouse.deliverycollect.application.port.primary.DeliveryCollectPort;
import com.warehouse.deliverycollect.application.port.primary.DeliveryCollectPortImpl;
import com.warehouse.deliverycollect.application.port.secondary.ShipmentCollectServicePort;
import com.warehouse.deliverycollect.infrastructure.adapter.primary.DeliveryCollectApiServiceAdapter;
import com.warehouse.deliverycollect.infrastructure.adapter.primary.mapper.DeliveryCollectApiMapper;
import com.warehouse.deliverycollect.infrastructure.adapter.secondary.ShipmentCollectServiceAdapter;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

@Configuration
public class DeliveryCollectConfiguration {

    @Bean
    public DeliveryCollectApiService deliveryCollectApiService(final DeliveryCollectPort deliveryCollectPort) {
        return new DeliveryCollectApiServiceAdapter(deliveryCollectPort, new DeliveryCollectApiMapper());
    }

    @Bean
    public DeliveryCollectPort deliveryCollectPort(final ShipmentCollectServicePort shipmentCollectServicePort) {
        return new DeliveryCollectPortImpl(shipmentCollectServicePort);
    }

    @Bean
    public ShipmentCollectServicePort shipmentCollectServicePort(final ShipmentApiService shipmentApiService,
                                                                 final ProcessHubEventPublisher processHubEventPublisher) {
        return new ShipmentCollectServiceAdapter(shipmentApiService, processHubEventPublisher);
    }
}
