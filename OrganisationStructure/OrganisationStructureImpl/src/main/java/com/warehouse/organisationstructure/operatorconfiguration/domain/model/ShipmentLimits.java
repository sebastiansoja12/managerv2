package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentLimits {
    private double maxWeight;
    private double minWeight;
    private double maxLength;
    private double maxWidth;
    private double maxHeight;
    private double maxShipmentValue;
    private boolean allowOversized;

    public ShipmentLimits() {
    }

    public ShipmentLimits(final double maxWeight,
                          final double minWeight,
                          final double maxLength,
                          final double maxWidth,
                          final double maxHeight,
                          final double maxShipmentValue) {
        this(maxWeight, minWeight, maxLength, maxWidth, maxHeight, maxShipmentValue, false);
    }

    public ShipmentLimits(final double maxWeight,
                          final double minWeight,
                          final double maxLength,
                          final double maxWidth,
                          final double maxHeight,
                          final double maxShipmentValue,
                          final boolean allowOversized) {
        this.maxWeight = maxWeight;
        this.minWeight = minWeight;
        this.maxLength = maxLength;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxShipmentValue = maxShipmentValue;
        this.allowOversized = allowOversized;
    }

    public double getMaxWeight() { return maxWeight; }
    public double getMinWeight() { return minWeight; }
    public double getMaxLength() { return maxLength; }
    public double getMaxWidth() { return maxWidth; }
    public double getMaxHeight() { return maxHeight; }
    public double getMaxShipmentValue() { return maxShipmentValue; }
    public boolean isAllowOversized() { return allowOversized; }
}
