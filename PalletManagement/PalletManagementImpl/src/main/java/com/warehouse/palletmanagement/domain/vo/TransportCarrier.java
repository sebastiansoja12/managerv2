package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record TransportCarrier(CarrierId carrierId,
                               String name,
                               TransportType defaultTransportType,
                               boolean active,
                               List<Driver> drivers,
                               List<Vehicle> vehicles) {

    public TransportCarrier {
        drivers = drivers == null ? List.of() : List.copyOf(drivers);
        vehicles = vehicles == null ? List.of() : List.copyOf(vehicles);
    }

    public Optional<Driver> driver(final DriverId driverId) {
        return this.drivers.stream().filter(driver -> Objects.equals(driver.driverId(), driverId)).findFirst();
    }

    public Optional<Vehicle> vehicle(final VehicleId vehicleId) {
        return this.vehicles.stream().filter(vehicle -> Objects.equals(vehicle.vehicleId(), vehicleId)).findFirst();
    }

    public record Driver(DriverId driverId, String fullName, String phone, boolean active) {
    }

    public record Vehicle(VehicleId vehicleId, String registrationNumber, String trailerRegistrationNumber, boolean active) {
    }
}
