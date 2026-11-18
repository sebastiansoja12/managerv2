package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;

public record ShipmentDeliveryFiguresDto(DepartmentId originDepartmentId,
                                         Currency currency,
                                         long deliveredShipments,
                                         BigDecimal deliveredValue) {
}
