package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper
public interface ShipmentResponseMapper {

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    default ShipmentCreateResponseDto map(final ShipmentCreateResponse response) {
        return new ShipmentCreateResponseDto(response.shipmentId().value().toString(),
                response.trackingNumber());
    }

    @Mapping(target = "dangerousGood.weight.value", source = "dangerousGood.weight.weight")
    ShipmentDto map(final Shipment shipment);

    default List<String> map(String value) {
        return List.of(value);
    }

    default SignatureDto map(final Signature signature) {
        if (signature == null) {
            return null;
        }
        return new SignatureDto(signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod().name(),
                map(signature.getSignature()));
    }

    default String map(final byte[] bytes) {
        return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

	default MoneyApi map(final Money amount) {
		return new MoneyApi(amount.getAmount(), amount.getCurrency().name());
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
