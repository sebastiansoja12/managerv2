package com.warehouse.driverhub.domain.model;

import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.vo.DriverChange;

public class CarrierDriver {

    private final DriverId driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String licenseNumber;
    private boolean active;

    public CarrierDriver(final DriverId driverId, final String firstName, final String lastName, final String phone,
                         final String licenseNumber, final boolean active) {
        if (driverId == null || driverId.getValue() == null) {
            throw new IllegalArgumentException("Driver id is required");
        }
        this.driverId = driverId;
        this.firstName = requiredName(firstName, "Driver first name is required");
        this.lastName = requiredName(lastName, "Driver last name is required");
        this.phone = Text.trim(phone);
        this.licenseNumber = Text.trim(licenseNumber);
        this.active = active;
    }

    public void apply(final DriverChange change) {
        switch (change) {
            case DriverChange.FirstName firstNameChange ->
                    this.firstName = requiredName(firstNameChange.value(), "Driver first name is required");
            case DriverChange.LastName lastNameChange ->
                    this.lastName = requiredName(lastNameChange.value(), "Driver last name is required");
            case DriverChange.Phone phoneChange -> this.phone = Text.trim(phoneChange.value());
            case DriverChange.LicenseNumber licenseChange -> this.licenseNumber = Text.trim(licenseChange.value());
            case DriverChange.Active activeChange -> this.active = activeChange.value();
        }
    }

    private static String requiredName(final String value, final String message) {
        if (Text.blank(value)) {
            throw DriverHubException.invalid(message);
        }
        return value.trim();
    }

    public DriverId getDriverId() {
        return driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isActive() {
        return active;
    }
}
