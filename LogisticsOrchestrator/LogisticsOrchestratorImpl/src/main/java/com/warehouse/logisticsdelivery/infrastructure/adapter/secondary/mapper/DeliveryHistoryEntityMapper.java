package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryHistoryEntity;

public class DeliveryHistoryEntityMapper {

    public DeliveryHistoryEntity toEntity(final DeliveryHistoryEntry entry) {
        return DeliveryHistoryEntity.builder()
                .deliveryId(entry.deliveryId().getId())
                .eventType(entry.eventType())
                .occurredAt(entry.occurredAt())
                .userId(entry.userId() == null ? null : entry.userId().value())
                .deliveryStatus(entry.deliveryStatus())
                .status(entry.status())
                .originDepartmentId(entry.originDepartmentId() == null ? null : entry.originDepartmentId().getValue())
                .destinationDepartmentId(entry.destinationDepartmentId() == null ? null
                        : entry.destinationDepartmentId().getValue())
                .pickupMethod(entry.pickupMethod())
                .method(entry.method())
                .pickupPointId(entry.pickupPointId() == null ? null : entry.pickupPointId().value())
                .deliveryPickupPointId(entry.deliveryPickupPointId() == null ? null
                        : entry.deliveryPickupPointId().value())
                .supplierId(entry.supplierId() == null ? null : entry.supplierId().value())
                .vehicleId(entry.vehicleId() == null ? null : entry.vehicleId().value())
                .signatureRequired(entry.signatureRequired())
                .instructions(entry.instructions())
                .plannedAt(entry.plannedAt())
                .stepCount(entry.stepCount())
                .build();
    }

    public DeliveryHistoryEntry toEntry(final DeliveryHistoryEntity entity) {
        return new DeliveryHistoryEntry(new DeliveryId(entity.getDeliveryId()), entity.getEventType(),
                entity.getOccurredAt(), entity.getUserId() == null ? null : new UserId(entity.getUserId()),
                entity.getDeliveryStatus(), entity.getStatus(),
                entity.getOriginDepartmentId() == null ? null : new DepartmentId(entity.getOriginDepartmentId()),
                entity.getDestinationDepartmentId() == null ? null
                        : new DepartmentId(entity.getDestinationDepartmentId()),
                entity.getPickupMethod(), entity.getMethod(),
                entity.getPickupPointId() == null ? null : new PickupPointId(entity.getPickupPointId()),
                entity.getDeliveryPickupPointId() == null ? null : new PickupPointId(entity.getDeliveryPickupPointId()),
                entity.getSupplierId() == null ? null : new SupplierId(entity.getSupplierId()),
                entity.getVehicleId() == null ? null : new VehicleId(entity.getVehicleId()),
                entity.getSignatureRequired(), entity.getInstructions(), entity.getPlannedAt(),
                entity.getStepCount() == null ? 0 : entity.getStepCount());
    }
}
