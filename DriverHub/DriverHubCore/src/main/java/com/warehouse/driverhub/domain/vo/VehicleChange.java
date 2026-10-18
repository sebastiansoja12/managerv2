package com.warehouse.driverhub.domain.vo;

import com.warehouse.driverhub.domain.enumeration.VehicleType;

import java.math.BigDecimal;

public sealed interface VehicleChange {

    record RegistrationNumber(String value) implements VehicleChange {
    }

    record Type(VehicleType value) implements VehicleChange {
    }

    record TrailerRegistrationNumber(String value) implements VehicleChange {
    }

    record MaxLoad(BigDecimal value) implements VehicleChange {
    }

    record Active(boolean value) implements VehicleChange {
    }
}
