package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto;

import java.util.Map;

public record SupplierConfigurationPolicyUpdateApiRequest(Long version, Map<String, Boolean> enforcedSettings) {
}
