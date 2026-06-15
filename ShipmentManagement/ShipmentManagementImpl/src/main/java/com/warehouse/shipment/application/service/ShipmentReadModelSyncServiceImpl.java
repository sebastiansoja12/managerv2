package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.model.Shipment;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        return syncReadModels(shipments);
    }

    @Transactional
    @Override
    public int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo) {
        final List<Shipment> shipments = this.shipmentRepository.findAllCreatedBetween(
                dateFrom.atStartOfDay(),
                dateTo.plusDays(1).atStartOfDay());
        return syncReadModels(shipments);
    }

    private int syncReadModels(final List<Shipment> shipments) {
        shipments.stream()
                .map(Shipment::snapshot)
                .forEach(this.readModelRepository::sync);

        return shipments.size();
    }
}
