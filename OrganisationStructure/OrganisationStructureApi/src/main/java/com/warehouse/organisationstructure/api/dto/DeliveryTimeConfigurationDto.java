package com.warehouse.organisationstructure.api.dto;

public record DeliveryTimeConfigurationDto(
        int minDeliveryDays,
        int maxDeliveryDays,
        int expressDeliveryDays,
        int sameDayDeliveryHours,
        int internationalDeliveryDays
) {
}
