package com.warehouse.dangerousgood.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.dangerousgood.domain.vo.Shipment;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.ShipmentReadRepository;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentReadRepository repository;

    public ShipmentServiceImpl(final ShipmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Shipment findById(final ShipmentId shipmentId) {
        return repository.findById(shipmentId).map(Shipment::from).orElse(null);
    }
}
