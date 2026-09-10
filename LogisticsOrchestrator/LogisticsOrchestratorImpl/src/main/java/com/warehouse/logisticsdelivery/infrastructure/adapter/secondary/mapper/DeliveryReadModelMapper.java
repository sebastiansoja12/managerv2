package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadItem;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryReadEntity;

public class DeliveryReadModelMapper {

    public DeliveryReadEntity toEntity(final DeliverySnapshot delivery) {
        return DeliveryReadEntity.builder()
                .id(delivery.deliveryId())
                .shipmentId(delivery.shipmentId())
                .targetType(delivery.target().type())
                .targetId(delivery.target().id())
                .type(delivery.type())
                .deliveryStatus(delivery.deliveryStatus())
                .status(delivery.status())
                .originDepartmentId(delivery.originDepartmentId())
                .destinationDepartmentId(delivery.destinationDepartmentId())
                .method(delivery.method())
                .stepCount(delivery.deliverySteps().size())
                .created(delivery.createdAt())
                .deliveredAt(delivery.deliveredAt())
                .build();
    }

    public DeliveryReadItem toItem(final DeliveryReadEntity entity) {
        return new DeliveryReadItem(entity.getId(), entity.getShipmentId(), entity.getTargetType(),
                entity.getTargetId(), entity.getType(), entity.getDeliveryStatus(), entity.getStatus(),
                entity.getOriginDepartmentId(), entity.getDestinationDepartmentId(), entity.getMethod(),
                entity.getStepCount(), entity.getCreated(), entity.getDeliveredAt());
    }
}
