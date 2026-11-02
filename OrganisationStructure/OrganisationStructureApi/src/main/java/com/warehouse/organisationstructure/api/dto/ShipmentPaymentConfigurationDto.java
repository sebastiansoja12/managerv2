package com.warehouse.organisationstructure.api.dto;

public record ShipmentPaymentConfigurationDto(
        boolean allowCashOnDelivery,
        double maxCashOnDeliveryAmount,
        CashOnDeliveryPaymentMethodDto cashOnDeliveryPaymentMethod,
        double requireInsuranceAboveValue
) {
}
