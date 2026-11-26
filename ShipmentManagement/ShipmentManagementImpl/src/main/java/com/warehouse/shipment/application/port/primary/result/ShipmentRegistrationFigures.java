package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;

public record ShipmentRegistrationFigures(DepartmentId originDepartmentId,
                                          Currency currency,
                                          long registeredShipments,
                                          long canceledShipments,
                                          BigDecimal registeredValue) {
}
