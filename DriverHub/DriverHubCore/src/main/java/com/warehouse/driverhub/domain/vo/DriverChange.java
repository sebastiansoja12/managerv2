package com.warehouse.driverhub.domain.vo;

public sealed interface DriverChange {

    record FirstName(String value) implements DriverChange {
    }

    record LastName(String value) implements DriverChange {
    }

    record Phone(String value) implements DriverChange {
    }

    record LicenseNumber(String value) implements DriverChange {
    }

    record Active(boolean value) implements DriverChange {
    }
}
