package com.warehouse.driverhub.domain.model;

import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.domain.enumeration.VehicleType;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.vo.VehicleChange;

import java.math.BigDecimal;
import java.util.Locale;

public class CarrierVehicle {

    private final VehicleId vehicleId;
    private String registrationNumber;
    private VehicleType vehicleType;
    private String trailerRegistrationNumber;
    private BigDecimal maxLoadKg;
    private boolean active;

    public CarrierVehicle(final VehicleId vehicleId, final String registrationNumber, final VehicleType vehicleType,
                          final String trailerRegistrationNumber, final BigDecimal maxLoadKg, final boolean active) {
        if (vehicleId == null || vehicleId.value() == null) {
            throw new IllegalArgumentException("Vehicle id is required");
        }
        this.vehicleId = vehicleId;
        this.registrationNumber = requiredRegistration(registrationNumber);
        this.vehicleType = vehicleType == null ? VehicleType.TRUCK : vehicleType;
        this.trailerRegistrationNumber = optionalRegistration(trailerRegistrationNumber);
        this.maxLoadKg = validMaxLoad(maxLoadKg);
        this.active = active;
    }

    public void apply(final VehicleChange change) {
        switch (change) {
            case VehicleChange.RegistrationNumber registrationChange ->
                    this.registrationNumber = requiredRegistration(registrationChange.value());
            case VehicleChange.Type typeChange -> {
                if (typeChange.value() == null) {
                    throw DriverHubException.invalid("Vehicle type is required");
                }
                this.vehicleType = typeChange.value();
            }
            case VehicleChange.TrailerRegistrationNumber trailerChange ->
                    this.trailerRegistrationNumber = optionalRegistration(trailerChange.value());
            case VehicleChange.MaxLoad maxLoadChange -> this.maxLoadKg = validMaxLoad(maxLoadChange.value());
            case VehicleChange.Active activeChange -> this.active = activeChange.value();
        }
    }

    static String normalizeRegistration(final String value) {
        return value.trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private static String requiredRegistration(final String value) {
        if (Text.blank(value)) {
            throw DriverHubException.invalid("Vehicle registration number is required");
        }
        return normalizeRegistration(value);
    }

    private static String optionalRegistration(final String value) {
        return Text.blank(value) ? null : normalizeRegistration(value);
    }

    private static BigDecimal validMaxLoad(final BigDecimal value) {
        if (value != null && value.signum() <= 0) {
            throw DriverHubException.invalid("Vehicle max load must be positive");
        }
        return value;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getTrailerRegistrationNumber() {
        return trailerRegistrationNumber;
    }

    public BigDecimal getMaxLoadKg() {
        return maxLoadKg;
    }

    public boolean isActive() {
        return active;
    }
}
