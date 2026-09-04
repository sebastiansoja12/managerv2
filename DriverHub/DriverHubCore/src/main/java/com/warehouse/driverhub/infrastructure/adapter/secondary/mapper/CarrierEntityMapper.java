package com.warehouse.driverhub.infrastructure.adapter.secondary.mapper;

import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.model.CarrierDriver;
import com.warehouse.driverhub.domain.model.CarrierVehicle;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierDriverEntity;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierEntity;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierVehicleEntity;

public final class CarrierEntityMapper {

    private CarrierEntityMapper() {
    }

    public static CarrierEntity toEntity(final Carrier carrier) {
        final CarrierEntity entity = new CarrierEntity(
                carrier.getCarrierId(),
                carrier.getName(),
                carrier.getTaxId(),
                carrier.getPhone(),
                carrier.getEmail(),
                carrier.getDefaultTransportType(),
                carrier.isActive(),
                carrier.getCreated(),
                carrier.getDrivers().stream()
                        .map(driver -> new CarrierDriverEntity(driver.getDriverId(), carrier.getCarrierId(),
                                driver.getFirstName(), driver.getLastName(), driver.getPhone(),
                                driver.getLicenseNumber(), driver.isActive()))
                        .toList(),
                carrier.getVehicles().stream()
                        .map(vehicle -> new CarrierVehicleEntity(vehicle.getVehicleId(), carrier.getCarrierId(),
                                vehicle.getRegistrationNumber(), vehicle.getVehicleType(),
                                vehicle.getTrailerRegistrationNumber(), vehicle.getMaxLoadKg(), vehicle.isActive()))
                        .toList());
        entity.assignOperator(carrier.getOperatorId());
        return entity;
    }

    public static Carrier toModel(final CarrierEntity entity) {
        return new Carrier(entity.getCarrierId(), entity.operatorId(), entity.getName(), entity.getTaxId(),
                entity.getPhone(), entity.getEmail(), entity.getDefaultTransportType(), entity.isActive(),
                entity.getCreatedAt(),
                entity.getDrivers().stream()
                        .map(driver -> new CarrierDriver(driver.getDriverId(), driver.getFirstName(),
                                driver.getLastName(), driver.getPhone(), driver.getLicenseNumber(), driver.isActive()))
                        .toList(),
                entity.getVehicles().stream()
                        .map(vehicle -> new CarrierVehicle(vehicle.getVehicleId(), vehicle.getRegistrationNumber(),
                                vehicle.getVehicleType(), vehicle.getTrailerRegistrationNumber(),
                                vehicle.getMaxLoadKg(), vehicle.isActive()))
                        .toList());
    }

    public static Carrier toActiveModel(final CarrierEntity entity) {
        return new Carrier(entity.getCarrierId(), entity.operatorId(), entity.getName(), entity.getTaxId(),
                entity.getPhone(), entity.getEmail(), entity.getDefaultTransportType(), entity.isActive(),
                entity.getCreatedAt(),
                entity.getDrivers().stream()
                        .filter(CarrierDriverEntity::isActive)
                        .map(driver -> new CarrierDriver(driver.getDriverId(), driver.getFirstName(),
                                driver.getLastName(), driver.getPhone(), driver.getLicenseNumber(), true))
                        .toList(),
                entity.getVehicles().stream()
                        .filter(CarrierVehicleEntity::isActive)
                        .map(vehicle -> new CarrierVehicle(vehicle.getVehicleId(), vehicle.getRegistrationNumber(),
                                vehicle.getVehicleType(), vehicle.getTrailerRegistrationNumber(),
                                vehicle.getMaxLoadKg(), true))
                        .toList());
    }
}
