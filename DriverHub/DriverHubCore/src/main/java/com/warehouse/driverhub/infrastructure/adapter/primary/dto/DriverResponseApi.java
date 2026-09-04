package com.warehouse.driverhub.infrastructure.adapter.primary.dto;

public record DriverResponseApi(Long driverId,
                                Long carrierId,
                                String carrierName,
                                boolean carrierActive,
                                String firstName,
                                String lastName,
                                String phone,
                                String licenseNumber,
                                boolean active) {
}
