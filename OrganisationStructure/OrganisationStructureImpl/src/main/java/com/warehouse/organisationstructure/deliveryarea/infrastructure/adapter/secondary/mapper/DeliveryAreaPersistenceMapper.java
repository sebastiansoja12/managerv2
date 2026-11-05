package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.mapper;

import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity.DeliveryAreaEntity;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity.DeliveryAreaPointEmbeddable;

public class DeliveryAreaPersistenceMapper {

    public DeliveryAreaEntity toEntity(final DeliveryArea deliveryArea) {
        return new DeliveryAreaEntity(
                deliveryArea.getDeliveryAreaId(),
                deliveryArea.getDepartmentId(),
                deliveryArea.getName().value(),
                deliveryArea.getSource(),
                deliveryArea.getBoundary().points().stream()
                        .map(point -> new DeliveryAreaPointEmbeddable(point.latitude(), point.longitude()))
                        .toList(),
                deliveryArea.getCreatedAt(),
                deliveryArea.getUpdatedAt());
    }

    public DeliveryArea toDomain(final DeliveryAreaEntity entity) {
        return DeliveryArea.rehydrate(
                entity.getDeliveryAreaId(),
                entity.getDepartmentId(),
                new DeliveryAreaName(entity.getName()),
                new DeliveryAreaBoundary(entity.getPoints().stream()
                        .map(point -> new GeoPoint(point.getLatitude(), point.getLongitude()))
                        .toList()),
                entity.getSource(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
