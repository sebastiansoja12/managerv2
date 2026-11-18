package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;

public record ShipmentDeliveryFigures(DepartmentId originDepartmentId,
                                      Currency currency,
                                      long deliveredShipments,
                                      BigDecimal deliveredValue) {
}
