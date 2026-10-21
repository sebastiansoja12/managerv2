package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.DeliveryStep;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryStatusColumn;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryStepEntity;
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
                        : DeliveryStatus.valueOf(entity.getDeliveryStatus().name()),
                entity.getStatus(), entity.getOriginDepartmentId(), entity.getDestinationDepartmentId(),
                entity.getPickupMethod(), entity.getMethod(), entity.getPickupPointId(), entity.getDeliveryPickupPointId(),
                Boolean.TRUE.equals(entity.getSignatureRequired()), entity.getInstructions(), entity.getPlannedAt(),
                entity.getDeliveredAt(), entity.getToken(),
                entity.getReplacedByDeliveryId() == null ? null : new DeliveryId(entity.getReplacedByDeliveryId()));
    }

    default DeliveryStep toDomain(final DeliveryStepEntity entity) {
        return new DeliveryStep(entity.getId(), entity.getDeliveryId(), entity.getAttemptedAt(),
                entity.getOutcome(), entity.getDeliveryStatus(), entity.getSignatureId(), entity.getUserId(), entity.getSupplierId(),
                entity.getDepartmentId(), entity.getVehicleId(),
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
                        : DeliveryStatusColumn.valueOf(delivery.getDeliveryStatus().name()))
                .status(delivery.getStatus() == null ? DeliveryLifecycleStatus.CREATED : delivery.getStatus())
                .originDepartmentId(delivery.getOriginDepartmentId())
                .destinationDepartmentId(delivery.getDestinationDepartmentId())
                .pickupMethod(delivery.getPickupMethod())
                .method(delivery.getMethod())
                .pickupPointId(delivery.getPickupPointId())
                .deliveryPickupPointId(delivery.getDeliveryPickupPointId())
                .signatureRequired(Boolean.TRUE.equals(delivery.getSignatureRequired()))
                .instructions(delivery.getInstructions())
                .plannedAt(delivery.getPlannedAt())
                .deliveredAt(delivery.getDeliveredAt())
                .token(delivery.getToken())
                .replacedByDeliveryId(delivery.getReplacedByDeliveryId() == null ? null
                        : delivery.getReplacedByDeliveryId().getId())
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
                .comment(step.comment())
                .failureReason(step.failureReason())
                .token(step.token())
                .build();
    }

    DeliveryStatusColumn map(DeliveryStatus deliveryStatus);

    DeliveryStatus map(DeliveryStatusColumn deliveryStatus);

    default DeliveryId map(final String deliveryId) {
        return new DeliveryId(deliveryId);
    }

    default DeliveryStepId mapStep(final java.util.UUID id) {
        return new DeliveryStepId(id);
    }
}
