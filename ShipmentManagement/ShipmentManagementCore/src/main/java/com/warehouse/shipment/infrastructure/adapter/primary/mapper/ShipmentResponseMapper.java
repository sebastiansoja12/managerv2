package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentCreateResponse map(final ShipmentCreateResponseDto responseDto);

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    ShipmentCreateResponseDto map(final ShipmentCreateResponse response);

    ShipmentDto map(final Shipment shipment);

	default MoneyDto map(final Money amount) {
        if (amount == null) {
            return new MoneyDto();
        }
		return new MoneyDto(amount.getAmount(), amount.getCurrency().name());
	}

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        final ShipmentIdDto id;
        if (shipmentId == null) {
            id = new ShipmentIdDto();
        } else {
            id = new ShipmentIdDto(shipmentId.getValue());
        }
        return id;
    }
}
