package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class OperatorConfiguration {

    private ShippingCapabilities shippingCapabilities;

    private ShipmentConfiguration shipmentConfiguration;

    private DeliveryTimeConfiguration deliveryTimeConfiguration;

    private DepartmentConfiguration departmentConfiguration;

    private FleetConfiguration fleetConfiguration;

    private CommunicationConfiguration communicationConfiguration;

    private RegionalConfiguration regionalConfiguration;

    private SortingConfiguration sortingConfiguration;

    private PickupPointConfiguration pickupPointConfiguration;

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
        this(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration, departmentConfiguration,
                null, null, null);
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration,
                                 final DepartmentConfiguration departmentConfiguration,
                                 final FleetConfiguration fleetConfiguration,
                                 final CommunicationConfiguration communicationConfiguration,
                                 final RegionalConfiguration regionalConfiguration) {
        this(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration, departmentConfiguration,
                fleetConfiguration, communicationConfiguration, regionalConfiguration, null);
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration,
                                 final DepartmentConfiguration departmentConfiguration,
                                 final FleetConfiguration fleetConfiguration,
                                 final CommunicationConfiguration communicationConfiguration,
                                 final RegionalConfiguration regionalConfiguration,
                                 final SortingConfiguration sortingConfiguration) {
        this(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration, departmentConfiguration,
                fleetConfiguration, communicationConfiguration, regionalConfiguration, sortingConfiguration, null);
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentConfiguration shipmentConfiguration,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration,
                                 final DepartmentConfiguration departmentConfiguration,
                                 final FleetConfiguration fleetConfiguration,
                                 final CommunicationConfiguration communicationConfiguration,
                                 final RegionalConfiguration regionalConfiguration,
                                 final SortingConfiguration sortingConfiguration,
                                 final PickupPointConfiguration pickupPointConfiguration) {
        this.shippingCapabilities = shippingCapabilities;
        this.shipmentConfiguration = shipmentConfiguration;
        this.deliveryTimeConfiguration = deliveryTimeConfiguration;
        this.departmentConfiguration = departmentConfiguration;
        this.fleetConfiguration = fleetConfiguration;
        this.communicationConfiguration = communicationConfiguration;
        this.regionalConfiguration = regionalConfiguration;
        this.sortingConfiguration = sortingConfiguration;
        this.pickupPointConfiguration = pickupPointConfiguration;
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
                DepartmentConfiguration.defaultConfiguration(),
                FleetConfiguration.defaultConfiguration(),
                CommunicationConfiguration.defaultConfiguration(),
                RegionalConfiguration.defaultConfiguration(),
                SortingConfiguration.defaultConfiguration(),
                PickupPointConfiguration.defaultConfiguration()
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

    public FleetConfiguration getFleetConfiguration() {
        return fleetConfiguration;
    }

    public CommunicationConfiguration getCommunicationConfiguration() {
        return communicationConfiguration;
    }

    public RegionalConfiguration getRegionalConfiguration() {
        return regionalConfiguration;
    }

    public SortingConfiguration getSortingConfiguration() {
        return sortingConfiguration;
    }

    public PickupPointConfiguration getPickupPointConfiguration() {
        return pickupPointConfiguration;
    }

    public OperatorConfiguration withShipmentConfiguration(final ShipmentConfiguration shipmentConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withDepartmentConfiguration(final DepartmentConfiguration departmentConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withFleetConfiguration(final FleetConfiguration fleetConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withCommunicationConfiguration(final CommunicationConfiguration communicationConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withRegionalConfiguration(final RegionalConfiguration regionalConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withSortingConfiguration(final SortingConfiguration sortingConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }

    public OperatorConfiguration withPickupPointConfiguration(final PickupPointConfiguration pickupPointConfiguration) {
        return new OperatorConfiguration(shippingCapabilities, shipmentConfiguration, deliveryTimeConfiguration,
                departmentConfiguration, fleetConfiguration, communicationConfiguration, regionalConfiguration,
                sortingConfiguration, pickupPointConfiguration);
    }
}
