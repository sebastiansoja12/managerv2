package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.model.Shipment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ShipmentReadModelSyncServiceImpl implements ShipmentReadModelSyncPort {

    private final ShipmentReadModelRepository readModelRepository;

    private final ShipmentRepository shipmentRepository;

    public ShipmentReadModelSyncServiceImpl(final ShipmentReadModelRepository readModelRepository,
                                            final ShipmentRepository shipmentRepository) {
        this.readModelRepository = readModelRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    @Override
    public void syncReadModel(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        this.readModelRepository.sync(shipment.snapshot());
    }

    @Transactional
    @Override
    public int syncReadModels() {
        final List<Shipment> shipments = this.shipmentRepository.findAll();

        shipments.stream()
                .map(Shipment::snapshot)
                .forEach(this.readModelRepository::sync);

        return shipments.size();
    }
}
