package com.warehouse.commonassets.enumeration;

public enum ShipmentSize {

    TINY("5cmx5cmx5cm", 10),
    SMALL("10cmx10cmx10cm", 15),
    MEDIUM("30cmx30cmx30cm", 20),
    AVERAGE("50cmx50cmx50cm", 30),
    BIG("80cmx80cmx80cm", 50),
    CUSTOM("XcmXcmXcm", 70),
    TEST("test", 99);

    private final String shipmentSize;
    public final double price;

    ShipmentSize(final String shipmentSize, final double price) {
        this.shipmentSize = shipmentSize;
        this.price = price;
    }

    public String getShipmentSize() {
        return shipmentSize;
    }

    public double getPrice() { return price; }

}
