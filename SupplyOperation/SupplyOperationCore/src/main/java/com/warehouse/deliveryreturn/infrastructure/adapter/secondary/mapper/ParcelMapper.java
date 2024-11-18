package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelMapper {

    default Shipment map(ShipmentDto shipment) {
        return Shipment.builder()
                .id(shipment.shipmentId().getValue())
                .shipmentStatus(shipment.shipmentStatus().name())
                .recipientEmail(shipment.recipient().getEmail())
                .senderEmail(shipment.sender().getEmail())
                .locked(shipment.locked())
                .build();
    }
}
