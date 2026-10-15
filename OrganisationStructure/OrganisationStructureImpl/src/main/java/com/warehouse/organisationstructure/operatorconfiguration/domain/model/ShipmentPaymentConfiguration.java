package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentPaymentConfiguration {
    private boolean allowCashOnDelivery;
    private double maxCashOnDeliveryAmount;
    private CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod;
    private double requireInsuranceAboveValue;

    public ShipmentPaymentConfiguration() {
    }

    public ShipmentPaymentConfiguration(final boolean allowCashOnDelivery,
                                        final double maxCashOnDeliveryAmount,
                                        final CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod,
                                        final double requireInsuranceAboveValue) {
        this.allowCashOnDelivery = allowCashOnDelivery;
        this.maxCashOnDeliveryAmount = maxCashOnDeliveryAmount;
        this.cashOnDeliveryPaymentMethod = cashOnDeliveryPaymentMethod;
        this.requireInsuranceAboveValue = requireInsuranceAboveValue;
    }

    public static ShipmentPaymentConfiguration defaultConfiguration() {
        return new ShipmentPaymentConfiguration(true, 5000.0, CashOnDeliveryPaymentMethod.BOTH, 0.0);
    }

    public boolean isAllowCashOnDelivery() { return allowCashOnDelivery; }
    public double getMaxCashOnDeliveryAmount() { return maxCashOnDeliveryAmount; }
    public CashOnDeliveryPaymentMethod getCashOnDeliveryPaymentMethod() { return cashOnDeliveryPaymentMethod; }
    public double getRequireInsuranceAboveValue() { return requireInsuranceAboveValue; }
}
