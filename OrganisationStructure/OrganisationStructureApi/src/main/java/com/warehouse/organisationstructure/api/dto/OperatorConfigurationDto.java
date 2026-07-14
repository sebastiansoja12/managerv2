package com.warehouse.organisationstructure.api.dto;

public record OperatorConfigurationDto(
        ShippingCapabilitiesDto shippingCapabilities,
        ShipmentLimitsDto shipmentLimits,
        DeliveryTimeConfigurationDto deliveryTimeConfiguration
) {
    public record ShippingCapabilitiesDto(
            boolean supportsDomesticShipping,
            boolean supportsInternationalShipping,
            boolean supportsExpressShipping,
            boolean supportsSameDayDelivery,
            boolean supportsCashOnDelivery,
            boolean supportsParcelLockers,
            boolean supportsPickupPoints,
            boolean supportsHomeDelivery,
            boolean supportsSaturdayDelivery,
            boolean supportsSundayDelivery,
            boolean supportsReturnShipments,
            boolean providesTracking,
            boolean providesInsurance
    ) {
    }

    public record ShipmentLimitsDto(
            double maxWeight,
            double minWeight,
            double maxLength,
            double maxWidth,
            double maxHeight,
            double maxShipmentValue
    ) {
    }

    public record DeliveryTimeConfigurationDto(
            int minDeliveryDays,
            int maxDeliveryDays,
            int expressDeliveryDays,
            int sameDayDeliveryHours,
            int internationalDeliveryDays
    ) {
    }
}
