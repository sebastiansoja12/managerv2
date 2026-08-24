package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class OperatorConfiguration {

    private ShippingCapabilities shippingCapabilities;

    private ShipmentConfiguration shipmentConfiguration;

    private DeliveryTimeConfiguration deliveryTimeConfiguration;

    public OperatorConfiguration() {
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration) {
        this.shippingCapabilities = shippingCapabilities;
        this.shipmentConfiguration = shipmentConfiguration;
        this.deliveryTimeConfiguration = deliveryTimeConfiguration;
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentLimits shipmentLimits,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration) {
        this(shippingCapabilities, ShipmentConfiguration.defaultWith(shipmentLimits), deliveryTimeConfiguration);
    }

    public static OperatorConfiguration defaultFor(final boolean supportsInternationalShipping,
                                                   final boolean supportsCashOnDelivery,
                                                   final boolean supportsLockers) {
        return new OperatorConfiguration(
                new ShippingCapabilities(
                        true,
                        supportsInternationalShipping,
                        false,
                        false,
                        supportsCashOnDelivery,
                        supportsLockers,
                        false,
                        true,
                        false,
                        false,
                        false,
                        true,
                        false
                ),
                ShipmentConfiguration.defaultConfiguration(),
                new DeliveryTimeConfiguration()
        );
    }

    public ShippingCapabilities getShippingCapabilities() {
        return shippingCapabilities;
    }

    public ShipmentConfiguration getShipmentConfiguration() {
        return shipmentConfiguration;
    }

    public ShipmentLimits getShipmentLimits() {
        return shipmentConfiguration != null ? shipmentConfiguration.getShipmentLimits() : null;
    }

    public DeliveryTimeConfiguration getDeliveryTimeConfiguration() {
        return deliveryTimeConfiguration;
    }
}
