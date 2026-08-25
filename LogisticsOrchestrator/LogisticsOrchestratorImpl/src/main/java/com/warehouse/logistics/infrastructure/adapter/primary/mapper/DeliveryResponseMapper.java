package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryStep;
import com.warehouse.logistics.infrastructure.adapter.primary.dto.DeliveryResponseDto;
import com.warehouse.logistics.infrastructure.adapter.primary.dto.DeliveryStepResponseDto;

import java.util.List;

public class DeliveryResponseMapper {

    public List<DeliveryResponseDto> map(final List<Delivery> deliveries) {
        return deliveries.stream().map(this::map).toList();
    }

    public DeliveryResponseDto map(final Delivery delivery) {
        return new DeliveryResponseDto(delivery.getDeliveryId().getId(), delivery.getTarget().type().name(),
                delivery.getTarget().id(), delivery.getShipmentId() == null ? null
                : String.valueOf(delivery.getShipmentId().getValue()), delivery.getType().name(),
                delivery.getDeliveryStatus() == null ? null : delivery.getDeliveryStatus().name(),
                delivery.getStatus().name(), delivery.getMethod() == null ? null : delivery.getMethod().name(),
                delivery.getCreatedAt(), delivery.getPlannedAt(), delivery.getDeliveredAt(),
                delivery.getDeliverySteps().stream().map(this::map).toList());
    }

    public DeliveryStepResponseDto map(final DeliveryStep step) {
        return new DeliveryStepResponseDto(step.stepNumber(), step.attemptedAt(), step.outcome().name(),
                step.deliveryStatus() == null ? null : step.deliveryStatus().name(),
                step.userId() == null ? null : String.valueOf(step.userId().value()),
                step.departmentId() == null ? null : String.valueOf(step.departmentId().getValue()),
                step.supplierId() == null ? null : String.valueOf(step.supplierId().value()),
                step.vehicleId() == null ? null : String.valueOf(step.vehicleId().value()),
                step.method() == null ? null : step.method().name(),
                step.comment(), step.failureReason());
    }
}
