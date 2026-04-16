package com.warehouse.shipment.infrastructure.adapter.primary.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;

@Component
public class ShipmentIdApiConverter implements Converter<String, ShipmentIdDto> {

    @Override
    public ShipmentIdDto convert(final String shipmentId) {
        return new ShipmentIdDto(Long.parseLong(shipmentId));
    }
}
