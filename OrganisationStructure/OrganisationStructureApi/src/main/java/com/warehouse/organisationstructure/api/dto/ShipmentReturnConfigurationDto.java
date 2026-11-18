package com.warehouse.organisationstructure.api.dto;

public record ShipmentReturnConfigurationDto(
        boolean allowReturns,
        int returnWindowDays,
        boolean autoReturnAfterFailedDelivery,
        int pickupPointStorageDays,
        boolean requireReturnReason
) {
}
