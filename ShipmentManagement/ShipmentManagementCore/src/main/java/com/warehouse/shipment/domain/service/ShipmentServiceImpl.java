package com.warehouse.shipment.domain.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.registry.DomainContext;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountryRequest;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
	public void createShipment(final Shipment shipment) {
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentCreatedEvent(shipment.snapshot(), Instant.now()));
    }

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        return this.shipmentRepository.findById(shipmentId);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.exists(shipmentId);
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentSenderChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeRecipient(recipient);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentRecipientChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentId shipmentId, final ShipmentType shipmentType, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        if (relatedShipmentId == null) {
            shipment.changeShipmentType(shipmentType);
        } else {
            shipment.changeShipmentTypeWithRelatedId(shipmentType, relatedShipmentId);
        }
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentTypeChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentStatus(shipmentStatus);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentStatusChangedEvent(shipment.snapshot(), Instant.now()));
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
        DomainContext.eventPublisher().publishEvent(new ShipmentCurrencyChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentIssuerCountryTo(final ShipmentId shipmentId, final CountryCode originCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeIssuerCountry(originCountry);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentReceiverCountryTo(final ShipmentId shipmentId, final CountryCode destinationCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeReceiverCountry(destinationCountry);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
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
        DomainContext.eventPublisher().publishEvent(new ShipmentRedirected(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentRerouted(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentRerouted();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentRerouted(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyRelatedShipmentLocked(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentLocked();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentRelatedLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentSent(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentSent();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentSent(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentReturned(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentReturned(final ShipmentId shipmentId, final String reason, final ReasonCode reasonCode) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentReturnCreated(shipment.snapshot(),
                reasonCode, reason, Instant.now()));
    }

    @Override
    public void notifyShipmentDelivered(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentDelivered();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentDelivered(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyReturnCanceled(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturnCanceled();
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void changeShipmentCountries(final ShipmentCountryRequest request) {
        final Shipment shipment = this.shipmentRepository.findById(request.shipmentId());
        shipment.updateCountries(request);
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void lockShipment(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.lockShipment();
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    public ShipmentId nextShipmentId() {
        final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
        return new ShipmentId(Math.abs(randomUUIDBits));
    }

    @Override
    public void update(final Shipment shipment) {
        this.shipmentRepository.createOrUpdate(shipment);
        DomainContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeRouteProcessId(final ProcessId processId, final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.assignRouteProcessId(processId);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void assignExternalReturnId(final ShipmentId shipmentId, final ReturnId returnId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.assignReturnId(returnId);
        this.shipmentRepository.createOrUpdate(shipment);
    }

    @Override
    public void redirectShipmentToSender(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final Shipment shipmentAfterRedirection = shipment.redirectToSender(shipment.getShipmentRelatedId());
        this.shipmentRepository.createOrUpdate(shipmentAfterRedirection);
    }

    @Override
    @Transactional
    public void changeDestination(final ShipmentId shipmentId, final String destination) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeDestinationDepartment(destination);
        this.shipmentRepository.createOrUpdate(shipment);
    }
}
