package com.warehouse.organisationstructure.api.dto;

public record OperatorConfigurationDto(
        ShippingCapabilitiesDto shippingCapabilities,
        ShipmentLimitsDto shipmentLimits,
        DeliveryTimeConfigurationDto deliveryTimeConfiguration
) {
}
