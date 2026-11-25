package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.shipment.application.port.secondary.Logger;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.event.ShipmentCanceled;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;
import java.time.LocalDateTime;

public class ShipmentPaymentService {

    private final ShipmentRepository shipmentRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final Logger logger;

    public ShipmentPaymentService(final ShipmentRepository shipmentRepository,
                                  final DomainEventPublisher domainEventPublisher,
                                  final Logger logger) {
        this.shipmentRepository = shipmentRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.logger = logger;
    }

    public void markAsPaid(final PriceQuoteId priceQuoteId) {
        this.shipmentRepository.findByPriceQuoteId(priceQuoteId).ifPresentOrElse(this::markAsPaid,
                () -> this.logger.warn("Paid price quote {} does not belong to any shipment", priceQuoteId.value()));
    }

    public void markAsPaid(final Shipment shipment) {
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.markAsPaid();
        if (previousSnapshot.paymentState() == shipment.getPaymentState()) {
            return;
        }
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentChanged(previousSnapshot, shipment.snapshot(), Instant.now()));
    }

    public void cancelForExpiredPayment(final PriceQuoteId priceQuoteId) {
        this.shipmentRepository.findByPriceQuoteId(priceQuoteId).ifPresentOrElse(this::cancelForExpiredPayment,
                () -> this.logger.warn("Expired price quote {} does not belong to any shipment",
                        priceQuoteId.value()));
    }

    public void cancelForExpiredPayment(final Shipment shipment) {
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.cancelForExpiredPayment(LocalDateTime.now());
        if (previousSnapshot.shipmentStatus() == shipment.getShipmentStatus()) {
            return;
        }
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(previousSnapshot, shipment.snapshot(), Instant.now()));
    }
}
