package com.warehouse.driverhub.api.dto;

import java.time.Instant;
import java.util.List;

public record CarrierDto(Long carrierId,
                         String name,
                         String taxId,
                         String phone,
                         String email,
                         String defaultTransportType,
                         boolean active,
                         Instant created,
                         List<CarrierDriverDto> drivers,
                         List<CarrierVehicleDto> vehicles) {

    public CarrierDto {
        drivers = drivers == null ? List.of() : List.copyOf(drivers);
        vehicles = vehicles == null ? List.of() : List.copyOf(vehicles);
    }
}
