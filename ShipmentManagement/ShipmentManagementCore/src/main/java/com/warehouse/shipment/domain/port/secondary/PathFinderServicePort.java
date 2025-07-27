package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;

public interface PathFinderServicePort {

    Result<VoronoiResponse, ShipmentErrorCode> determineDeliveryDepartment(final Address address);
}
