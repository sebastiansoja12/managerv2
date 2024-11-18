package com.warehouse.pallet.domain.vo;

public class Dimension {
    private final double length;
    private final double width;
    private final double height;

    public Dimension(final double length, final double width, final double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
