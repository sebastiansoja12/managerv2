package com.warehouse.platformadministration.application.port.primary.command;

public record ChangeMaintenanceModeCommand(boolean enabled, String message) {
}
