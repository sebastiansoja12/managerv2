package com.warehouse.supplierconfiguration.application.port.primary.command;

public record ResetSupplierConfigurationCommand(SupplierConfigurationTarget target, Long expectedVersion) {
}
