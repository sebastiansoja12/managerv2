package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentPaymentConfiguration {
    public static final int DEFAULT_PAYMENT_DEADLINE_HOURS = 72;

    private boolean allowCashOnDelivery;
    private double maxCashOnDeliveryAmount;
    private CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod;
    private double requireInsuranceAboveValue;
    private int paymentDeadlineHours = DEFAULT_PAYMENT_DEADLINE_HOURS;
    private boolean paymentsEnabled;
    private String billingStreet;
    private String billingPostalCode;
    private String billingCity;
    private String billingCountryCode;
    private String bankAccount;

    public ShipmentPaymentConfiguration() {
    }

    public ShipmentPaymentConfiguration(final boolean allowCashOnDelivery,
                                        final double maxCashOnDeliveryAmount,
                                        final CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod,
                                        final double requireInsuranceAboveValue,
                                        final Integer paymentDeadlineHours,
                                        final Boolean paymentsEnabled) {
        this(allowCashOnDelivery, maxCashOnDeliveryAmount, cashOnDeliveryPaymentMethod, requireInsuranceAboveValue,
                paymentDeadlineHours, paymentsEnabled, null, null, null, null, null);
    }

    public ShipmentPaymentConfiguration(final boolean allowCashOnDelivery,
                                        final double maxCashOnDeliveryAmount,
                                        final CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod,
                                        final double requireInsuranceAboveValue,
                                        final Integer paymentDeadlineHours,
                                        final Boolean paymentsEnabled,
                                        final String billingStreet,
                                        final String billingPostalCode,
                                        final String billingCity,
                                        final String billingCountryCode,
                                        final String bankAccount) {
        this.allowCashOnDelivery = allowCashOnDelivery;
        this.maxCashOnDeliveryAmount = maxCashOnDeliveryAmount;
        this.cashOnDeliveryPaymentMethod = cashOnDeliveryPaymentMethod;
        this.requireInsuranceAboveValue = requireInsuranceAboveValue;
        this.paymentDeadlineHours = paymentDeadlineHours != null ? paymentDeadlineHours
                : DEFAULT_PAYMENT_DEADLINE_HOURS;
        this.paymentsEnabled = Boolean.TRUE.equals(paymentsEnabled);
        this.billingStreet = billingStreet;
        this.billingPostalCode = billingPostalCode;
        this.billingCity = billingCity;
        this.billingCountryCode = billingCountryCode;
        this.bankAccount = bankAccount;
    }

    public static ShipmentPaymentConfiguration defaultConfiguration() {
        return new ShipmentPaymentConfiguration(true, 5000.0, CashOnDeliveryPaymentMethod.BOTH, 0.0,
                DEFAULT_PAYMENT_DEADLINE_HOURS, false);
    }

    public boolean isAllowCashOnDelivery() { return allowCashOnDelivery; }
    public double getMaxCashOnDeliveryAmount() { return maxCashOnDeliveryAmount; }
    public CashOnDeliveryPaymentMethod getCashOnDeliveryPaymentMethod() { return cashOnDeliveryPaymentMethod; }
    public double getRequireInsuranceAboveValue() { return requireInsuranceAboveValue; }
    public int getPaymentDeadlineHours() { return paymentDeadlineHours; }
    public boolean isPaymentsEnabled() { return paymentsEnabled; }
    public String getBillingStreet() { return billingStreet; }
    public String getBillingPostalCode() { return billingPostalCode; }
    public String getBillingCity() { return billingCity; }
    public String getBillingCountryCode() { return billingCountryCode; }
    public String getBankAccount() { return bankAccount; }
}
