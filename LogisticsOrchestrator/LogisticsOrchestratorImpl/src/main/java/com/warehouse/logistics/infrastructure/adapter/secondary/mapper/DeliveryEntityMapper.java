package com.warehouse.logistics.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.logistics.domain.enumeration.DeliverySaveStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryStep;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryStepEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.enumeration.Status;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeliveryEntityMapper {

    default Delivery toDomain(final DeliveryEntity entity) {
        final DeliveryTarget target = new DeliveryTarget(entity.getTargetType(), entity.getTargetId());
        return new Delivery(entity.getId(), target, entity.getShipmentId(),
                entity.getType(), entity.getDeliverySteps().stream().map(this::toDomain).toList(),
                entity.getCreated(), entity.getSupplierId(), entity.getVehicleId(),
                entity.getDeliveryStatus() == null ? null
                        : com.warehouse.logistics.domain.enumeration.DeliveryStatus.valueOf(entity.getDeliveryStatus().name()),
                entity.getStatus(), entity.getMethod(), entity.getPickupPointId(), entity.getDeliveryPickupPointId(),
                Boolean.TRUE.equals(entity.getSignatureRequired()), entity.getInstructions(), entity.getPlannedAt(),
                entity.getDeliveredAt(), entity.getToken());
    }

    default DeliveryStep toDomain(final DeliveryStepEntity entity) {
        return new DeliveryStep(entity.getId(), entity.getDeliveryId(), entity.getAttemptedAt(),
                entity.getOutcome(), entity.getDeliveryStatus(), entity.getSignatureId(), entity.getUserId(), entity.getSupplierId(),
                entity.getDepartmentId(), entity.getVehicleId(), entity.getMethod(),
                entity.getComment(), entity.getFailureReason(), entity.getToken(), entity.getStepNumber() - 1);
    }

    default DeliveryEntity toEntity(final Delivery delivery) {
        final List<DeliveryStepEntity> steps = delivery.getDeliverySteps() == null ? List.of()
                : delivery.getDeliverySteps().stream().map(this::toEntity).toList();
        return DeliveryEntity.builder()
                .id(delivery.getDeliveryId())
                .shipmentId(delivery.getShipmentId())
                .targetType(delivery.getTarget().type())
                .targetId(delivery.getTarget().id())
                .type(delivery.getType())
                .supplierId(delivery.getSupplierId())
                .vehicleId(delivery.getVehicleId())
                .created(delivery.getCreatedAt())
                .deliveryStatus(delivery.getDeliveryStatus() == null ? null
                        : Status.valueOf(delivery.getDeliveryStatus().name()))
                .status(delivery.getStatus() == null ? DeliveryLifecycleStatus.CREATED : delivery.getStatus())
                .method(delivery.getMethod())
                .pickupPointId(delivery.getPickupPointId())
                .deliveryPickupPointId(delivery.getDeliveryPickupPointId())
                .signatureRequired(Boolean.TRUE.equals(delivery.getSignatureRequired()))
                .instructions(delivery.getInstructions())
                .plannedAt(delivery.getPlannedAt())
                .deliveredAt(delivery.getDeliveredAt())
                .token(delivery.getToken())
                .deliverySteps(steps)
                .build();
    }

    default DeliveryStepEntity toEntity(final DeliveryStep step) {
        return DeliveryStepEntity.builder()
                .id(step.id())
                .deliveryId(step.deliveryId())
                .stepNumber(step.deliveryStep())
                .attemptedAt(step.attemptedAt())
                .outcome(step.outcome())
                .deliveryStatus(step.deliveryStatus())
                .signatureId(step.signatureId())
                .userId(step.userId())
                .supplierId(step.supplierId())
                .departmentId(step.departmentId())
                .vehicleId(step.vehicleId())
                .method(step.method())
                .comment(step.comment())
                .failureReason(step.failureReason())
                .token(step.token())
                .build();
    }

    default LogisticsResponse toResponse(final DeliveryEntity entity) {
        final Delivery delivery = toDomain(entity);
        return new LogisticsResponse(delivery.getDeliveryId(), null, delivery.getShipmentId(),
                DeliverySaveStatus.SAVED, delivery.getTarget());
    }

    Status map(DeliveryStatus deliveryStatus);

    DeliveryStatus map(Status deliveryStatus);

    default DeliveryId map(final String deliveryId) {
        return new DeliveryId(deliveryId);
    }

    default DeliveryStepId mapStep(final java.util.UUID id) {
        return new DeliveryStepId(id);
    }
}
