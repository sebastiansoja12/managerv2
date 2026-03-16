package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.UserId;

public record SoftwareConfigurationApiKey(UserId userId, String value) {
}
