package com.warehouse.mcp.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record DeliveryDetails(DeliveryId deliveryId,
                              ShipmentId shipmentId,
                              String type,
                              String deliveryStatus,
                              String lifecycleStatus,
                              DepartmentId originDepartmentId,
                              DepartmentId destinationDepartmentId,
                              String pickupMethod,
                              String method,
                              String pickupPointId,
                              String deliveryPickupPointId,
                              SupplierId supplierId,
                              VehicleId vehicleId,
                              Boolean signatureRequired,
                              String instructions,
                              LocalDateTime createdAt,
                              LocalDateTime plannedAt,
                              LocalDateTime deliveredAt,
                              List<DeliveryStep> steps) {
}
