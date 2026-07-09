package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;

public interface PathFinderServicePort {

    Result<VoronoiResponse, ErrorCode> determineDeliveryDepartment(final Address address);
}
