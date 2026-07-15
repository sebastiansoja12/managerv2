package com.warehouse.organisationstructure.api.dto;

public record ShipmentValidationConfigurationDto(
        boolean validateAddressData,
        boolean requireRecipientPhone,
        boolean requireRecipientEmail,
        boolean preventDuplicateTracking,
        boolean requireSenderReference,
        boolean validatePostalCode
) {
}
