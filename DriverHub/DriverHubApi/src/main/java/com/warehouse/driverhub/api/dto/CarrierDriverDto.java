package com.warehouse.driverhub.api.dto;

public record CarrierDriverDto(Long driverId,
                               String firstName,
                               String lastName,
                               String phone,
                               String licenseNumber,
                               boolean active) {

    public String fullName() {
        return ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
    }
}
