package com.warehouse.tracking.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentDto;
import com.warehouse.tracking.domain.model.Shipment;
import org.mapstruct.Mapper;

@Mapper
public interface ShipmentEventMapper {

    Shipment map(final ShipmentDto shipment);
}
