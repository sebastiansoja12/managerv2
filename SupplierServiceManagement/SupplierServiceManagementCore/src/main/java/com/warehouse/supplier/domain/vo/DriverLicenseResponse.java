package com.warehouse.supplier.domain.vo;

public record DriverLicenseResponse(Status status, String message) {
    public static DriverLicenseResponse failure(final String failure) {
        return new DriverLicenseResponse(Status.ERROR, failure);
    }

    public static DriverLicenseResponse ok() {
        return new DriverLicenseResponse(Status.OK, "Driver license updated successfully.");
    }

    public enum Status {
        OK, ERROR
    }
}
