package com.warehouse.organisationstructure.api.dto;

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
