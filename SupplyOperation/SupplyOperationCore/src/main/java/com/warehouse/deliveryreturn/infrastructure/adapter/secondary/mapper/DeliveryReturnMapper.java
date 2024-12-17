package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface DeliveryReturnMapper {

    default DeliveryReturn map(final DeliveryReturnEntity deliveryReturnEntity) {
        final UUID id = UUID.fromString(deliveryReturnEntity.getId());
        final Long shipmentId = deliveryReturnEntity.getParcelId();
        final String departmentCode = deliveryReturnEntity.getDepotCode();
        final String supplierCode = deliveryReturnEntity.getSupplierCode();
        final String deliveryStatus = deliveryReturnEntity.getDeliveryStatus().name();
        final String token = deliveryReturnEntity.getToken();
        return new DeliveryReturn(id, shipmentId, departmentCode, supplierCode, deliveryStatus, token);
    }

    @Mapping(target = "created", expression = "java(getLocalDateTime())")
    @Mapping(target = "supplierCode", source = "supplierCode.value")
    @Mapping(target = "depotCode", source = "departmentCode.value")
    @Mapping(target = "parcelId", source = "shipmentId.value")
    @Mapping(target = "token", constant = "tokenValue")
    // TODO
    DeliveryReturnEntity map(DeliveryReturnDetails deliveryReturnRequest);


    default LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
}
