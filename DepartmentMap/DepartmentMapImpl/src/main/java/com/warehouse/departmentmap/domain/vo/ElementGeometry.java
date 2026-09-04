package com.warehouse.departmentmap.domain.vo;

import com.warehouse.departmentmap.domain.exception.DepartmentMapException;

public record ElementGeometry(double x, double y, double width, double height, double rotation) {

    private static final double MIN_SIZE = 0.05;

    public ElementGeometry {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(width) || !Double.isFinite(height)
                || !Double.isFinite(rotation)) {
            throw DepartmentMapException.invalid("Element geometry must contain finite numbers");
        }
        if (width < MIN_SIZE || height < MIN_SIZE) {
            throw DepartmentMapException.invalid("Element width and height must be at least " + MIN_SIZE + " m");
        }
        rotation = ((rotation % 360) + 360) % 360;
    }

    public double centerX() {
        return x + width / 2;
    }

    public double centerY() {
        return y + height / 2;
    }

    public boolean centerWithin(final double mapWidth, final double mapHeight) {
        return centerX() >= 0 && centerX() <= mapWidth && centerY() >= 0 && centerY() <= mapHeight;
    }
}
