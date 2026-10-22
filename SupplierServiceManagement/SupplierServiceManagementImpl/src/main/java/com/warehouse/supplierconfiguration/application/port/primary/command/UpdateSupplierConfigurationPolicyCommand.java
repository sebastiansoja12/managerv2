package com.warehouse.supplierconfiguration.application.port.primary.command;

import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;

import java.util.Map;

public record UpdateSupplierConfigurationPolicyCommand(Long expectedVersion,
                                                       Map<SupplierSetting, Boolean> enforcedSettings) {
}
