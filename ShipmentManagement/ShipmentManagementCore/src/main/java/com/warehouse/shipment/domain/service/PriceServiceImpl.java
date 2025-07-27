package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Currency;
import org.springframework.stereotype.Service;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;
import com.warehouse.shipment.domain.vo.Price;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(final PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price determineShipmentPrice(final ShipmentSize shipmentSize, final Currency currency) {
        return priceRepository.priceByShipmentSize(shipmentSize, currency);
    }
}
