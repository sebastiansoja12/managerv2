package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.application.port.primary.result.ShipmentPageResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.domain.vo.ShipmentListEntry;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import java.util.List;

public class ShipmentQueryPortImpl implements ShipmentQueryPort {
    private final SpecificationRepository<ShipmentSearchCriteria, ShipmentListEntry> specificationShipmentRepository;
    private final ShipmentResultFactory shipmentResultFactory;

    public ShipmentQueryPortImpl(final SpecificationRepository<ShipmentSearchCriteria, ShipmentListEntry> specificationShipmentRepository,
                                 final ShipmentResultFactory shipmentResultFactory) {
        this.specificationShipmentRepository = specificationShipmentRepository;
        this.shipmentResultFactory = shipmentResultFactory;
    }

    @Override
    public ShipmentPageResult searchShipments(final ShipmentSearchCriteria criteria) {
        final List<ShipmentResult> content = specificationShipmentRepository.list(criteria).stream()
                .map(shipmentResultFactory::create)
                .toList();
        final long totalElements = specificationShipmentRepository.count(criteria);
        final int size = criteria.pageSize();
        final int totalPages = (int) ((totalElements + size - 1) / size);
        return new ShipmentPageResult(content, criteria.pageNumber(), size, totalElements, totalPages);
    }
}
