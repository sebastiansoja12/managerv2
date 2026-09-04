package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.math.BigDecimal;

public record PalletShipment(ShipmentId shipmentId, ShipmentStatus status, BigDecimal weightInKilograms) {
}
