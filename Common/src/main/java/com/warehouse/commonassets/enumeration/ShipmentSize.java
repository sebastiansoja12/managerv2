package com.warehouse.commonassets.enumeration;

public enum ShipmentSize {

    TINY("5cmx5cmx5cm"),
    SMALL("10cmx10cmx10cm"),
    MEDIUM("30cmx30cmx30cm"),
    AVERAGE("50cmx50cmx50cm"),
    BIG("80cmx80cmx80cm"),
    CUSTOM("XcmXcmXcm"),
    TEST("test");

    private final String shipmentSize;

    ShipmentSize(final String shipmentSize) {
        this.shipmentSize = shipmentSize;
    }

    public String getShipmentSize() {
        return shipmentSize;
    }
}
