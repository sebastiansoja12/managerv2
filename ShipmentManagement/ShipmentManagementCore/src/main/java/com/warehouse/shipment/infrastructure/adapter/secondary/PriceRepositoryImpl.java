package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;
import com.warehouse.shipment.domain.vo.Price;

public class PriceRepositoryImpl implements PriceRepository {

    private final PriceReadRepository repository;

    public PriceRepositoryImpl(final PriceReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Price priceByShipmentSize(final ShipmentSize shipmentSize) {
        return repository.findByShipmentSize(shipmentSize)
                .map(Price::from)
                .orElseGet(Price::empty);
    }
}
