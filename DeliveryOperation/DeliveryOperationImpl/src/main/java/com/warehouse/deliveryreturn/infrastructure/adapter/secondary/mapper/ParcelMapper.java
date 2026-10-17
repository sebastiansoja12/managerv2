package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentIdDto;
import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.DeliverableShipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;

@Mapper
public interface ParcelMapper {

    default DeliverableShipment map(ShipmentDto shipment) {
        return DeliverableShipment.builder()
                .shipmentId(map(shipment.shipmentId()))
                .shipmentStatus(shipment.shipmentStatus().name())
                .recipientEmail(shipment.recipient().getEmail())
                .senderEmail(shipment.sender().getEmail())
                .locked(shipment.locked())
                .build();
    }

    ShipmentId map(final ShipmentIdDto shipmentId);
}
