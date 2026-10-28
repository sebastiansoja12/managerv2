package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class OperatorConfiguration {

    private ShippingCapabilities shippingCapabilities;

    private ShipmentConfiguration shipmentConfiguration;

    private DeliveryTimeConfiguration deliveryTimeConfiguration;

    private DepartmentConfiguration departmentConfiguration;

    public OperatorConfiguration() {
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration) {
        this(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration, null);
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration,
                                 final DepartmentConfiguration departmentConfiguration) {
        this.shippingCapabilities = shippingCapabilities;
        this.shipmentConfiguration = shipmentConfiguration;
        this.deliveryTimeConfiguration = deliveryTimeConfiguration;
        this.departmentConfiguration = departmentConfiguration;
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
                new DeliveryTimeConfiguration(),
                DepartmentConfiguration.defaultConfiguration()
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

    public DepartmentConfiguration getDepartmentConfiguration() {
        return departmentConfiguration;
    }

    public OperatorConfiguration withDepartmentConfiguration(final DepartmentConfiguration departmentConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration);
    }
}
