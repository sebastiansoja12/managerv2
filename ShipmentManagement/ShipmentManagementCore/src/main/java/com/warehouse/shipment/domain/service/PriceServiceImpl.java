package com.warehouse.shipment.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(final PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void determineShipmentPrice(final Shipment shipment) {

    }
}
