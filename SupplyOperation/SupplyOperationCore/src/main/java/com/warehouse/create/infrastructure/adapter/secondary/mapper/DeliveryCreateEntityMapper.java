package com.warehouse.create.infrastructure.adapter.secondary.mapper;

import com.warehouse.create.domain.model.Request;
import com.warehouse.create.infrastructure.adapter.secondary.entity.DeliveryCreateEntity;
import com.warehouse.delivery.infrastructure.adapter.secondary.enumeration.Status;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface DeliveryCreateEntityMapper {
    default DeliveryCreateEntity map(Request request) {
        final DeliveryCreateEntity entity = new DeliveryCreateEntity();
        entity.setCreated(LocalDateTime.now());
        entity.setDeliveryStatus(Status.valueOf(request.getProcessType().name()));
        entity.setDepotCode(request.getDeviceInformation().getDepotCode());
        entity.setParcelId(request.getParcelId());
        entity.setSupplierCode(request.getDeviceInformation().getUsername());
        return entity;
    }
}
