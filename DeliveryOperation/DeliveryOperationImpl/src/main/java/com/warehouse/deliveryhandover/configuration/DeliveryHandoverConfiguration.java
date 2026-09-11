package com.warehouse.deliveryhandover.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.deliveryhandover.api.DeliveryHandoverApiService;
import com.warehouse.deliveryhandover.application.port.primary.DeliveryHandoverPort;
import com.warehouse.deliveryhandover.application.port.primary.DeliveryHandoverPortImpl;
import com.warehouse.deliveryhandover.application.port.secondary.ShipmentHandoverServicePort;
import com.warehouse.deliveryhandover.application.port.secondary.SupplierConfigurationServicePort;
import com.warehouse.deliveryhandover.infrastructure.adapter.primary.DeliveryHandoverApiServiceAdapter;
import com.warehouse.deliveryhandover.infrastructure.adapter.primary.mapper.DeliveryHandoverApiMapper;
import com.warehouse.deliveryhandover.infrastructure.adapter.secondary.ShipmentHandoverServiceAdapter;
import com.warehouse.deliveryhandover.infrastructure.adapter.secondary.SupplierConfigurationServiceAdapter;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.supplier.api.SupplierConfigurationApiService;

@Configuration
public class DeliveryHandoverConfiguration {

    @Bean
    public DeliveryHandoverApiService deliveryHandoverApiService(final DeliveryHandoverPort deliveryHandoverPort) {
        return new DeliveryHandoverApiServiceAdapter(deliveryHandoverPort, new DeliveryHandoverApiMapper());
    }

    @Bean
    public DeliveryHandoverPort deliveryHandoverPort(
            final ShipmentHandoverServicePort shipmentHandoverServicePort,
            final SupplierConfigurationServicePort deliveryHandoverSupplierConfigurationServicePort) {
        return new DeliveryHandoverPortImpl(shipmentHandoverServicePort, deliveryHandoverSupplierConfigurationServicePort);
    }

    @Bean
    public ShipmentHandoverServicePort shipmentHandoverServicePort(final ShipmentApiService shipmentApiService,
                                                                   final ProcessHubEventPublisher processHubEventPublisher) {
        return new ShipmentHandoverServiceAdapter(shipmentApiService, processHubEventPublisher);
    }

    @Bean
    public SupplierConfigurationServicePort deliveryHandoverSupplierConfigurationServicePort(
            final SupplierConfigurationApiService supplierConfigurationApiService,
            final OperatorContextProvider operatorContextProvider) {
        return new SupplierConfigurationServiceAdapter(supplierConfigurationApiService, operatorContextProvider);
    }
}
