package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadItem;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryReadItemResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryReadPageResponseDto;

public class DeliveryReadModelResponseMapper {

    public DeliveryReadPageResponseDto map(final DeliveryReadPage page) {
        return new DeliveryReadPageResponseDto(page.items().stream().map(this::map).toList(), page.page(),
                page.size(), page.hasNext());
    }

    private DeliveryReadItemResponseDto map(final DeliveryReadItem item) {
        return new DeliveryReadItemResponseDto(
                item.deliveryId().getId(),
                item.shipmentId() == null ? null : String.valueOf(item.shipmentId().getValue()),
                item.targetType().name(),
                item.targetId(),
                item.type().name(),
                item.deliveryStatus() == null ? null : item.deliveryStatus().name(),
                item.status().name(),
                item.originDepartmentId() == null ? null : String.valueOf(item.originDepartmentId().getValue()),
                item.destinationDepartmentId() == null ? null
                        : String.valueOf(item.destinationDepartmentId().getValue()),
                item.method() == null ? null : item.method().name(),
                item.stepCount(),
                item.createdAt(),
                item.deliveredAt());
    }
}
