package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class OperatorConfiguration {

    private ShippingCapabilities shippingCapabilities;

    private ShipmentLimits shipmentLimits;

    private DeliveryTimeConfiguration deliveryTimeConfiguration;

    public OperatorConfiguration() {
    }

    public OperatorConfiguration(final ShippingCapabilities shippingCapabilities,
                                 final ShipmentLimits shipmentLimits,
                                 final DeliveryTimeConfiguration deliveryTimeConfiguration) {
        this.shippingCapabilities = shippingCapabilities;
        this.shipmentLimits = shipmentLimits;
        this.deliveryTimeConfiguration = deliveryTimeConfiguration;
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
                new ShipmentLimits(),
                new DeliveryTimeConfiguration()
        );
    }

    public ShippingCapabilities getShippingCapabilities() {
        return shippingCapabilities;
    }

    public ShipmentLimits getShipmentLimits() {
        return shipmentLimits;
    }

    public DeliveryTimeConfiguration getDeliveryTimeConfiguration() {
        return deliveryTimeConfiguration;
    }

    public static class ShippingCapabilities {
        private boolean supportsDomesticShipping;
        private boolean supportsInternationalShipping;
        private boolean supportsExpressShipping;
        private boolean supportsSameDayDelivery;
        private boolean supportsCashOnDelivery;
        private boolean supportsParcelLockers;
        private boolean supportsPickupPoints;
        private boolean supportsHomeDelivery;
        private boolean supportsSaturdayDelivery;
        private boolean supportsSundayDelivery;
        private boolean supportsReturnShipments;
        private boolean providesTracking;
        private boolean providesInsurance;

        public ShippingCapabilities() {
        }

        public ShippingCapabilities(final boolean supportsDomesticShipping,
                                    final boolean supportsInternationalShipping,
                                    final boolean supportsExpressShipping,
                                    final boolean supportsSameDayDelivery,
                                    final boolean supportsCashOnDelivery,
                                    final boolean supportsParcelLockers,
                                    final boolean supportsPickupPoints,
                                    final boolean supportsHomeDelivery,
                                    final boolean supportsSaturdayDelivery,
                                    final boolean supportsSundayDelivery,
                                    final boolean supportsReturnShipments,
                                    final boolean providesTracking,
                                    final boolean providesInsurance) {
            this.supportsDomesticShipping = supportsDomesticShipping;
            this.supportsInternationalShipping = supportsInternationalShipping;
            this.supportsExpressShipping = supportsExpressShipping;
            this.supportsSameDayDelivery = supportsSameDayDelivery;
            this.supportsCashOnDelivery = supportsCashOnDelivery;
            this.supportsParcelLockers = supportsParcelLockers;
            this.supportsPickupPoints = supportsPickupPoints;
            this.supportsHomeDelivery = supportsHomeDelivery;
            this.supportsSaturdayDelivery = supportsSaturdayDelivery;
            this.supportsSundayDelivery = supportsSundayDelivery;
            this.supportsReturnShipments = supportsReturnShipments;
            this.providesTracking = providesTracking;
            this.providesInsurance = providesInsurance;
        }

        public boolean isSupportsDomesticShipping() { return supportsDomesticShipping; }
        public boolean isSupportsInternationalShipping() { return supportsInternationalShipping; }
        public boolean isSupportsExpressShipping() { return supportsExpressShipping; }
        public boolean isSupportsSameDayDelivery() { return supportsSameDayDelivery; }
        public boolean isSupportsCashOnDelivery() { return supportsCashOnDelivery; }
        public boolean isSupportsParcelLockers() { return supportsParcelLockers; }
        public boolean isSupportsPickupPoints() { return supportsPickupPoints; }
        public boolean isSupportsHomeDelivery() { return supportsHomeDelivery; }
        public boolean isSupportsSaturdayDelivery() { return supportsSaturdayDelivery; }
        public boolean isSupportsSundayDelivery() { return supportsSundayDelivery; }
        public boolean isSupportsReturnShipments() { return supportsReturnShipments; }
        public boolean isProvidesTracking() { return providesTracking; }
        public boolean isProvidesInsurance() { return providesInsurance; }
    }

    public static class ShipmentLimits {
        private double maxWeight;
        private double minWeight;
        private double maxLength;
        private double maxWidth;
        private double maxHeight;
        private double maxShipmentValue;

        public ShipmentLimits() {
        }

        public ShipmentLimits(final double maxWeight,
                              final double minWeight,
                              final double maxLength,
                              final double maxWidth,
                              final double maxHeight,
                              final double maxShipmentValue) {
            this.maxWeight = maxWeight;
            this.minWeight = minWeight;
            this.maxLength = maxLength;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.maxShipmentValue = maxShipmentValue;
        }

        public double getMaxWeight() { return maxWeight; }
        public double getMinWeight() { return minWeight; }
        public double getMaxLength() { return maxLength; }
        public double getMaxWidth() { return maxWidth; }
        public double getMaxHeight() { return maxHeight; }
        public double getMaxShipmentValue() { return maxShipmentValue; }
    }

    public static class DeliveryTimeConfiguration {
        private int minDeliveryDays;
        private int maxDeliveryDays;
        private int expressDeliveryDays;
        private int sameDayDeliveryHours;
        private int internationalDeliveryDays;

        public DeliveryTimeConfiguration() {
        }

        public DeliveryTimeConfiguration(final int minDeliveryDays,
                                         final int maxDeliveryDays,
                                         final int expressDeliveryDays,
                                         final int sameDayDeliveryHours,
                                         final int internationalDeliveryDays) {
            this.minDeliveryDays = minDeliveryDays;
            this.maxDeliveryDays = maxDeliveryDays;
            this.expressDeliveryDays = expressDeliveryDays;
            this.sameDayDeliveryHours = sameDayDeliveryHours;
            this.internationalDeliveryDays = internationalDeliveryDays;
        }

        public int getMinDeliveryDays() { return minDeliveryDays; }
        public int getMaxDeliveryDays() { return maxDeliveryDays; }
        public int getExpressDeliveryDays() { return expressDeliveryDays; }
        public int getSameDayDeliveryHours() { return sameDayDeliveryHours; }
        public int getInternationalDeliveryDays() { return internationalDeliveryDays; }
    }
}
