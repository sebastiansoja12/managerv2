package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Money;
import com.warehouse.shipment.infrastructure.api.dto.*;
import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentResponse map(final ShipmentResponseDto responseDto);

    ShipmentResponseDto map(final ShipmentResponse response);

    ShipmentDto map(final Shipment shipment);

	default MoneyDto map(final Money amount) {
        if (amount == null) {
            return new MoneyDto();
        }
		return new MoneyDto(amount.getAmount(), amount.getCurrency().name());
	}

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }
}
