package com.warehouse.organisationstructure.api.dto;

public record ShipmentDataRetentionConfigurationDto(
        int anonymizeRecipientDataAfterDays,
        int archiveClosedShipmentsAfterDays,
        int deliveryPhotoRetentionDays
) {
}
