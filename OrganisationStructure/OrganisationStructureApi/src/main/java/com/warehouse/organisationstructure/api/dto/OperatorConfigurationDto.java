package com.warehouse.organisationstructure.api.dto;

public record OperatorConfigurationDto(
        ShippingCapabilitiesDto shippingCapabilities,
        ShipmentConfigurationDto shipmentConfiguration,
        DeliveryTimeConfigurationDto deliveryTimeConfiguration
) {
}
