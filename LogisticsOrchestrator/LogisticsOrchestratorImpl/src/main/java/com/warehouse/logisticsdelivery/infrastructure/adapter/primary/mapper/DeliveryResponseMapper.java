package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryStepSnapshot;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryHistoryEntryResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryRelationResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepResponseDto;

import java.util.List;

public class DeliveryResponseMapper {

    private final int attemptLimit;

    public DeliveryResponseMapper(final int attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public List<DeliveryResponseDto> map(final List<DeliverySnapshot> deliveries) {
        return deliveries.stream().map(this::map).toList();
    }

    public DeliveryResponseDto map(final DeliverySnapshot delivery) {
        return new DeliveryResponseDto(delivery.deliveryId().getId(), delivery.target().type().name(),
                delivery.target().id(), delivery.shipmentId() == null ? null
                : String.valueOf(delivery.shipmentId().getValue()), delivery.type().name(),
                delivery.deliveryStatus() == null ? null : delivery.deliveryStatus().name(),
                delivery.status().name(),
                delivery.originDepartmentId() == null ? null
                        : String.valueOf(delivery.originDepartmentId().getValue()),
                delivery.destinationDepartmentId() == null ? null
                        : String.valueOf(delivery.destinationDepartmentId().getValue()),
                delivery.pickupMethod() == null ? null : delivery.pickupMethod().name(),
                delivery.method() == null ? null : delivery.method().name(),
                delivery.pickupPointId() == null ? null : String.valueOf(delivery.pickupPointId().value()),
                delivery.deliveryPickupPointId() == null ? null
                        : String.valueOf(delivery.deliveryPickupPointId().value()),
                delivery.supplierId() == null ? null : String.valueOf(delivery.supplierId().value()),
                delivery.vehicleId() == null ? null : String.valueOf(delivery.vehicleId().value()),
                delivery.signatureRequired(), delivery.instructions(),
                delivery.createdAt(), delivery.plannedAt(), delivery.deliveredAt(),
                attemptLimit, delivery.deliverySteps().stream().map(this::map).toList());
    }

    public List<DeliveryRelationResponseDto> mapRelations(final List<DeliveryRelation> relations) {
        return relations.stream()
                .map(relation -> new DeliveryRelationResponseDto(relation.relation().name(), map(relation.delivery())))
                .toList();
    }

    public List<DeliveryHistoryEntryResponseDto> mapHistory(final List<DeliveryHistoryEntry> history) {
        return history.stream().map(this::map).toList();
    }

    public DeliveryHistoryEntryResponseDto map(final DeliveryHistoryEntry entry) {
        return new DeliveryHistoryEntryResponseDto(entry.eventType().name(), entry.occurredAt(),
                entry.userId() == null ? null : String.valueOf(entry.userId().value()),
                entry.deliveryStatus() == null ? null : entry.deliveryStatus().name(),
                entry.status().name(),
                entry.originDepartmentId() == null ? null : String.valueOf(entry.originDepartmentId().getValue()),
                entry.destinationDepartmentId() == null ? null
                        : String.valueOf(entry.destinationDepartmentId().getValue()),
                entry.pickupMethod() == null ? null : entry.pickupMethod().name(),
                entry.method() == null ? null : entry.method().name(),
                entry.pickupPointId() == null ? null : String.valueOf(entry.pickupPointId().value()),
                entry.deliveryPickupPointId() == null ? null : String.valueOf(entry.deliveryPickupPointId().value()),
                entry.supplierId() == null ? null : String.valueOf(entry.supplierId().value()),
                entry.vehicleId() == null ? null : String.valueOf(entry.vehicleId().value()),
                entry.signatureRequired(), entry.instructions(), entry.plannedAt(), entry.stepCount());
    }

    public DeliveryStepResponseDto map(final DeliveryStepSnapshot step) {
        return new DeliveryStepResponseDto(step.stepNumber(), step.attemptedAt(), step.outcome().name(),
                step.deliveryStatus() == null ? null : step.deliveryStatus().name(),
                step.userId() == null ? null : String.valueOf(step.userId().value()),
                step.departmentId() == null ? null : String.valueOf(step.departmentId().getValue()),
                step.supplierId() == null ? null : String.valueOf(step.supplierId().value()),
                step.vehicleId() == null ? null : String.valueOf(step.vehicleId().value()),
                step.signatureId() == null ? null : String.valueOf(step.signatureId().getValue()),
                step.comment(), step.failureReason());
    }
}
