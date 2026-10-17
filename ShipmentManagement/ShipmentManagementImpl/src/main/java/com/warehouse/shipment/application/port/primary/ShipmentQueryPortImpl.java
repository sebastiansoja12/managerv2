package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import java.util.List;

public class ShipmentQueryPortImpl implements ShipmentQueryPort {
    private final SpecificationRepository<ShipmentSearchCriteria, Shipment> specificationShipmentRepository;
    private final ShipmentResultFactory shipmentResultFactory;

    public ShipmentQueryPortImpl(final SpecificationRepository<ShipmentSearchCriteria, Shipment> specificationShipmentRepository,
                                 final ShipmentResultFactory shipmentResultFactory) {
        this.specificationShipmentRepository = specificationShipmentRepository;
        this.shipmentResultFactory = shipmentResultFactory;
    }

    @Override
    public List<ShipmentResult> searchShipments(final ShipmentSearchCriteria criteria) {
        return specificationShipmentRepository.list(criteria).stream()
                .map(shipmentResultFactory::create)
                .toList();
    }
}
