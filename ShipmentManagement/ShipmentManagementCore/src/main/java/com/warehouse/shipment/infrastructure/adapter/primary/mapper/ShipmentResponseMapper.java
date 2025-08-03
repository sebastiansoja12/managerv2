package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import java.nio.charset.StandardCharsets;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentCreateResponse map(final ShipmentCreateResponseDto responseDto);

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    ShipmentCreateResponseDto map(final ShipmentCreateResponse response);

    ShipmentDto map(final Shipment shipment);

    default SignatureDto map(final Signature signature) {
        return new SignatureDto(signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod().name(),
                map(signature.getSignature()));
    }

    default String map(final byte[] bytes) {
        return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

	default MoneyDto map(final Money amount) {
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
