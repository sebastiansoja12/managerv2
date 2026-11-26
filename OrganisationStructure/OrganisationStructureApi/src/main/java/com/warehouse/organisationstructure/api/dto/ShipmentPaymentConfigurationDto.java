package com.warehouse.organisationstructure.api.dto;

public record ShipmentPaymentConfigurationDto(
        boolean allowCashOnDelivery,
        double maxCashOnDeliveryAmount,
        CashOnDeliveryPaymentMethodDto cashOnDeliveryPaymentMethod,
        double requireInsuranceAboveValue,
        Integer paymentDeadlineHours,
        Boolean paymentsEnabled,
        String billingStreet,
        String billingPostalCode,
        String billingCity,
        String billingCountryCode,
        String bankAccount
) {

    public ShipmentPaymentConfigurationDto(final boolean allowCashOnDelivery,
                                           final double maxCashOnDeliveryAmount,
                                           final CashOnDeliveryPaymentMethodDto cashOnDeliveryPaymentMethod,
                                           final double requireInsuranceAboveValue,
                                           final Integer paymentDeadlineHours,
                                           final Boolean paymentsEnabled) {
        this(allowCashOnDelivery, maxCashOnDeliveryAmount, cashOnDeliveryPaymentMethod, requireInsuranceAboveValue,
                paymentDeadlineHours, paymentsEnabled, null, null, null, null, null);
    }
}
