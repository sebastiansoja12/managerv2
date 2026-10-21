package com.warehouse.driverhub.application.port.primary.command;

public record DriverCommand(String firstName, String lastName, String phone, String licenseNumber) {
}
