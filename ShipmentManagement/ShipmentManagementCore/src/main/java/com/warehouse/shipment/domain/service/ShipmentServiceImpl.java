package com.warehouse.shipment.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final RouteLogServicePort routeLogServicePort;

    private final SoftwareConfigurationServicePort softwareConfigurationServicePort;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository,
                               final RouteLogServicePort routeLogServicePort,
                               final SoftwareConfigurationServicePort softwareConfigurationServicePort) {
        this.shipmentRepository = shipmentRepository;
        this.routeLogServicePort = routeLogServicePort;
        this.softwareConfigurationServicePort = softwareConfigurationServicePort;
    }

    @Override
	public void createShipment(final Shipment shipment) {
        this.shipmentRepository.createOrUpdate(shipment);
	}

    @Override
    public ShipmentId createCopy(final ShipmentId shipmentId) {
        final ShipmentId newShipmentId = nextShipmentId();
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final Shipment copiedShipment = shipment.snapshot();
        copiedShipment.setShipmentId(newShipmentId);
        this.shipmentRepository.createOrUpdate(copiedShipment);
        return newShipmentId;
    }

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        return this.shipmentRepository.findById(shipmentId);
    }

    @Override
    public void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.update(shipmentUpdate);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.exists(shipmentId);
    }

    @Override
    public RouteProcess initializeRouteProcess(final ShipmentId shipmentId) {
		final SoftwareConfiguration softwareConfiguration = this.softwareConfigurationServicePort.getSoftwareConfiguration();
        return this.routeLogServicePort.initializeRouteProcess(shipmentId, softwareConfiguration);
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeRecipient(recipient);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentType(shipmentType);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentStatus(shipmentStatus);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentRelatedId(relatedShipmentId);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentPriority(shipmentPriority);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeCurrency(currency);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentOriginCountryTo(final ShipmentId shipmentId, final Country originCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentOrigin(originCountry);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentDestinationCountryTo(final ShipmentId shipmentId, final Country destinationCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentDestinationCountry(destinationCountry);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSignatureRequired(signatureRequired);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeDangerousGood(dangerousGood);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentRedirected(relatedShipmentId);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyShipmentRerouted(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentRerouted();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyRelatedShipmentLocked(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentLocked();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyShipmentSent(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentSent();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyShipmentReturned(ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void notifyShipmentDelivered(ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentDelivered();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public ShipmentId nextShipmentId() {
        final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
        return new ShipmentId(Math.abs(randomUUIDBits));
    }
}
