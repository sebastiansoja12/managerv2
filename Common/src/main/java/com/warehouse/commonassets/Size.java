package com.warehouse.commonassets;


import lombok.Getter;

@Getter
public enum Size {
    TINY("5cmx5cmx5cm", 10),
    SMALL("10cmx10cmx10cm", 15),
    MEDIUM("30cmx30cmx30cm", 20),
    AVERAGE("50cmx50cmx50cm", 30),
    BIG("80cmx80cmx80cm", 50),
    CUSTOM("XcmXcmXcm", 70),
    TEST("test", 99);

    private final String parcelSize;

    private final double price;

    Size(String size, double price) {
        this.parcelSize = size;
        this.price = price;
    }
}
