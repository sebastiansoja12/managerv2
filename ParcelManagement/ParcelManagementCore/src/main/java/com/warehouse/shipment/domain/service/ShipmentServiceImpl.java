package com.warehouse.shipment.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentPriority;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
	public void createShipment(final Shipment shipment) {
        shipmentRepository.save(shipment);
	}

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        return shipmentRepository.findById(shipmentId);
    }

    @Override
    public void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.update(shipmentUpdate);
        shipmentRepository.update(shipment);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return shipmentRepository.exists(shipmentId);
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeRecipient(recipient);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentType(shipmentType);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentStatus(shipmentStatus);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentRelatedId(relatedShipmentId);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentPriority(shipmentPriority);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeCurrency(currency);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentOriginCountryTo(final ShipmentId shipmentId, final Country originCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentOrigin(originCountry);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentDestinationCountryTo(final ShipmentId shipmentId, final Country destinationCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentDestinationCountry(destinationCountry);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSignatureRequired(signatureRequired);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeDangerousGood(dangerousGood);
        shipmentRepository.update(shipment);
    }

    @Override
    public void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentRedirected(relatedShipmentId);
        shipmentRepository.update(shipment);
    }

    @Override
    public void notifyRelatedShipmentLocked(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentLocked();
        shipmentRepository.update(shipment);
    }

    @Override
    public ShipmentId nextShipmentId() {
        final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
        return new ShipmentId(Math.abs(randomUUIDBits));
    }
}
