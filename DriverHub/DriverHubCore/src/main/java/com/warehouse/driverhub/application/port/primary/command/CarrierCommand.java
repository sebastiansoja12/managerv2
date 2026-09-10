package com.warehouse.driverhub.application.port.primary.command;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.driverhub.domain.enumeration.TransportType;

public record CarrierCommand(String name, TaxId taxId, String phone, String email,
                             TransportType defaultTransportType) {
}
