package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;


public class PathFinderMockAdapter implements PathFinderServicePort {

    private final PathFinderMockService pathFinderMockService;

    public PathFinderMockAdapter(PathFinderMockService pathFinderMockService) {
        this.pathFinderMockService = pathFinderMockService;
    }

    @Override
    public Result<VoronoiResponse, ShipmentErrorCode> determineDeliveryDepartment(final Address address) {
        return Result.success(pathFinderMockService.determineDeliveryDepot(address));
    }
}
