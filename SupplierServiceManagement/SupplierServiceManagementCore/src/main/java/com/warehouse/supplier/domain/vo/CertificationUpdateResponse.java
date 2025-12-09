package com.warehouse.supplier.domain.vo;

public record CertificationUpdateResponse(Status status, String message) {

    public static CertificationUpdateResponse ok() {
        return new CertificationUpdateResponse(Status.OK, "Certification updated successfully.");
    }

    public static CertificationUpdateResponse failure(final String message) {
        return new CertificationUpdateResponse(Status.FAILURE, message);
    }

    public enum Status {
        OK, FAILURE
    }
}
