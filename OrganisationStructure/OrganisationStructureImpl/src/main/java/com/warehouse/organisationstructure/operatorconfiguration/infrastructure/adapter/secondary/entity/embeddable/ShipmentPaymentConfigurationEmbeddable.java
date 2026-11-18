package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CashOnDeliveryPaymentMethod;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentPaymentConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ShipmentPaymentConfigurationEmbeddable {

    @Column(name = "shipment_allow_cash_on_delivery")
    private boolean allowCashOnDelivery;

    @Column(name = "shipment_max_cash_on_delivery_amount")
    private double maxCashOnDeliveryAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_cash_on_delivery_payment_method")
    private CashOnDeliveryPaymentMethod cashOnDeliveryPaymentMethod;

    @Column(name = "shipment_require_insurance_above_value")
    private double requireInsuranceAboveValue;

    public ShipmentPaymentConfigurationEmbeddable() {
    }

    public static ShipmentPaymentConfigurationEmbeddable from(final ShipmentPaymentConfiguration configuration) {
        final ShipmentPaymentConfiguration source = configuration != null
                ? configuration
                : ShipmentPaymentConfiguration.defaultConfiguration();
        final ShipmentPaymentConfigurationEmbeddable embeddable = new ShipmentPaymentConfigurationEmbeddable();
        embeddable.allowCashOnDelivery = source.isAllowCashOnDelivery();
        embeddable.maxCashOnDeliveryAmount = source.getMaxCashOnDeliveryAmount();
        embeddable.cashOnDeliveryPaymentMethod = source.getCashOnDeliveryPaymentMethod();
        embeddable.requireInsuranceAboveValue = source.getRequireInsuranceAboveValue();
        return embeddable;
    }

    public ShipmentPaymentConfiguration toModel() {
        return new ShipmentPaymentConfiguration(
                allowCashOnDelivery,
                maxCashOnDeliveryAmount,
                cashOnDeliveryPaymentMethod,
                requireInsuranceAboveValue
        );
    }
}
