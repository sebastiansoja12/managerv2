package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;


public class PathFinderMockAdapter implements PathFinderServicePort {

    private final PathFinderMockService pathFinderMockService;

    public PathFinderMockAdapter(PathFinderMockService pathFinderMockService) {
        this.pathFinderMockService = pathFinderMockService;
    }

    @Override
    public City determineDeliveryDepot(Address address) {
        if (Objects.isNull(address) || StringUtils.isEmpty(address.getCity())) {
            throw new DestinationDepotDeterminationException(SHIPMENT_202);
        }
        return pathFinderMockService.determineDeliveryDepot(address);
    }
}
