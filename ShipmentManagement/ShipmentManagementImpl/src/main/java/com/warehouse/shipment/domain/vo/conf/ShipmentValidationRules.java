package com.warehouse.shipment.domain.vo.conf;

public record ShipmentValidationRules(
        boolean validateAddressData,
        boolean requireRecipientPhone,
        boolean requireRecipientEmail,
        boolean preventDuplicateTracking,
        boolean requireSenderReference,
        boolean validatePostalCode
) {

    public static ShipmentValidationRules defaults() {
        return new ShipmentValidationRules(true, true, false, true, false, true);
    }
}
