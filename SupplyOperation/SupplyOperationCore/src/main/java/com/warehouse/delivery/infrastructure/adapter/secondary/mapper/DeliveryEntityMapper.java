package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.enumeration.DeliverySaveStatus;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.delivery.infrastructure.adapter.secondary.enumeration.Status;

@Mapper
public interface DeliveryEntityMapper {
    default DeliveryEntity map(final DeliveryRequest delivery) {
        final DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setDeliveryStatus(map(delivery.getDeliveryStatus()));
        deliveryEntity.setCreated(LocalDateTime.now());
        deliveryEntity.setToken(delivery.getDeliveryToken() == null ? delivery.getReturnToken().toString() : delivery.getDeliveryToken().getValue());
        deliveryEntity.setParcelId(delivery.getShipmentId().getValue());
        deliveryEntity.setDepotCode(delivery.getDepartmentCode().getValue());
        deliveryEntity.setSupplierCode(delivery.getSupplierCode().getValue());
        return deliveryEntity;
    }

    default DeliveryResponse map(final DeliveryEntity deliveryEntity) {
        final DeliveryId deliveryId = new DeliveryId(deliveryEntity.getId());
        final ShipmentId shipmentId = new ShipmentId(deliveryEntity.getParcelId());
        return new DeliveryResponse(deliveryId, null, shipmentId, DeliverySaveStatus.SAVED);
    }

    Status map(final DeliveryStatus deliveryStatus);

    DeliveryStatus map(final Status deliveryStatus);

    default DeliveryId map(final String deliveryId) {
        return new DeliveryId(deliveryId);
    }
}
